package com.zzz.core.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Animation
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Components to be used for testing
 */
@Composable
fun TestLazyColumn(
    modifier: Modifier = Modifier,
    items : Int = 50,
) {


    Column(
        modifier.fillMaxSize()
    ) {

        LazyColumn(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items){
                Text(
                    "HEEEEEE" ,
                    fontSize = 25.sp ,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}

@Composable
fun TestProgBar(
    modifier: Modifier = Modifier
) {
    var total = remember {
        mutableLongStateOf(0L)
    }
    val progress = remember {
        Animatable(
            initialValue = 0f
        )
    }
    LaunchedEffect(Unit) {
        println("HERE")
        while (total.longValue<10){
            println("LOOP")
            total.longValue++
            progress.animateTo(total.longValue/10f)
            delay(1000)
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        Row(
            Modifier.fillMaxWidth()
                .height(70.dp)
                .drawBehind{
                    drawRect(
                        color = Color.White,
                        size = Size(
                            height = size.height,
                            width = size.width * progress.value
                        )
                    )
                }
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text("HIIII THERE")
            }
        }

    }
}



