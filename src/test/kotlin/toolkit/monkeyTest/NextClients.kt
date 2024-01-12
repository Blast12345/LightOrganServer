package toolkit.monkeyTest

import config.children.Client

fun nextClients(length: Int = nextPositiveInt(3)): Set<Client> {
    val list = mutableSetOf<Client>()

    for (i in 0 until length) {
        list.add(nextClient())
    }

    return list
}
