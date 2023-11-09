import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.khubaib.receipe.repository.Repository
import org.khubaib.receipe.theme.AppTheme
import org.khubaib.receipe.theme.LocalThemeIsDark
import org.khubaib.receipe.ui.Desktop
import org.khubaib.receipe.util.RecipeState
import org.khubaib.receipe.viewmodel.MainViewModel
import java.awt.Dimension

fun main() = application {
    Window(
        title = "Receipe-KMP",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        //App()

        AppTheme {
            val scope = rememberCoroutineScope()
            var text by remember {
                mutableStateOf("")
            }
            var isSearch by remember { mutableStateOf(false) }
            var isRecipeData by remember { mutableStateOf(false) }
            val repository = Repository()
            val viewModel = MainViewModel(repository)

            var recipesState by remember { mutableStateOf<RecipeState>(RecipeState.Loading) }

            LaunchedEffect(isRecipeData) {
                viewModel.getRandomRecipes()

                viewModel.randomRecipes.collect() {
                    recipesState = it
                }
            }
            LaunchedEffect(isSearch) {
                viewModel.getSearchRecipes(text)

                viewModel.searchRecipes.collect() {
                    recipesState = it
                }
            }
            var isDark by LocalThemeIsDark.current
            val newIcon = rememberVectorPainter(image = Icons.Default.FileOpen)
            val refreshIcon = rememberVectorPainter(image = Icons.Default.Refresh)
            val exitIcon = rememberVectorPainter(image = Icons.Default.ExitToApp)
            val darkIcon = rememberVectorPainter(image = Icons.Default.DarkMode)
            val lightIcon = rememberVectorPainter(image = Icons.Default.LightMode)

            MenuBar {
                // File Menu
                Menu(text = "File", mnemonic = 'F') {
                    Item(
                        text = "New Recipe", mnemonic = 'N', enabled = true,
                        icon = newIcon,
                        shortcut = KeyShortcut(key = Key.N, ctrl = true)
                    ) {

                    }
                    Item(
                        text = "Refresh", mnemonic = 'R', enabled = true,
                        icon = refreshIcon,
                        shortcut = KeyShortcut(key = Key.R, ctrl = true)
                    ) {

                    }
                    Item(
                        text = "Exit", mnemonic = 'E', enabled = true,
                        icon = exitIcon,
                        shortcut = KeyShortcut(key = Key.Escape, ctrl = true)
                    ) {

                    }

                }

                //Setting Menu
                Menu(text = "Setting", mnemonic = 'S') {
                    Item(
                        text = if (isDark) "Light Theme" else "Dark Theme",
                        mnemonic = if (isDark) 'L' else 'D',
                        enabled = true,
                        icon = darkIcon,
                        shortcut = KeyShortcut(key = if (isDark) Key.L else Key.D, ctrl = true)
                    ) {
                        isDark = !isDark
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                when (recipesState) {
                    is RecipeState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is RecipeState.Success -> {
                        val recipes = (recipesState as RecipeState.Success).recipes
                        Desktop(recipes)


                    }

                    is RecipeState.Error -> {
                        val error = (recipesState as RecipeState.Error).error
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SelectionContainer {
                                Text("Error While Loading Data...$error ")

                            }
                            IconButton(onClick = { isRecipeData = !isRecipeData }) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}

