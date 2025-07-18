package com.zzz.feature_translate.presentation.tab_download.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.theme.WanderaTheme
import com.zzz.core.theme.lightOnBackground
import com.zzz.core.theme.lightPrimary
import com.zzz.core.theme.lightSecondary
import com.zzz.core.theme.lightSurface
import com.zzz.core.theme.lightSurfaceContainer
import com.zzz.feature_translate.R
import kotlinx.coroutines.launch

/**
 * Instruction about the download feature. Has hardcoded values for light scheme
 * @param onFinish Called when user done reading all
 */
@Composable
fun FirstTimeInstructions(
    onFinish :()->Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState() { 2 }
    val scope = rememberCoroutineScope()

    val curveColor = lightSecondary
    val color1 = lightSurface
    val color2 = lightSurfaceContainer

    val backgroundBrush = remember {
        Brush.linearGradient(
            listOf(
                color2 ,
                color1
            )
        )
    }
    Column(
        modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.large)
            .fillMaxSize()
            .background(backgroundBrush)
            .drawWithContent {
                drawContent()
                val path = Path().apply {
                    val width = size.width
                    val height = size.height
                    moveTo(0f,height)
                    quadraticTo(
                        x1 = width/2,
                        y1 = height - 250f,
                        x2 = width,
                        y2 = height
                    )
                    close()
                }
                drawPath(
                    path,
                    color = curveColor
                )
            },
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.fillMaxSize()
        ) {index->
            when(index){
                0 ->{
                    Column(
                        Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Seems its your first time here--here's some information to get you started!",
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = lightOnBackground
                        )
                        VerticalSpace(40.dp)

                        Image(
                            painter = painterResource(R.drawable.downloaded_image) ,
                            contentDescription = "Illustration of a man looking at files",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.width(200.dp)
                                .aspectRatio(1f)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            "You can download over 50 translation models & easily use them without network!",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = lightOnBackground
                        )

                        TextButton(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                            },
                        ) {
                            Text("Next" , color = lightPrimary)
                        }
                    }

                }
                1->{
                    Column(
                        Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Need many languages? Your phone might feel the weight!",
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            color = lightOnBackground
                        )
                        VerticalSpace(40.dp)

                        Text(
                            text = buildAnnotatedString {
                                pushStyle(
                                    SpanStyle(color = lightOnBackground)
                                )
                                append("The models you download are stored on your own device, and they may take up to ")
                                withStyle(
                                    SpanStyle(
                                        fontWeight = FontWeight.Bold
                                    )
                                ){
                                    append("30-40 MB of space")
                                }
                                append(".Make sure to delete them when no longer needed")
                            } ,
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp
                        )
                        Image(
                            painter = painterResource(R.drawable.trash_image),
                            contentDescription = "Trash can",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(200.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        TextButton(
                            onClick = {
                                onFinish()
                            }
                        ) {
                            Text("Start translating" , color = lightPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FirstTimePrev() {
    WanderaTheme {
        FirstTimeInstructions(onFinish = {})
    }
}