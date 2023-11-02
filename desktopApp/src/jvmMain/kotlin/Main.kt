
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.myprojectname.app.MainApp
import com.myprojectname.app.di.commonModule
import com.myprojectname.app.ui.theme.MyProjectNameTheme
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(commonModule)
    }
    Window(onCloseRequest = ::exitApplication) {
        MyProjectNameTheme {
            MainApp()
        }
    }
}
