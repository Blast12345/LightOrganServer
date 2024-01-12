package toolkit.monkeyTest

import config.children.Client
import kotlin.random.Random

fun nextClient(): Client {
    return Client(
        ip = nextString(),
        port = Random.nextInt(until = 65535)
    )
}
