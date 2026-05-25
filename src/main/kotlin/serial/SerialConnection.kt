package gateway.serial

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import logging.Logger
import wrappers.serial.SerialFormat
import wrappers.serial.SerialPort

class SerialConnection(
    private val serialPort: SerialPort,
    // TODO: Make public?
    private val baudRate: Int,
    private val format: SerialFormat,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO) // TODO: Capture scope?
) {

    private val _readJob = MutableStateFlow<Job?>(null)
    private val _incomingLines = MutableSharedFlow<String>()

    val isConnected: StateFlow<Boolean> = _readJob.map { it != null }.stateIn(scope, SharingStarted.Eagerly, false)
    val incomingLines: Flow<String> = _incomingLines.asSharedFlow()

    fun connect() {
        if (isConnected.value) return

        serialPort.open(baudRate, format)

        _readJob.value = scope.launch {
            try {
                startReadLoop()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Logger.error(e.message ?: "${serialPort.systemPath} read loop has failed unexpectedly.")
            }
        }.also { job ->
            job.invokeOnCompletion {
                serialPort.close()
                _readJob.value = null
            }
        }
    }

    private suspend fun startReadLoop() {
        while (currentCoroutineContext().isActive) {
            val line = serialPort.readNextLine()
            _incomingLines.emit(line)
        }
    }

    fun disconnect() {
        _readJob.value?.cancel()
    }

    fun sendLine(line: String) {
        serialPort.writeLine(line)
    }

}