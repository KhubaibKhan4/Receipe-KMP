package org.khubaib.receipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.Logger
import org.khubaib.receipe.data.model.RecipeX
import org.khubaib.receipe.data.model.Recipes
import org.khubaib.receipe.data.remote.getRecipes
import org.khubaib.receipe.theme.AppTheme
import org.khubaib.receipe.theme.LocalThemeIsDark
import org.khubaib.receipe.ui.AppViewModel
import org.khubaib.receipe.ui.components.RecipeList
import org.khubaib.receipe.util.RandomViewModel
import org.khubaib.receipe.util.network.DataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun App() = AppTheme {

    val httpClient: HttpClient = HttpClient {
        // configure the HTTP client here if needed
    }
    var recipeData by remember {
        mutableStateOf(listOf<RecipeX>())
    }
    var recipeSearchData by remember {
        mutableStateOf<DataState<Recipes>?>(null)
    }
    var search_recipe_Data by remember {
        mutableStateOf(listOf<RecipeX>())
    }
    var text by remember {
        mutableStateOf("")
    }

    var isSearch by remember {
        mutableStateOf(false)
    }

    val appViewModel: RandomViewModel = remember { RandomViewModel(httpClient) }
    val appViewModels: AppViewModel = remember{ AppViewModel(httpClient) }

    LaunchedEffect(true) {
        appViewModel.randomRecipes()
        appViewModels.recipeRandom()
        KtorSimpleLogger("MAIN")
        appViewModel.searchRecipes(text)
        recipeData = getRecipes().recipes!!

    }


    Column(
        modifier = Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            var isDark by LocalThemeIsDark.current
            IconButton(
                onClick = { isDark = !isDark }
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp).size(20.dp),
                    imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = null
                )
            }


        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {

            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text("Search Recipe")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        appViewModel.searchRecipes(text)
                        recipeSearchData = appViewModel.searchRecipe.value
                        isSearch = !isSearch
                    }) {
                        Image(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(0.70f)
                    .clip(shape = RoundedCornerShape(24.dp)),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

        }


        if (!isSearch) {
            appViewModel.randomRecipe.collectAsState().value.let {
                when (it) {
                    is DataState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is DataState.Success<Recipes> -> {
                        RecipeList(recipeData)
                        Napier.d("${it.data}", tag = "MAIN")
                        Napier.d("${recipeData}", tag = "MAIN")
                    }

                    is DataState.Error -> {
                        SelectionContainer {

                            Text("${it.exception.message} ${it.exception}")
                        }
                        Napier.d("${it.exception}", tag = "MAIN")
                    }

                    else -> {

                    }
                }
            }
        } else {
            recipeSearchData.let {
                when (it) {
                    is DataState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is DataState.Success<Recipes> -> {

                        it.data.recipes?.let { it1 -> RecipeList(it1) }
                        Napier.d("${it.data}", tag = "MAIN")
                        Napier.d("${recipeData}", tag = "MAIN")
                    }

                    is DataState.Error -> {
                        SelectionContainer {

                            Text("${it.exception.message} ${it.exception}")
                        }
                        Napier.d("${it.exception}", tag = "MAIN")
                    }

                    else -> {

                    }
                }
            }
        }

    }

}


internal expect fun openUrl(url: String?)


