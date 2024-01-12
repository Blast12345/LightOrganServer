package lightOrgan

import wrappers.color.Color

interface LightOrganSubscriber {
    fun new(color: Color)
}
