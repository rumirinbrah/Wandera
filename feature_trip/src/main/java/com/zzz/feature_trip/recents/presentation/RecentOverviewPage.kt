package com.zzz.feature_trip.recents.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.pager.HorizontalPager
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.IconTextButton
import com.zzz.core.presentation.components.DotsLoadingAnimation
import com.zzz.core.presentation.components.KeyboardAware
import com.zzz.core.presentation.components.SheetState
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.components.rememberWanderaSheetState
import com.zzz.core.presentation.dialogs.ConfirmActionDialog
import com.zzz.core.presentation.events.ObserveAsEvents
import com.zzz.core.presentation.headers.DateHeader
import com.zzz.core.presentation.modifiers.customShadow
import com.zzz.core.presentation.toast.WanderaToast
import com.zzz.core.presentation.toast.WanderaToastState
import com.zzz.core.theme.redToastSweep
import com.zzz.data.trip.model.Day
import com.zzz.feature_trip.overview.presentation.components.BookLikeTextField
import com.zzz.feature_trip.overview.presentation.components.ChecklistHeader
import com.zzz.feature_trip.overview.presentation.components.ChecklistItemRoot
import com.zzz.feature_trip.overview.presentation.components.ItineraryLayoutOptions
import com.zzz.feature_trip.overview.presentation.components.ItineraryList
import com.zzz.feature_trip.overview.presentation.components.ItineraryPager
import com.zzz.feature_trip.overview.presentation.components.MarkedAsDoneAnimation
import com.zzz.feature_trip.overview.presentation.components.OverviewDocumentCard
import com.zzz.feature_trip.overview.presentation.components.OverviewPageFab
import com.zzz.feature_trip.overview.presentation.components.OverviewTopBar
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.NoteExpenseTabRow
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pager.expense_tracker.AddExpenseSheet
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pager.expense_tracker.ExpensePage
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewActions
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewEvents
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewState
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecentOverviewRoot(
    modifier: Modifier = Modifier ,
    overviewViewModel: OverviewViewModel = koinViewModel() ,
    navigateToDayDetails: (dayId : Long) -> Unit ,
    shareTrip: (tripId: Long) -> Unit ,
    navigateUp: () -> Unit ,
) {
    val state by overviewViewModel.overviewState.collectAsStateWithLifecycle()
    val events = overviewViewModel.events
    val days by overviewViewModel.days.collectAsStateWithLifecycle()

    RecentTripOverviewPage(
        modifier ,
        state = state ,
        events = events ,
        days = days ,
        onAction = { action ->
            overviewViewModel.onAction(action)
        } ,
        navigateToDayDetails = navigateToDayDetails ,
        shareTrip = shareTrip ,
        navigateUp = navigateUp
    )
}

/**
 * @param events One time UI events
 * @param navigateToDayDetails View day details like todos, locations
 * @param shareTrip
 */
@Composable
private fun RecentTripOverviewPage(
    modifier: Modifier = Modifier ,
    state: OverviewState ,
    events: Flow<OverviewEvents> ,
    days: List<Day> ,
    onAction: (OverviewActions) -> Unit ,
    navigateToDayDetails: (dayId : Long) -> Unit ,
    shareTrip: (tripId: Long) -> Unit = {} ,
    navigateUp: () -> Unit ,
) {

    val scope = rememberCoroutineScope()

    val columnScrollState = rememberScrollState()

    val pagerState = rememberPagerState() {
        days.size
    }
    val notesExpensePagerState = rememberPagerState { 2 }

    var deleteDialog by remember { mutableStateOf(false) }

    ObserveAsEvents(events) { event ->
        when (event) {
            OverviewEvents.NavigateUp ->{
                navigateUp()
            }
            else -> {}
        }
    }

    BackHandler {
        when {
            !state.fabCollapsed -> {
                onAction(OverviewActions.OnFabCollapse(true))
            }

            else -> {
                onAction(OverviewActions.CleanUpResources)
                navigateUp()
            }
        }
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
        } else {
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

                    }
                )

                //date
                VerticalSpace()
                DateHeader(
                    startDate = state.trip?.startDate ?: 0 ,
                    endDate = state.trip?.endDate ?: 0 ,
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
                                //onAction(OverviewActions.UpdateSelectedDay(it))
                                navigateToDayDetails(it.id)
                            }
                        )
                    } else {
                        ItineraryList(
                            days ,
                            onClick = {
                                //onAction(OverviewActions.UpdateSelectedDay(it))
                                navigateToDayDetails(it.id)
                            } ,
                            markDayStatus = { isDone , dayId ->
                                //println("new status for $dayId is $isDone")
                                onAction(OverviewActions.UpdateDayStatus(dayId , isDone))
                            },
                            viewOnly = true
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

                //checklist
                VerticalSpace(10.dp)
                Column {

                    ChecklistHeader(
                        collapsed = state.checklistCollapsed ,
                        onCollapse = {
                            onAction(OverviewActions.OnChecklistCollapse)
                        }
                    )
                    VerticalSpace(8.dp)

                    AnimatedVisibility(!state.checklistCollapsed) {
                        if (state.checklist.isEmpty()) {
                            Text(
                                "Seems like you haven't created a checklist..." ,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                            )

                        }

                        LazyColumn(
                            Modifier
                                .fillMaxWidth()
                                .heightIn(max = 500.dp) ,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = state.checklist ,
                                key = { it.id }
                            ) { item ->
                                ChecklistItemRoot(
                                    item = item ,
                                    onCheck = { itemId , checked ->
                                        onAction(
                                            OverviewActions.CheckChecklistItem(
                                                itemId ,
                                                checked
                                            )
                                        )
                                    } ,
                                    onDelete = { itemId ->
                                        onAction(OverviewActions.DeleteChecklistItem(itemId))
                                    } ,
                                    modifier = Modifier.animateItem() ,
                                    trapeziumChecklist = state.trapeziumChecklist,
                                    interactionsEnabled = false
                                )

                            }
                        }
                    }


                }


                VerticalSpace(10.dp)
                NoteExpenseTabRow(
                    currentTab = notesExpensePagerState.currentPage ,
                    onTabChange = {
                        scope.launch {
                            notesExpensePagerState.animateScrollToPage(it)
                        }
                    }
                )
                VerticalSpace(10.dp)

                //--- Note ; Expense PAGER ---
                HorizontalPager(
                    state = notesExpensePagerState ,
                    verticalAlignment = Alignment.Top ,
                    modifier = Modifier
                        .animateContentSize() ,
                ) { page ->
                    when (page) {
                        0 -> {
                            Column(
                                Modifier.padding(4.dp)
                            ) {
                                KeyboardAware {
                                    BookLikeTextField(
                                        modifier = Modifier
                                            .customShadow(
                                                MaterialTheme.colorScheme.onBackground ,
                                                borderRadius = 0.dp
                                            ) ,
                                        value = state.expenseNote ,
                                        onValueChange = {} ,
                                        onSave = {},
                                        interactionsEnabled = false
                                    )
                                }
                                VerticalSpace(10.dp)
                            }

                        }

                        1 -> {
                            ExpensePage(
                                totalExpense = state.totalExpense ,
                                expenses = state.expenses ,
                                groupedExpenses = state.groupedExpenses ,
                                launchAddExpenseSheet = {} ,
                                onExpenseItemClick = { },
                                interactionsEnabled = false,
                                viewOnly = true
                            )
                        }
                    }
                }


                VerticalSpace(30.dp)

                //delete
                IconTextButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .customShadow(
                            MaterialTheme.colorScheme.surfaceContainer ,
                            alpha = 0.8f ,
                        ) ,
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

        //------ OVERLAYS ------
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

        AnimatedVisibility(
            state.playMarkAsDoneAnimation,
            enter = fadeIn() ,
            exit = fadeOut()
        ){
            MarkedAsDoneAnimation(
                onAnimationFinish = {
                    navigateUp()
                }
            )
        }

    }


}