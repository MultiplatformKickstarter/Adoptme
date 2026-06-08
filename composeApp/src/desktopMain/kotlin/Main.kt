import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.multiplatformkickstarter.app.MainApp
import com.multiplatformkickstarter.app.di.commonModule
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme
import org.koin.core.context.startKoin

fun main() =
    application {
        startKoin {
            modules(commonModule)
        }
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(placement = WindowPlacement.Maximized),
            title = "Adoptme",
        ) {
            MultiplatformKickstarterTheme {
                MainApp()
            }
        }
    }
