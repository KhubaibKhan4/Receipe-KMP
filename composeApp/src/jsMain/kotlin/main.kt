import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.skiko.wasm.onWasmReady
import org.khubaib.receipe.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("Receipe-KMP") {
           App()
        }
    }
}



