package org.khubaib.receipe.ui.components

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.seiko.imageloader.rememberImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.khubaib.receipe.data.model.Meal
import org.khubaib.receipe.data.model.Recipes
import org.khubaib.receipe.theme.LocalThemeIsDark
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopSlider(
    meal: Recipes,
    scope: CoroutineScope
) {
    var isDark by LocalThemeIsDark.current
    val state =
        rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
            meal.meals.size
        }
    val pageOffset = state.currentPageOffsetFraction
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(state = state) { page ->
            val pageItem by remember {
                mutableIntStateOf(page)
            }
            val currentItem = meal.meals[pageItem]
            TopSliderItem(meal = currentItem, pagerState = state)
        }
        DotsIndicator(
            pagerState = state,
            modifier = Modifier.padding(8.dp),
            dotSize = 8.dp,
            selectedDotColor = Color.Red,
            unselectedDotColor = if (isDark) Color.White else Color.DarkGray
        )
        // Automatic slide every 200 milliseconds
        LaunchedEffect(state.currentPage) {
            while (true) {
                delay(4000)
                val nextPage = (state.currentPage + 1) % meal.meals.size
                state.animateScrollToPage(
                    nextPage,
                    pageOffsetFraction = pageOffset,
                    animationSpec = spring(
                        dampingRatio = 1f,
                        stiffness = 1f,
                        visibilityThreshold = pageOffset
                    )
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopSliderItem(
    meal: Meal,
    pagerState: PagerState,
) {

    val pageOffset = pagerState.currentPageOffsetFraction

    // Calculate the horizontal offset for the current image
    val offsetX = (pageOffset * 295.dp).coerceIn((-295).dp, 295.dp)

    // Calculate the scale factor to zoom in/out the current image
    val scaleFactor = 0.5f + 0.5f * (1 - pageOffset.absoluteValue)

    Box(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .width(295.dp)
            .height(154.dp)
            .offset(x = offsetX)
            .scale(scaleX = scaleFactor, scaleY = scaleFactor)
            .clickable {
            }
    ) {

        val painter =
            rememberImagePainter(url = meal.strMealThumb!!)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop

        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp, start = 4.dp, end = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${meal.strMeal}",
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 22.sp,
                color = Color(0xFFFFFFFF),
                letterSpacing = 0.12.sp,
            )
            Text(
                text = "${meal.idMeal}",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = Color(0xFFEBEBEF),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotsIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    selectedDotColor: Color = Color.Black,
    unselectedDotColor: Color = Color.Gray
) {
    val pageCount = pagerState.pageCount
    val currentPage = pagerState.currentPage
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { pageIndex ->
            val dotColor = if (pageIndex == currentPage) selectedDotColor else unselectedDotColor
            val size = if (pageIndex == currentPage) 16.dp else dotSize
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    //.size(dotSize)
                    .width(size)
                    .background(color = dotColor, shape = CircleShape)
                    .padding(4.dp) // Adjust the padding as needed
            )
        }
    }
}