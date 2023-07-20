package config

import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextConfig

class ConfigPersisterTests {

    private val scope = TestScope()

    private val config: Config = nextConfig()
    private val persistedConfig: PersistedConfig = mockk(relaxed = true)

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ConfigPersister {
        return ConfigPersister(
            persistedConfig = persistedConfig,
            scope = scope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `the start automatically is persisted when it changes`() {
        val sut = createSUT()
        sut.persist(config)

        val newValue = nextConfig().startAutomatically.value.not()
        config.startAutomatically.value = newValue

        scope.advanceUntilIdle()
        verify { persistedConfig.startAutomatically = newValue }
    }

}
