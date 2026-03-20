package toolkit.junit

import logging.Logger
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class DisableLoggingExtension : BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        Logger.enabled = false
    }
}