package com.zzz.feature_trip.create.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.theme.WanderaTheme
import com.zzz.data.trip.model.TodoLocation
import com.zzz.feature_trip.R
import com.zzz.feature_trip.create.presentation.components.DayTitleCard
import com.zzz.feature_trip.create.presentation.components.TodoLocationItem
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.presentation.states.DayState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DayDetailsRoot(
    navigateUp: () -> Unit ,
    createViewModel: CreateViewModel = koinViewModel()
) {
    val dayState by createViewModel.dayState.collectAsStateWithLifecycle()
    DayDetailsPage(
        DayState(dayTitle = "Dolomites") ,
        navigateUp = {
            navigateUp()
            createViewModel.onAction(CreateAction.OnDiscard)
        }
    )
}

@Composable
fun DayDetailsPage(
    dayState: DayState ,
    navigateUp: () -> Unit ,
    modifier: Modifier = Modifier
) {
    val background = MaterialTheme.colorScheme.background
    BackHandler {
        navigateUp()
    }
    Column(
        modifier

            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        //image, title, nav button
        Box() {


            Image(
                painter = painterResource(com.zzz.core.R.drawable.test_trees) ,
                contentDescription = "" ,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f) ,
                contentScale = ContentScale.Crop
            )
            DayTitleCard(
                dayState.dayTitle ,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            CircularIconButton(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.TopStart) ,
                icon = com.zzz.core.R.drawable.arrow_back ,
                contentDescription = "Go back" ,
                background = Color.DarkGray.copy(0.4f) ,
                onClick = {
                    //navigateUp()
                } ,
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .offset(y = -(50.dp))
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .drawBehind {
                        drawArc(
                            background ,
                            startAngle = 0f ,
                            sweepAngle = -180f ,
                            useCenter = true ,
                            size = Size(width = size.width , height = size.height)
                        )
                    }
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp) ,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(Modifier.fillMaxHeight(0.1f))
                Text(
                    "Places to visit/TODOs" ,
                    fontSize = 18.sp ,
                    fontWeight = FontWeight.Bold ,
                )
                LazyColumn(
                    Modifier.fillMaxWidth()
                        .fillMaxHeight() ,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        50
                    ){
                        TodoLocationItem(
                            TodoLocation(title = "HEHEHEHEH") ,
                            modifier = Modifier ,
                            onDeleteTodo = {
                            } ,
                            isViewOnly = true
                        )
                    }
//                    items(dayState.todoLocations) { todo ->
//                        TodoLocationItem(
//                            todo ,
//                            modifier = Modifier ,
//                            onDeleteTodo = {
//                            } ,
//                            isViewOnly = true
//                        )
//                    }
                }
            }

            /*
            Column(
                Modifier.fillMaxWidth() ,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                dayState.todoLocations.onEach { todo ->
                    TodoLocationItem(
                        todo ,
                        modifier = Modifier ,
                        onDeleteTodo = {
                        } ,
                        isViewOnly = true
                    )
                }
            }

             */
        }
    }


}

@Preview
@Composable
fun DayDetailsPrev(
    modifier: Modifier = Modifier
) {
    WanderaTheme {
        DayDetailsPage(
            dayState = DayState(
                dayTitle = "Dolomites" ,
                dayNo = 1
            ) ,
            navigateUp = {}
        )
    }
}