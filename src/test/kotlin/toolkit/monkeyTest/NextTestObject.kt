package toolkit.monkeyTest

data class TestObject(val value: Int)

fun nextTestObject(): TestObject {
    return TestObject(
        value = nextInt()
    )
}