package org.khubaib.receipe.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.YoutubeSearchedFor
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import org.khubaib.receipe.data.model.Meal
import org.khubaib.receipe.data.model.Recipes
import org.khubaib.receipe.openUrl

@Composable
fun RecipeList(recipes: Recipes) {
    val state = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = true,
        state = state
    ) {
        items(recipes.meals) { recipe ->
            RecipeItem(recipe)
        }
    }

}

@Composable
fun RecipeItem(recipe: Meal) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val painter =
            rememberImagePainter(url = recipe.strMealThumb!!)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shadow(elevation = 4.dp, shape = RectangleShape),
            contentScale = ContentScale.Crop,

            )

        Spacer(modifier = Modifier.height(16.dp))
        val htmlRegex = "<.*?>".toRegex()

        // Replace HTML tags with an empty string
        val plainText = recipe.strInstructions?.replace(htmlRegex, "")
        Text(
            text = recipe.strMeal ?: "",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = plainText!!,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        Ingredients(ingredients = recipe.strIngredient1, amount = recipe.strMeasure1)
        Ingredients(ingredients = recipe.strIngredient2, amount = recipe.strMeasure2)
        Ingredients(ingredients = recipe.strIngredient3, amount = recipe.strMeasure3)
        Ingredients(ingredients = recipe.strIngredient4, amount = recipe.strMeasure4)
        Ingredients(ingredients = recipe.strIngredient5, amount = recipe.strMeasure5)
        Ingredients(ingredients = recipe.strIngredient6, amount = recipe.strMeasure6)
        Ingredients(ingredients = recipe.strIngredient7, amount = recipe.strMeasure7)
        Ingredients(ingredients = recipe.strIngredient8, amount = recipe.strMeasure8)
        Ingredients(ingredients = recipe.strIngredient9, amount = recipe.strMeasure9)
        Ingredients(ingredients = recipe.strIngredient10, amount = recipe.strMeasure10)
        Ingredients(ingredients = recipe.strIngredient11, amount = recipe.strMeasure11)
        Ingredients(ingredients = recipe.strIngredient12, amount = recipe.strMeasure12)
        Ingredients(ingredients = recipe.strIngredient13, amount = recipe.strMeasure13)
        Ingredients(ingredients = recipe.strIngredient14, amount = recipe.strMeasure14)
        Ingredients(ingredients = recipe.strIngredient15, amount = recipe.strMeasure15)
        recipe?.strIngredient16?.let { recipe?.strMeasure16?.let { it1 -> Ingredients(ingredients = it, amount = it1) } }
        recipe.strMeasure17?.let { Ingredients(ingredients = recipe.strIngredient17!!, amount = it) }
        recipe.strIngredient18?.let { recipe.strMeasure18?.let { it1 -> Ingredients(ingredients = it, amount = it1) } }
        recipe.strMeasure19?.let { recipe.strIngredient19?.let { it1 -> Ingredients(ingredients = it1, amount = it) } }
        recipe.strIngredient20?.let { recipe.strMeasure20?.let { it1 -> Ingredients(ingredients = it, amount = it1) } }



        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.ConfirmationNumber,
                contentDescription = "Mean Id",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Meal ID: ${recipe.idMeal}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Category,
                contentDescription = "Meal Category",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Recipe Category: ${recipe.strCategory}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.AreaChart,
                contentDescription = "Area Icon",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Country: ${recipe.strArea}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                openUrl(recipe.strYoutube)
            }) {
            Icon(
                Icons.Default.OpenInNew,
                contentDescription = "Url Icon",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Source Link",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Composable
fun Ingredients(ingredients: String, amount: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ingredient: $ingredients, Amount: $amount",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}



