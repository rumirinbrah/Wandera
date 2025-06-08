package com.zzz.feature_trip.create.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.components.ImageComponent
import com.zzz.core.theme.WanderaTheme
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
        dayState,//,
        navigateUp = {
            navigateUp()
            createViewModel.onAction(CreateAction.DayActions.OnDiscardCreation)
        }
    )
}

@Composable
private fun DayDetailsPage(
    dayState: DayState ,
    navigateUp: () -> Unit ,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val background = MaterialTheme.colorScheme.background


    BackHandler {
        navigateUp()
    }
    Column(
        modifier
            .fillMaxSize()
            .background(background)
    ) {
        //image, title, nav button
        Box() {


            ImageComponent(
                title = dayState.dayTitle,
                imageUri = dayState.image,
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(0.45f)
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
                background = Color.DarkGray.copy(0.5f) ,
                onClick = {
                    //navigateUp()
                } ,
            )
        }
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Box(
                Modifier
                    .offset(y = -(50.dp))
            ){
                Box(
                    Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawArc(
                                background ,
                                startAngle = 0f ,
                                sweepAngle = -180f ,
                                useCenter = true ,
                                size = Size(width = size.width , height = 100.dp.toPx())
                            )
                        }
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) ,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //Spacer(Modifier.fillMaxHeight(0.1f))
                Text(
                    "Places to visit/TODOs" ,
                    fontSize = 18.sp ,
                    fontWeight = FontWeight.Bold ,
                )
                LazyColumn(
                    Modifier.fillMaxWidth() ,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
//                    items(
//                        50
//                    ){
//                        TodoLocationItem(
//                            TodoLocation(title = "HEHEHEHEH") ,
//                            modifier = Modifier ,
//                            onDeleteTodo = {
//                            } ,
//                            isViewOnly = true
//                        )
//                    }
                    items(dayState.todoLocations) { todo ->
                        TodoLocationItem(
                            todo ,
                            modifier = Modifier ,
                            onDeleteTodo = {
                            } ,
                            isViewOnly = true
                        )
                    }
                }
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
        //}
    }


}
/*

 */

@Preview
@Composable
fun DayDetailsPrev(
    modifier: Modifier = Modifier
) {
    WanderaTheme {
        DayDetailsPage(
            dayState = DayState(
                dayTitle = "Dolomites" ,
            ) ,
            navigateUp = {}
        )
    }
}