package config

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextConfig

class ConfigProviderTests {

    private val factoryConfig = nextConfig()

    @BeforeEach
    fun setup() {
        mockkConstructor(ConfigFactory::class)
        every { anyConstructed<ConfigFactory>().create() } returns factoryConfig

        mockkConstructor(ConfigPersistenceHelper::class)
        every { anyConstructed<ConfigPersistenceHelper>().persistStateChanges(any()) } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ConfigProvider {
        return ConfigProvider()
    }

    @Test
    fun `get the current config`() {
        val sut = createSUT()
        val actual = sut.current
        assertEquals(factoryConfig, actual)
    }

    @Test
    fun `the current config is the same between instances`() {
        val sut1 = createSUT()
        val sut2 = createSUT()
        assertEquals(sut1.current, sut2.current)
    }

    @Test
    fun `changes to the config are persisted`() {
        createSUT()
        verify { anyConstructed<ConfigPersistenceHelper>().persistStateChanges(factoryConfig) }
    }

}