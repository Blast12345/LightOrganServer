package toolkit.monkeyTest

import config.children.Client

fun nextClientList(): List<Client> {
    return listOf(
        nextClient(),
        nextClient()
    )
}