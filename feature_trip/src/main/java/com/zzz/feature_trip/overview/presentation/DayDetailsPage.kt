package com.zzz.feature_trip.overview.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.components.ImageComponentWithDefaultBackground
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.modifiers.baldyShape
import com.zzz.core.theme.WanderaTheme
import com.zzz.data.trip.DayWithTodos
import com.zzz.feature_trip.create.presentation.components.TodoLocationItem
import com.zzz.feature_trip.overview.presentation.components.DayTitleCard
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewActions
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DayDetailsRoot(
    modifier: Modifier = Modifier ,
    navigateUp: () -> Unit ,
    overviewViewModel: OverviewViewModel = koinViewModel()
) {
    val state by overviewViewModel.overviewState.collectAsStateWithLifecycle()


    DayDetailsPage(
        modifier ,
        state.selectedDay ,
        navigateUp = {
            overviewViewModel.onAction(OverviewActions.ClearSelectedDay)
            navigateUp()
        }
    )
}

@Composable
private fun DayDetailsPage(
    modifier: Modifier = Modifier ,
    dayWithTodos: DayWithTodos? ,
    navigateUp: () -> Unit
) {
    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground

    val density = LocalDensity.current

    val day = remember {
        dayWithTodos?.day
    }
    val todos = remember {
        dayWithTodos?.todosAndLocations?.filter {
            it.isTodo
        }
    }
    val locations = remember {
        dayWithTodos?.todosAndLocations?.filter {
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
    ) {
        if (day == null) {
            Text(
                "Oops! Something went wrong..." ,
                modifier = Modifier.align(Alignment.CenterHorizontally) ,
                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
            )
        } else {
            Box {

                //image, title, nav button
                ImageComponentWithDefaultBackground(
                    title = day.locationName ,
                    imageUri = day.image ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.45f)
                )
                Box(
                    Modifier.fillMaxWidth()
                        .height(40.dp)
                        .drawBehind {
                            drawRect(
                                brush = Brush.verticalGradient(
                                    listOf(background.copy(0.5f), Color.Transparent)
                                )
                            )
                        }
                        .align(Alignment.TopCenter)
                )
                CircularIconButton(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(4.dp)
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
                )
            }

            Box(
                Modifier.offset(y = (-40).dp)
            ){
                Column(
                    Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val outerBox = baldyShape().createOutline(size, layoutDirection, density)
                            val innerBox = baldyShape(30.dp).createOutline(size, layoutDirection, density)

                            drawOutline(
                                outerBox ,
                                onBackground,
                                alpha = 0.4f
                            )
                            drawOutline(innerBox, background)
                        }
                        .padding(horizontal = 16.dp) ,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(Modifier.fillMaxHeight(0.1f))

                    LazyColumn(
                        Modifier.fillMaxWidth() ,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        item {
                            Text(
                                "TODOs" ,
                                fontSize = 18.sp ,
                                fontWeight = FontWeight.Bold ,
                            )
                        }
                        items(
                            todos!! ,
                            key = { it.id }
                        ) { todo ->
                            TodoLocationItem(
                                todo ,
                                modifier = Modifier ,
                                onDeleteTodo = {
                                } ,
                                isViewOnly = true
                            )
                        }
                        item {
                            VerticalSpace()
                            Text(
                                "Places to visit" ,
                                fontSize = 18.sp ,
                                fontWeight = FontWeight.Bold ,
                            )
                        }
                        items(
                            locations!! ,
                            key = { it.id }
                        ) { todo ->
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



        }


    }


}

@Composable
private fun DayDetailsPageRevamp(
    modifier: Modifier = Modifier ,
    dayWithTodos: DayWithTodos? ,
    navigateUp: () -> Unit
) {
    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground

    val density = LocalDensity.current

    val day = remember {
        dayWithTodos?.day
    }
    val todos = remember {
        dayWithTodos?.todosAndLocations?.filter {
            it.isTodo
        }
    }
    val locations = remember {
        dayWithTodos?.todosAndLocations?.filter {
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
    ) {
        if (day == null) {
            Text(
                "Oops! Something went wrong..." ,
                modifier = Modifier.align(Alignment.CenterHorizontally) ,
                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
            )
        } else {
            Box {

                //image, title, nav button
                ImageComponentWithDefaultBackground(
                    title = day.locationName ,
                    imageUri = day.image ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.45f)
                )
                Box(
                    Modifier.fillMaxWidth()
                        .height(40.dp)
                        .drawBehind {
                            drawRect(
                                brush = Brush.verticalGradient(
                                    listOf(background.copy(0.5f), Color.Transparent)
                                )
                            )
                        }
                        .align(Alignment.TopCenter)
                )
                CircularIconButton(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(4.dp)
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
                )
            }

            Box(
                Modifier.offset(y = (-40).dp)
            ){
                Column(
                    Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val outerBox = baldyShape().createOutline(size, layoutDirection, density)
                            val innerBox = baldyShape(30.dp).createOutline(size, layoutDirection, density)

                            drawOutline(
                                outerBox ,
                                onBackground,
                                alpha = 0.4f
                            )
                            drawOutline(innerBox, background)
                        }
                        .padding(horizontal = 16.dp) ,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(Modifier.fillMaxHeight(0.1f))

                    LazyColumn(
                        Modifier.fillMaxWidth() ,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        item {
                            Text(
                                "TODOs" ,
                                fontSize = 18.sp ,
                                fontWeight = FontWeight.Bold ,
                            )
                        }
                        items(
                            todos!! ,
                            key = { it.id }
                        ) { todo ->
                            TodoLocationItem(
                                todo ,
                                modifier = Modifier ,
                                onDeleteTodo = {
                                } ,
                                isViewOnly = true
                            )
                        }
                        item {
                            VerticalSpace()
                            Text(
                                "Places to visit" ,
                                fontSize = 18.sp ,
                                fontWeight = FontWeight.Bold ,
                            )
                        }
                        items(
                            locations!! ,
                            key = { it.id }
                        ) { todo ->
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