package dsp.clean

import extensions.minus
import extensions.times
import org.apache.commons.math3.complex.Complex
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class CleanResult<Component, Data>(
    val components: List<Component>,
    val residual: Data
)

interface CleanComponent {
    val magnitude: Double
}

// TODO: Reference
abstract class CleanAlgorithm<Data, Component : CleanComponent> {

    fun clean(
        dirtyData: Data,
        impulseResponseFunction: (Component) -> Data,
        loopGain: Double,
        maxIterations: Int,
        magnitudeThreshold: Double
    ): CleanResult<Component, Data> {
        require(maxIterations > 0) { "The number of iterations must be greater than 0" }
        require(magnitudeThreshold >= 0.0) { "The magnitude threshold must greater than or equal to 0" }
        require(loopGain > 0.0 && loopGain <= 1.0) { "The loop gain must be between 0.0 and 1.0" }

        var residual = dirtyData
        val components = mutableListOf<Component>()

        repeat(maxIterations) {
            val component = findComponent(residual)

            if (component.magnitude < magnitudeThreshold) {
                return CleanResult(components, residual)
            }

            val scaledComponent = scaleComponent(component, loopGain)
            components.add(scaledComponent)

            val impulseResponse = impulseResponseFunction(scaledComponent)
            residual = subtract(residual, impulseResponse)
        }

        return CleanResult(components, residual)
    }


    abstract fun findComponent(residual: Data): Component
    abstract fun scaleComponent(component: Component, loopGain: Double): Component
    abstract fun subtract(data: Data, impulseResponse: Data): Data

}

data class InterpolatedCleanComponent1D(
    val position: Double,
    val value: Complex,
) : CleanComponent {
    override val magnitude: Double
        get() = value.abs()
}

class InterpolatedCleanAlgorithm1D : CleanAlgorithm<List<Complex>, InterpolatedCleanComponent1D>() {

    override fun findComponent(residual: List<Complex>): InterpolatedCleanComponent1D {
        val index = residual.withIndex().maxBy { it.value.abs() }.index
        return interpolateComponent(residual, index)
    }

    private fun interpolateComponent(residual: List<Complex>, index: Int): InterpolatedCleanComponent1D {
        if (index == 0 || index == residual.lastIndex) {
            return InterpolatedCleanComponent1D(index.toDouble(), residual[index])
        }

        val previous = residual[index - 1].abs()
        val current = residual[index].abs()
        val next = residual[index + 1].abs()

        val denominator = previous - 2 * current + next
        if (denominator == 0.0) {
            return InterpolatedCleanComponent1D(index.toDouble(), residual[index])
        }

        val delta = 0.5 * (previous - next) / denominator

        val interpolatedMagnitude = current - 0.25 * (previous - next) * delta

        val currentPhase = atan2(y = residual[index].imaginary, x = residual[index].real)
        val neighborPhase = if (delta >= 0) {
            atan2(residual[index + 1].imaginary, residual[index + 1].real)
        } else {
            atan2(residual[index - 1].imaginary, residual[index - 1].real)
        }
        val interpolatedPhase = currentPhase + abs(delta) * (neighborPhase - currentPhase)

        val value = Complex(
            interpolatedMagnitude * cos(interpolatedPhase),
            interpolatedMagnitude * sin(interpolatedPhase)
        )

        return InterpolatedCleanComponent1D(index + delta, value)
    }

    override fun scaleComponent(
        component: InterpolatedCleanComponent1D,
        loopGain: Double
    ): InterpolatedCleanComponent1D {
        return InterpolatedCleanComponent1D(
            position = component.position,
            value = component.value * loopGain
        )
    }

    override fun subtract(
        data: List<Complex>,
        impulseResponse: List<Complex>
    ): List<Complex> {
        return data.zip(impulseResponse) { a, b -> a - b }
    }
    //    init {
//        require(configuration.loopGain > 0.0 && configuration.loopGain <= 1.0) { "The loop gain must be between 0 and 1." }
//        require(configuration.maxIterations > 0) { "The maximum number of iterations must be greater than zero." }
//        require(configuration.magnitudeThreshold >= 0) { "The threshold must be non-negative." }
//    }
//
//    data class Component(
//        val position: Int,
//        val amplitude: Complex
//    )
//
//    data class Result(
//        val components: List<Component>,
//        val residual: List<Complex>
//    )
//
//    // TODO: Is dirty accurate?
//    fun deconvolve(dirtySignal: List<Complex>, pointSpreadFunction: PointSpreadFunction1D): Result {
//        var residual = dirtySignal.toList() // what remains of the original signal after each deconvolution
//        val components = mutableListOf<Component>() // the extracted point sources (i.e. real peaks)
//
//        repeat(configuration.maxIterations) {
//            val peak = findPeak(residual)
//
//            if (peak.amplitude.abs() < configuration.magnitudeThreshold) {
//                return Result(components, residual)
//            }
//
//            val scaledPeak = Component(peak.position, peak.amplitude * configuration.loopGain)
//            components.add(scaledPeak)
//            residual = subtractShiftedPsf(residual, pointSpreadFunction, scaledPeak) // TODO: Rename function?
//        }
//
//        return Result(components, residual)
//    }
//
//
//    private fun findPeak(residual: List<Complex>): Component {
//        // Because this class is use-case agnostic, technically a peak can be negative, so use the absolute value
//        val peak = residual.withIndex().maxBy { it.value.abs() }
//        return Component(peak.index, peak.value)
//    }
//
//    private fun subtractShiftedPsf(
//        residual: List<Complex>,
//        pointSpreadFunction: PointSpreadFunction1D,
//        scaledPeak: Component
//    ): List<Complex> {
//        return residual.mapIndexed { index, value ->
//            val psfIndex = index - scaledPeak.position + pointSpreadFunction.center
//
//            if (psfIndex in pointSpreadFunction.values.indices) {
//                value - scaledPeak.amplitude * pointSpreadFunction.values[psfIndex]
//            } else {
//                value
//            }
//        }
//    }

}