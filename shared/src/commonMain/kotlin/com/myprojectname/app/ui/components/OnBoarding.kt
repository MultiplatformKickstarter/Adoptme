@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.myprojectname.app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class OnBoardingComponent(carouselItems: List<CarouselItem>, private val action: () -> Unit) {
    /*private val carouselContent: List<CarouselItem> = listOf(
        CarouselItem(R.drawable.img, resources.getResourceName(R.string.carousel_first_page_text)),
        CarouselItem(R.drawable.mklogo, resources.getResourceName(R.string.carousel_first_page_text)),
        CarouselItem(R.drawable.img, resources.getResourceName(R.string.carousel_first_page_text)),
        )*/

    @Composable
    fun DrawCarousel() {
        val state = rememberPagerState()

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


@Composable
fun Carousel(state: PagerState, carouselItemsList: List<CarouselItem>, action: () -> Unit) {
    HorizontalPager(pageCount = carouselItemsList.size, state = state) { page ->
        Row {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /*
                    Image(
                        painterResource(id = carouselItemsList[page].imageResId),
                        "",
                        modifier = Modifier
                            .fillMaxHeight(0.87f)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )*/
                    //
                    Text(
                        text = carouselItemsList[page].text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 10.dp)
                    )

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    Button(
                        onClick = { action.invoke() },
                        modifier = Modifier.alpha(changeVisibilityIfLastPage(currentPage, carouselItemsList.size - 1))
                    ) {
                        Text(
                            text = "Aceptar",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 10.dp),
                            color = Color.Black
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

data class CarouselItem(val imageResId: Int, val text: String)
