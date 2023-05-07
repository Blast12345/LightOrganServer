package config

import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.prefs.Preferences
import kotlin.random.Random

class PersistedConfigTests {

    private val preferences: Preferences = mockk(relaxed = true)

    private val startAutomaticallyKey = "startAutomaticallyKey"

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): PersistedConfig {
        return PersistedConfig(
            preferences = preferences
        )
    }

    @Test
    fun `start automatically is read from preferences`() {
        val sut = createSUT()
        sut.startAutomatically
        verify { preferences.getBoolean(startAutomaticallyKey, any()) }
    }

    @Test
    fun `start automatically defaults to false`() {
        val sut = createSUT()
        sut.startAutomatically
        verify { preferences.getBoolean(startAutomaticallyKey, false) }
    }

    @Test
    fun `start automatically is set to preferences`() {
        val sut = createSUT()

        val newValue = Random.nextBoolean()
        sut.startAutomatically = newValue

        verify { preferences.putBoolean(startAutomaticallyKey, newValue) }
    }

}