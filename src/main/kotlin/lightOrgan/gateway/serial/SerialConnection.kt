package lightOrgan.gateway.serial

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import serial.SerialFrameFormat
import serial.SerialPort

class SerialConnection(
    private val serialPort: SerialPort,
    private val baudRate: Int,
    private val format: SerialFrameFormat,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO) // TODO: Capture scope?
) {

    private val _readJob = MutableStateFlow<Job?>(null)
    private val _incomingLines = MutableSharedFlow<String>()

    val isConnected: StateFlow<Boolean> = _readJob.map { it != null }.stateIn(scope, SharingStarted.Eagerly, false)
    val incomingLines: Flow<String> = _incomingLines.asSharedFlow()
    val portPath = serialPort.name // TODO: Test

    fun connect() {
        TODO()
//        if (isConnected.value) return
//
//        serialPort.open(baudRate, format)
//
//        _readJob.value = scope.launch {
//            try {
//                startReadLoop()
//            } catch (e: CancellationException) {
//                throw e
//            } catch (e: Exception) {
//                Logger.error(e.message ?: "${serialPort.portPath} read loop has failed unexpectedly.")
//            }
//        }.also { job ->
//            job.invokeOnCompletion {
//                serialPort.close()
//                _readJob.value = null
//            }
//        }
    }

    private suspend fun startReadLoop() {
        TODO()
//        while (currentCoroutineContext().isActive) {
//            val line = serialPort.readNextLine()
//
//            if (line.isNotEmpty()) {
//                _incomingLines.emit(line)
//            }
//        }
    }

    fun disconnect() {
        TODO()
//        serialPort.close()
//        _readJob.value?.cancel()
//        _readJob.value = null
    }

    suspend fun write(line: String) {
        TODO()
//        serialPort.writeLine(line) // TODO: What happens if a write fails?
    }

}