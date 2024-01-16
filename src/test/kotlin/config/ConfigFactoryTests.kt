package config

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ConfigFactoryTests {

    private val persistedConfig: PersistedConfig = mockk()

    @BeforeEach
    fun setupHappyPath() {
        every { persistedConfig.startAutomatically } returns Random.nextBoolean()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ConfigFactory {
        return ConfigFactory(
            persistedConfig = persistedConfig
        )
    }

    @Test
    fun `start automatically is pulled from the persisted config`() {
        val sut = createSUT()
        val config = sut.create()
        assertEquals(persistedConfig.startAutomatically, config.startAutomatically.value)
    }

}
