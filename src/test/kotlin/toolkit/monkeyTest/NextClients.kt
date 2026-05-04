package toolkit.monkeyTest

import config.children.Client

fun nextClients(length: Int = nextInt(3)): Set<Client> {
    val list = mutableSetOf<Client>()

    repeat(length) {
        list.add(nextClient())
    }

    return list
}
