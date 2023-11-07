@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class, ExperimentalResourceApi::class)

package com.myprojectname.app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.myprojectname.app.ui.theme.Typography
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


class OnBoardingComponent(private val carouselItems: List<CarouselItem>, private val action: () -> Unit) {

    @Composable
    fun DrawCarousel() {
        val state = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { carouselItems.size }
        )

        Column {
            Carousel(state, carouselItems, action)
            Spacer(modifier = Modifier.padding(4.dp))
            DotsIndicator(
                totalDots = carouselItems.size,
                selectedIndex = state.currentPage,
                selectedColor = Color.DarkGray,
                unSelectedColor = Color.Gray
            )
        }
        
    }
}

@Composable
fun Carousel(state: PagerState, carouselItemsList: List<CarouselItem>, action: () -> Unit) {
    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxSize()
    ) {
            Row {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painterResource(carouselItemsList[it].imageResource),
                            "",
                            modifier = Modifier
                                .fillMaxHeight(0.87f)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 10.dp),
                            text = carouselItemsList[it].text,
                            style = Typography.get().headlineSmall
                        )

                        Spacer(modifier = Modifier.padding(vertical = 8.dp))

                        Button(
                            onClick = { action.invoke() },
                            modifier = Modifier.alpha(changeVisibilityIfLastPage(it, carouselItemsList.size - 1))
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 10.dp),
                                text = carouselItemsList[it].text,
                                style = Typography.get().headlineSmall
                            )
                        }
                    }
                }
            }
        }

}

private fun changeVisibilityIfLastPage(currentPage: Int, carouselItemsListSize: Int): Float {
    return if (currentPage == carouselItemsListSize) {
        1f
    } else {
        0f
    }
}

@Composable
fun DotsIndicator(
    totalDots : Int,
    selectedIndex : Int,
    selectedColor: Color,
    unSelectedColor: Color,
    ){

    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .background(Color.Black),
        horizontalArrangement = Arrangement.Center
    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(unSelectedColor)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

data class CarouselItem(val imageResource: String, val text: String)
