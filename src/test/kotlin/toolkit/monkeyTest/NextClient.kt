package toolkit.monkeyTest

import config.Client
import kotlin.random.Random

fun nextClient(): Client {
    return Client(
        ip = nextString(),
        port = Random.nextInt()
    )
}