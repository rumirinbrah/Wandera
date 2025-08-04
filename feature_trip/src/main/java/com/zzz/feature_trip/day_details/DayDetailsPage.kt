package com.zzz.feature_trip.day_details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.components.ImageComponentWithDefaultImage
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.DateText
import com.zzz.core.presentation.modifiers.baldyShape
import com.zzz.core.theme.WanderaTheme
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.feature_trip.day_details.components.DayDetailsTodoItem
import com.zzz.feature_trip.day_details.viewmodel.DayDetailAction
import com.zzz.feature_trip.day_details.viewmodel.DayDetailsViewModel
import com.zzz.feature_trip.overview.presentation.components.DayTitleCard
import org.koin.androidx.compose.koinViewModel

/**
 * @param viewOnly Used to hide action buttons when needed
 */
@Composable
fun DayDetailsRoot(
    modifier: Modifier = Modifier ,
    dayId : Long,
    navigateUp: () -> Unit ,
    dayDetailsViewModel: DayDetailsViewModel = koinViewModel() ,
    viewOnly : Boolean = false ,
) {
    val state by dayDetailsViewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        dayDetailsViewModel.onAction(
            DayDetailAction.FetchDayDetails(dayId , !viewOnly)
        )
    }

    DayDetailsPage(
        modifier ,
        day = state.day ,
        todoLocations = state.todos,
        navigateUp = {
            navigateUp()
        },
        onAction = {action->
            dayDetailsViewModel.onAction(action)
        },
        viewOnly = viewOnly
    )
}

@Composable
private fun DayDetailsPage(
    modifier: Modifier = Modifier ,
    day: Day? ,
    todoLocations : List<TodoLocation> = emptyList() ,
    navigateUp: () -> Unit ,
    onAction : (DayDetailAction)->Unit ,
    viewOnly : Boolean = false ,
) {
    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground

    val density = LocalDensity.current
    val localConfig = LocalConfiguration.current
    val screenHeight = remember {
        localConfig.screenHeightDp.dp
    }
    val columnScrollState = rememberScrollState()

    val todos = remember(todoLocations) {
        todoLocations.filter {
            it.isTodo
        }
    }
    val locations = remember(todoLocations) {
        todoLocations.filter {
            !it.isTodo
        }
    }


    BackHandler {
        navigateUp()
    }
    Column(
        modifier
            .fillMaxSize()
            .background(
                background
            )

            .verticalScroll(columnScrollState)
    ) {
        if (day == null) {
            Text(
                "Oops! Something went wrong..." ,
                modifier = Modifier.align(Alignment.CenterHorizontally) ,
                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
            )
        } else {
            Box(
                Modifier.height(screenHeight / 2)
            ) {

                //image, title, nav button
                ImageComponentWithDefaultImage(
                    title = day.locationName ,
                    imageUri = day.image ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .drawBehind {
                            drawRect(
                                brush = Brush.verticalGradient(
                                    listOf(background.copy(0.5f) , Color.Transparent)
                                )
                            )
                        }
                        .align(Alignment.TopCenter)
                )
                CircularIconButton(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                        .align(Alignment.TopStart) ,
                    icon = com.zzz.core.R.drawable.arrow_back ,
                    contentDescription = "Go back" ,
                    background = Color.DarkGray.copy(0.5f) ,
                    onBackground = Color.White ,
                    onClick = {
                        navigateUp()
                    } ,
                )
                DayTitleCard(
                    day.locationName ,
                    modifier = Modifier.align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                )
            }

            Box(
                Modifier.offset(y = (-40).dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val outerBox =
                                baldyShape().createOutline(size , layoutDirection , density)
                            val innerBox =
                                baldyShape(30.dp).createOutline(size , layoutDirection , density)

                            drawOutline(
                                outerBox ,
                                onBackground ,
                                alpha = 0.4f
                            )
                            drawOutline(innerBox , background)
                        }
                        .padding(horizontal = 16.dp) ,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
//                    Spacer(Modifier.fillMaxHeight(0.1f))
                    VerticalSpace(60.dp)

                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp) ,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        item {
                            Text(
                                "TODOs" ,
                                fontSize = 20.sp ,
                                fontWeight = FontWeight.Bold ,
                            )

                            if (todos.isEmpty()) {
                                Text(
                                    "Edit your trips and add some!" ,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                        }
                        items(
                            todos ,
                            key = { it.id }
                        ) { todo ->
                            DayDetailsTodoItem(
                                todo = todo ,
                                onCheck = { id , done ->
                                    onAction(DayDetailAction.MarkTodoAsDone(id , done))
                                } ,
                                modifier = Modifier.animateItem() ,
                                isViewOnly = viewOnly
                            )

                        }
                        item {
                            VerticalSpace()
                            Text(
                                "Places to visit" ,
                                fontSize = 20.sp ,
                                fontWeight = FontWeight.Bold ,
                            )
                            if (locations.isEmpty()) {
                                Text(
                                    "Looks like you haven't added anything" ,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }


                        }
                        items(
                            locations ,
                            key = { it.id }
                        ) { todo ->
                            DayDetailsTodoItem(
                                todo = todo ,
                                onCheck = { id , done ->
                                    onAction(DayDetailAction.MarkTodoAsDone(id , done))
                                } ,
                                modifier = Modifier.animateItem() ,
                                isViewOnly = viewOnly
                            )
                        }
                        item {
                            VerticalSpace()
                        }
                    }

                    DateText(
                        Modifier.align(Alignment.CenterHorizontally)
                    )
                    VerticalSpace()

                }
            }
        }
    }
}


@Preview
@Composable
fun DayDetailsPrev(
    modifier: Modifier = Modifier
) {
    WanderaTheme {

    }
}