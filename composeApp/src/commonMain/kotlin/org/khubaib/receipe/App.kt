package org.khubaib.receipe

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.khubaib.receipe.network.model.User
import org.khubaib.receipe.theme.AppTheme

@Composable
internal fun App() = AppTheme {

    var extended by remember {
        mutableStateOf(false)
    }
    val userList = generateRandomUsers(40)
    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .weight(0.9f)
                ) {
                    Text(
                        "This is KMP Title",
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "This is KMP Description. A demonstration of how to use the Realm database in a Kotlin Multiplatform application.An application that consumes data from The Movie Database to display current trending, upcoming, and popular movies and TV shows. Requires that you create an API key with The Movie Database.",
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Normal,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (extended) {
                        Text(
                            "A collection of code and tools for Kotlin Multiplatform development. Designed to showcase libraries, architectural choices, and best practices when building Kotlin Multiplatform applications. The use case is downloading and displaying information about dog breeds. Introduced in this video tutorial. A simple calculator application. Showing how to integrate Kotlin and native code using expected and actual declarations.",
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Normal,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                IconButton(
                    onClick = { extended = !extended },
                    modifier = Modifier.weight(0.1f)
                ) {
                    Icon(
                        imageVector = if (extended) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

        }
        Box(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(
                WindowInsets.safeDrawing
            )
        ) {
            val state = rememberLazyListState()
            LazyColumn(state = state) {
                items(userList) { user ->
                    UserItem(user = user)
                    Divider(color = Color.LightGray, thickness = 1.dp)
                }
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(scrollState = state),
                modifier = Modifier.wrapContentHeight().align(alignment = Alignment.CenterEnd),
                style = ScrollbarStyle(
                    minimalHeight = 10.dp,
                    thickness = 4.dp,
                    hoverColor = Color.LightGray,
                    shape = RoundedCornerShape(4.dp),
                    hoverDurationMillis = 200,
                    unhoverColor = Color.White
                )
            )
        }
    }
}

fun generateRandomUsers(size: Int): List<User> {
    val userList = mutableListOf<User>()
    val names =
        listOf("John", "Doe", "Alice", "Bob", "Eva", "Charlie", "Emma", "Oliver", "Sophia", "Liam")
    for (i in 1..size) {
        val name = names.random()
        val profileUrl = "https://github.com/$name"
        val user = User(name, profileUrl)
        userList.add(user)
    }
    return userList
}

@Composable
fun UserItem(user: User) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = user.url,
            color = Color.Blue,
            fontSize = 14.sp,
            modifier = Modifier.clickable {
                // Handle click action here
            }
        )
    }
}

internal expect fun openUrl(url: String?)