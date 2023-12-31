package org.khubaib.receipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.skiko.OS
import org.khubaib.receipe.repository.Repository
import org.khubaib.receipe.theme.AppTheme
import org.khubaib.receipe.theme.LocalThemeIsDark
import org.khubaib.receipe.ui.components.RecipeList
import org.khubaib.receipe.ui.components.TopSlider
import org.khubaib.receipe.util.RecipeState
import org.khubaib.receipe.viewmodel.MainViewModel
/*
Android + iOS + Desktop + Web is ready
Developed By

Muhammad Khubaib Imtiaz

For Collaboration or Freelance Contact Below
Email: 18.bscs.803@gmailc.om
Github: @KhubaibKhan4
Linkedin: @KhubaibKhanDev

 */
@Composable
internal fun App() = AppTheme {

    //Coroutine Scope
    val scope = rememberCoroutineScope()
    //Search Text
    var text by remember {
        mutableStateOf("")
    }
    //Search State Management
    var isSearch by remember { mutableStateOf(false) }

    //Recipe Data State Management
    var isRecipeData by remember { mutableStateOf(false) }

    //Repository for Fetching Data
    val repository = Repository()

    //ViewModel for KMP
    val viewModel = MainViewModel(repository)

    // RecipeState Management
    var recipesState by remember { mutableStateOf<RecipeState>(RecipeState.Loading) }

    //For Recipe Results
    LaunchedEffect(isRecipeData) {
        viewModel.getRandomRecipes()

        viewModel.randomRecipes.collect() {
            recipesState = it
        }
    }

    //For Search Results
    LaunchedEffect(isSearch) {
        viewModel.getSearchRecipes(text)

        viewModel.searchRecipes.collect() {
            recipesState = it
        }
    }




    Column(
        modifier = Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /*Condition for Displaying the Content
        Only for Android OS*/
        if (OS.Android == OS.Android) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text("Search Recipe")
                    },
                    trailingIcon = {
                        IconButton(onClick = {
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
        }


        //Recipe State
        when (recipesState) {

            //Loading State Management
            is RecipeState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            //Success State Management
            is RecipeState.Success -> {
                val recipes = (recipesState as RecipeState.Success).recipes
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TopSlider(recipes, scope)
                    Spacer(modifier = Modifier.height(8.dp))
                    RecipeList(recipes)
                }
            }

            //Error State Management
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


internal expect fun openUrl(url: String?)

