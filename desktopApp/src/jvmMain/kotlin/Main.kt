
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.multiplatformkickstarter.app.MainApp
import com.multiplatformkickstarter.app.di.commonModule
import com.multiplatformkickstarter.app.ui.theme.MultiplatformKickstarterTheme
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(commonModule)
    }
    Window(onCloseRequest = ::exitApplication) {
        MultiplatformKickstarterTheme {
            MainApp()
        }
    }
}
