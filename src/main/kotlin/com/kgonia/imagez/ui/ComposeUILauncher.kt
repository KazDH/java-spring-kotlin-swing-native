package com.kgonia.imagez.ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.springframework.context.ConfigurableApplicationContext

/**
 * Launches the Compose Desktop UI.
 * Called from Java Spring Boot application after context initialization.
 */
fun launchComposeUI(springContext: ConfigurableApplicationContext) {
    application {
        Window(
            onCloseRequest = {
                // Close Spring context gracefully
                springContext.close()
                exitApplication()
            },
            title = "Imagez"
        ) {
            ImagezApp()
        }
    }
}

