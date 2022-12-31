fun main() {
    val lightOrgan = LightOrgan()
    lightOrgan.start()

    while (true) {
        if (!lightOrgan.isRunning) {
            lightOrgan.start()
        }
    }
}