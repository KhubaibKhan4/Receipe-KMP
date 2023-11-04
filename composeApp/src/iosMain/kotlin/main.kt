import androidx.compose.ui.window.ComposeUIViewController
import org.khubaib.receipe.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }


