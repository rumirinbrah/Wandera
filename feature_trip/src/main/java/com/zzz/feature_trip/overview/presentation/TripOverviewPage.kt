package com.zzz.feature_trip.overview.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.IconTextButton
import com.zzz.core.presentation.components.DotsLoadingAnimation
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.dialogs.ConfirmActionDialog
import com.zzz.core.presentation.events.ObserveAsEvents
import com.zzz.core.presentation.events.UIEvents
import com.zzz.core.presentation.headers.DateHeader
import com.zzz.core.presentation.toast.WanderaToast
import com.zzz.core.presentation.toast.WanderaToastState
import com.zzz.core.theme.redToastSweep
import com.zzz.data.trip.model.Day
import com.zzz.feature_trip.overview.presentation.components.BookLikeTextField
import com.zzz.feature_trip.overview.presentation.components.ItineraryLayoutOptions
import com.zzz.feature_trip.overview.presentation.components.ItineraryList
import com.zzz.feature_trip.overview.presentation.components.ItineraryPager
import com.zzz.feature_trip.overview.presentation.components.OverviewDocumentCard
import com.zzz.feature_trip.overview.presentation.components.OverviewTopBar
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewActions
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewState
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun TripOverviewRoot(
    modifier: Modifier = Modifier ,
    overviewViewModel: OverviewViewModel = koinViewModel() ,
    wanderaToastState: WanderaToastState,
    navigateToDayDetails: () -> Unit ,
    navigateToEditTrip: (tripId: Long) -> Unit ,
    navigateUp: () -> Unit ,
) {
    val state by overviewViewModel.overviewState.collectAsStateWithLifecycle()
    val events = overviewViewModel.events
    val days by overviewViewModel.days.collectAsStateWithLifecycle()

    TripOverviewPage(
        modifier ,
        state = state ,
        events = events,
        days = days,
        wanderaToastState,
        onAction = { action ->
            overviewViewModel.onAction(action)
        } ,
        navigateToDayDetails = navigateToDayDetails ,
        navigateToEditTrip = navigateToEditTrip ,
        navigateUp = navigateUp
    )
}

@Composable
private fun TripOverviewPage(
    modifier: Modifier = Modifier ,
    state: OverviewState ,
    events : Flow<UIEvents>,
    days: List<Day> ,
    wanderaToastState: WanderaToastState,
    onAction: (OverviewActions) -> Unit ,
    navigateToDayDetails: () -> Unit ,
    navigateToEditTrip: (tripId: Long) -> Unit ,
    navigateUp: () -> Unit ,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val columnScrollState = rememberScrollState()

    val pagerState = rememberPagerState() {
        days.size
    }
    var deleteDialog by remember { mutableStateOf(false) }

    ObserveAsEvents(events) {event->
        when(event){
            is UIEvents.Error -> {
                wanderaToastState.showToast(event.errorMsg, redToastSweep)
            }
            is UIEvents.SuccessWithMsg -> {
                //Toast.makeText(context , event.msg , Toast.LENGTH_SHORT).show()
                wanderaToastState.showToast(event.msg)
            }
            else->{}
        }
    }

    //scroll to the bottom in order to avoid textfield keyboard overlap
    LaunchedEffect(state.expenseNote) {
        if(state.expenseNote.endsWith("\n")){

            //delay since the notes box animates its content with 500L animation spec + 100L delay
            delay(650)
            columnScrollState.animateScrollTo(
                columnScrollState.maxValue
            )
        }
    }

    BackHandler {
        onAction(OverviewActions.CleanUpResources)
        navigateUp()
    }

    Box(
        modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        if (state.loading) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center) ,
                contentAlignment = Alignment.Center
            ) {
                DotsLoadingAnimation()
            }
        }
        else {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(columnScrollState)
                    .padding(16.dp) ,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OverviewTopBar(
                    tripName = state.trip?.tripName ?: "null" ,
                    onBack = {
                        onAction(OverviewActions.CleanUpResources)
                        navigateUp()
                    } ,
                    editTrip = {
                        state.trip?.id?.let {
                            navigateToEditTrip(it)
                        }
                    }
                )

                //date
                VerticalSpace()
                DateHeader(
                    startDate = state.trip?.startDate ?: 0 ,
                    endDate = state.trip?.endDate ?: 0
                )

                //Itinerary layout
                VerticalSpace()
                ItineraryLayoutOptions(
                    isPagerLayout = state.itineraryPagerLayout ,
                    onLayoutChange = {
                        onAction(OverviewActions.ChangeItineraryLayout)
                    }
                )
                AnimatedContent(
                    state.itineraryPagerLayout ,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    }
                ) { pagerLayout ->
                    if (pagerLayout) {
                        ItineraryPager(
                            pagerState = pagerState ,
                            days = days ,
                            onDayClick = {
                                onAction(OverviewActions.UpdateSelectedDay(it))
                                navigateToDayDetails()
                            }
                        )
                    } else {
                        ItineraryList(
                            days ,
                            onClick = {
                                onAction(OverviewActions.UpdateSelectedDay(it))
                                navigateToDayDetails()
                            } ,
                            markDayStatus = { isDone , dayId ->
                                //println("new status for $dayId is $isDone")
                                onAction(OverviewActions.UpdateDayStatus(dayId , isDone))
                            }
                        )
                    }
                }

                VerticalSpace(10.dp)
                //docs
                if (state.docs.isNotEmpty()) {
                    Text(
                        "Your documents" ,
                        fontSize = 16.sp ,
                        fontWeight = FontWeight.Bold ,
                    )
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp) ,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            state.docs ,
                            key = {
                                it.id
                            }
                        ) { doc ->
                            OverviewDocumentCard(
                                doc
                            )
                        }
                    }
                }

                //expense notes
                VerticalSpace(10.dp)
                Text(
                    "Expenses",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                BookLikeTextField(
                    value = state.expenseNote,
                    onValueChange = {
                        onAction(OverviewActions.OnExpenseNoteValueChange(it))
                    },
                    onSave = {
                        onAction(OverviewActions.UpdateExpenseNote)
                    }
                )


                VerticalSpace(30.dp)

                //delete
                IconTextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally) ,
                    icon = com.zzz.core.R.drawable.delete ,
                    text = "Delete Trip" ,
                    shape = MaterialTheme.shapes.large ,
                    background = MaterialTheme.colorScheme.errorContainer ,
                    onBackground = MaterialTheme.colorScheme.onErrorContainer ,
                    onClick = {
                        deleteDialog = true
                    }
                )
                VerticalSpace(20.dp)
            }
        } // COL-END
        if (deleteDialog) {
            ConfirmActionDialog(
                title = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontSize = 18.sp
                        )
                    ) {
                        append("Are you sure you want to delete ")
                    }
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold ,
                            fontSize = 18.sp
                        )
                    ) {
                        append(state.trip?.tripName)
                    }
                    withStyle(
                        SpanStyle(
                            fontSize = 18.sp
                        )
                    ) {
                        append("? This cannot be UNDONE!")
                    }
                } ,
                actionText = "Delete" ,
                onConfirm = {
                    deleteDialog = false
                    navigateUp()
                    onAction(OverviewActions.DeleteTrip)
                } ,
                onCancel = {
                    deleteDialog = false
                }
            )
        }

        WanderaToast(
            state = wanderaToastState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

    }


}