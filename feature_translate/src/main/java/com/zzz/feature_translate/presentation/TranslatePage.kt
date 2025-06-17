package com.zzz.feature_translate.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.feature_translate.R
import com.zzz.feature_translate.presentation.components.TranslateTabRow
import kotlinx.coroutines.launch

@Composable
fun TranslateRoot(

) {
    TranslatePage()
}

@Composable
private fun TranslatePage(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val pagerState = rememberPagerState {
        2
    }



    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            TranslateTabRow(
                currentTab = pagerState.currentPage,
                onTabChange = {tab->
                    scope.launch {
                        pagerState.animateScrollToPage(tab)
                    }
                }
            )

            HorizontalPager(
                pagerState ,
                modifier = Modifier.fillMaxWidth(),
            ) {index->
                when(index){
                    0->{
                        Column(
                            Modifier.fillMaxSize()
                                .padding(16.dp)
                        ){

                            Image(
                                painter = painterResource(R.drawable.downloaded_image),
                                contentDescription = "Illustration of a man looking at files",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.width(200.dp)
                                    .aspectRatio(1f)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                "You can download over 50 translation models & easily use them without network!",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            VerticalSpace()
                            Text(
                                "Browse models",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    1->{
                        Column(Modifier.fillMaxSize()){
                            Text("Translate")
                        }
                    }
                }
            }
        }
    }
}