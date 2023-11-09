package org.khubaib.receipe.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import org.khubaib.receipe.data.model.Meal
import org.khubaib.receipe.data.model.Recipes
import org.khubaib.receipe.openUrl
import org.khubaib.receipe.theme.LocalThemeIsDark
import org.khubaib.receipe.ui.components.Ingredients


@Composable
fun Desktop(recipes: Recipes) {
    val selectedRecipe = remember { mutableStateOf<Meal?>(null) }
    val backIconClicked = remember { mutableStateOf(false) }
    var isDark by LocalThemeIsDark.current
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        val state = rememberScrollState()
        LeftPanel(
            modifier = Modifier
                .weight(0.45f)
                .height(4000.dp)
                .verticalScroll(state), recipes, selectedRecipe
        )

        Box {

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(scrollState = state),
                modifier = Modifier.align(Alignment.CenterEnd)
                    .pointerHoverIcon(icon = PointerIcon.Hand),
                style = ScrollbarStyle(
                    minimalHeight = 10.dp,
                    thickness = 4.dp,
                    shape = RoundedCornerShape(4.dp),
                    hoverDurationMillis = 100,
                    unhoverColor =if (isDark) Color.LightGray.copy(alpha = 0.50f) else Color.DarkGray,
                    hoverColor =if (isDark) Color.LightGray else Color.DarkGray
                )
            )
        }
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )


        Box(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight()
        ) {
            if (selectedRecipe.value != null) {
                RightPanel(
                    Modifier.fillMaxHeight(), meal = selectedRecipe.value!!,
                    backIconClicked.value, onBackIconClicked = {
                        backIconClicked.value = false
                        selectedRecipe.value = null
                    }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image, contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Select a recipe from the left panel to view details",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun LeftPanel(
    modifier: Modifier,
    recipes: Recipes,
    selectedRecipe: MutableState<Meal?>
) {

    var text by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .height(4000.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text("Search Recipe")
            },
            modifier = Modifier
                .wrapContentWidth()
                .height(85.dp)
                .pointerHoverIcon(icon = PointerIcon.Text)
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(24.dp))
                .clickable {
                    isEnabled = !isEnabled
                },
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        isEnabled = !isEnabled
                    },
                    modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                ) {
                    Image(imageVector = Icons.Default.Search, contentDescription = null)
                }
            },
            enabled = isEnabled
        )
        DesktopRecipeList(recipes) {
            selectedRecipe.value = it
        }

    }
}


@Composable
fun RightPanel(
    modifier: Modifier,
    meal: Meal?,
    backIcon: Boolean,
    onBackIconClicked: () -> Unit
) {
    var isDark by LocalThemeIsDark.current
    val state = rememberScrollState()
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .animateContentSize()

    ) {
        if (meal == null || backIcon) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Select a recipe from the left panel to view details",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .animateContentSize()
                    .verticalScroll(state = state)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        onBackIconClicked()
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                        )
                    }

                    Spacer(modifier = Modifier.width(48.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberImagePainter(
                        url = meal.strMealThumb
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(220.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, Color.LightGray, MaterialTheme.shapes.medium)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = meal.strMeal,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Summary",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = meal.strInstructions,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ingredients",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Ingredients(ingredients = meal.strIngredient1, amount = meal.strMeasure1)
                Ingredients(ingredients = meal.strIngredient2, amount = meal.strMeasure2)
                Ingredients(ingredients = meal.strIngredient3, amount = meal.strMeasure3)
                Ingredients(ingredients = meal.strIngredient4, amount = meal.strMeasure4)
                Ingredients(ingredients = meal.strIngredient5, amount = meal.strMeasure5)
                Ingredients(ingredients = meal.strIngredient6, amount = meal.strMeasure6)
                Ingredients(ingredients = meal.strIngredient7, amount = meal.strMeasure7)
                Ingredients(ingredients = meal.strIngredient8, amount = meal.strMeasure8)
                meal.strIngredient9?.let {
                    Ingredients(
                        ingredients = it,
                        amount = meal.strMeasure9
                    )
                }
                meal.strIngredient10?.let {
                    Ingredients(
                        ingredients = it,
                        amount = meal.strMeasure10
                    )
                }
                meal.strIngredient11?.let {
                    Ingredients(
                        ingredients = it,
                        amount = meal.strMeasure11
                    )
                }
                meal.strIngredient12?.let {
                    Ingredients(
                        ingredients = it,
                        amount = meal.strMeasure12
                    )
                }
                meal.strIngredient13?.let {
                    Ingredients(
                        ingredients = it,
                        amount = meal.strMeasure13
                    )
                }
                meal.strIngredient14?.let {
                    Ingredients(
                        ingredients = it,
                        amount = meal.strMeasure14
                    )
                }
                meal.strIngredient15?.let {
                    Ingredients(
                        ingredients = it,
                        amount = meal.strMeasure15
                    )
                }
                meal.strIngredient16?.let {
                    meal.strMeasure16?.let { it1 ->
                        Ingredients(
                            ingredients = it,
                            amount = it1
                        )
                    }
                }
                meal.strIngredient17?.let {
                    meal.strMeasure17?.let { it1 ->
                        Ingredients(
                            ingredients = it,
                            amount = it1
                        )
                    }
                }
                meal.strIngredient18?.let {
                    meal.strMeasure18?.let { it1 ->
                        Ingredients(
                            ingredients = it,
                            amount = it1
                        )
                    }
                }
                meal.strIngredient19?.let {
                    meal.strMeasure19?.let { it1 ->
                        Ingredients(
                            ingredients = it,
                            amount = it1
                        )
                    }
                }
                Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.ConfirmationNumber,
                        contentDescription = "Mean Id",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Meal ID: ${meal.idMeal}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Category,
                        contentDescription = "Meal Category",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Recipe Category: ${meal.strCategory}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AreaChart,
                        contentDescription = "Area Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Country: ${meal.strArea}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        openUrl(meal.strYoutube)
                    }) {
                    Icon(
                        Icons.Default.OpenInNew,
                        contentDescription = "Url Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Youtube Source Link",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState = state),
            modifier = Modifier.align(Alignment.CenterEnd)
                .pointerHoverIcon(icon = PointerIcon.Hand),
            style = ScrollbarStyle(
                minimalHeight = 10.dp,
                thickness = 4.dp,
                shape = RoundedCornerShape(4.dp),
                hoverDurationMillis = 100,
                unhoverColor =if (isDark) Color.LightGray.copy(alpha = 0.50f) else Color.DarkGray,
                hoverColor =if (isDark) Color.LightGray else Color.DarkGray
            )
        )

    }
}




