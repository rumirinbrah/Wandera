package com.zzz.feature_trip.overview.presentation

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.IconTextButton
import com.zzz.core.presentation.components.DotsLoadingAnimation
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
import com.zzz.feature_trip.overview.presentation.components.ExpandableBookLikeTextField
import com.zzz.feature_trip.overview.presentation.components.ItineraryLayoutOptions
import com.zzz.feature_trip.overview.presentation.components.ItineraryList
import com.zzz.feature_trip.overview.presentation.components.ItineraryPager
import com.zzz.feature_trip.overview.presentation.components.MarkedAsDoneRoot
import com.zzz.feature_trip.overview.presentation.components.OverviewPageFab
import com.zzz.feature_trip.overview.presentation.components.OverviewTopBar
import com.zzz.feature_trip.overview.presentation.components.UserDocsList
import com.zzz.feature_trip.overview.presentation.components.checklist.ChecklistSection
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.NoteExpenseTabRow
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.AddExpenseSheet
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.ExpensePage
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewActions
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewEvents
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewState
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TripOverviewRoot(
    modifier: Modifier = Modifier ,
    overviewViewModel: OverviewViewModel = koinViewModel() ,
    wanderaToastState: WanderaToastState ,
    navigateToDayDetails: (dayId : Long) -> Unit ,
    navToEditTrip: (tripId: Long) -> Unit ,
    shareTrip: (tripId: Long) -> Unit ,
    navigateUp: () -> Unit ,
) {
    val state by overviewViewModel.overviewState.collectAsStateWithLifecycle()
    val events = overviewViewModel.events
    val days by overviewViewModel.days.collectAsStateWithLifecycle()


    TripOverviewPage(
        modifier ,
        state = state ,
        events = events ,
        days = days ,
        wanderaToastState ,
        onAction = { action ->
            overviewViewModel.onAction(action)
        } ,
        navigateToDayDetails = navigateToDayDetails ,
        navToEditTrip = navToEditTrip ,
        shareTrip = shareTrip ,
        navigateUp = navigateUp
    )
}

/**
 * @param events One time UI events
 * @param wanderaToastState Can be used to show custom toasts
 * @param navigateToDayDetails View day details like todos, locations
 * @param navToEditTrip Edit trip details
 * @param shareTrip Navigate to the ExportPage with tripId
 */
@Composable
private fun TripOverviewPage(
    modifier: Modifier = Modifier ,
    state: OverviewState ,
    events: Flow<OverviewEvents> ,
    days: List<Day> ,
    wanderaToastState: WanderaToastState ,
    onAction: (OverviewActions) -> Unit ,
    navigateToDayDetails: (dayId : Long) -> Unit ,
    navToEditTrip: (tripId: Long) -> Unit ,
    shareTrip: (tripId: Long) -> Unit = {} ,
    navigateUp: () -> Unit ,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val columnScrollState = rememberScrollState()

    val notesExpensePagerState = rememberPagerState { 2 }

    val wanderaSheetState = rememberWanderaSheetState(
        SheetState.CLOSED
    )

    var deleteDialog by remember { mutableStateOf(false) }

    ObserveAsEvents(events) { event ->
        when (event) {
            is OverviewEvents.Error -> {
                wanderaToastState.showToast(event.errorMsg , redToastSweep)
            }

            is OverviewEvents.SuccessWithMsg -> {
                //Toast.makeText(context , event.msg , Toast.LENGTH_SHORT).show()
                wanderaToastState.showToast(event.msg)
            }
            is OverviewEvents.ShareExpenseIntent->{
                context.startActivity(event.intent)
            }

            OverviewEvents.NavigateUp ->{
                navigateUp()
            }

            else -> {}
        }
    }

    //scroll to the bottom in order to avoid textfield keyboard overlap
    LaunchedEffect(state.expenseNote) {
        if (state.expenseNote.endsWith("\n")) {

            //delay since the notes box animates its content with 500L animation spec + 100L delay
            delay(650)
            columnScrollState.animateScrollTo(
                columnScrollState.maxValue
            )
        }
    }


    BackHandler {
        when {
            !state.fabCollapsed -> {
                onAction(OverviewActions.OnFabCollapse(true))
            }

//            wanderaSheetState.visible -> {
//                scope.launch {
//                    wanderaSheetState.animateTo(SheetState.CLOSED)
//                }
//            }
            wanderaToastState.visible->{
                wanderaToastState.dismiss()
                onAction(OverviewActions.CleanUpResources)
                navigateUp()
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
                    .verticalScroll(columnScrollState, enabled = !wanderaSheetState.visible)
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
                            days = days ,
                            onDayClick = {
                                //onAction(OverviewActions.UpdateSelectedDay(it,false))
                                navigateToDayDetails(it.id)
                            },
                            interactionsEnabled = !wanderaSheetState.visible
                        )
                    } else {
                        ItineraryList(
                            days ,
                            onClick = {
                                //onAction(OverviewActions.UpdateSelectedDay(it,false))
                                navigateToDayDetails(it.id)
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
                    /*
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

                     */
                    UserDocsList(
                        documents = state.docs,
                        collapsed = state.docsListCollapsed,
                        onCollapseChange = {
                            onAction(OverviewActions.OnDocsListCollapse)
                        },
                        //modifier = Modifier.background(Color.Gray)
                    )
                }

                //checklist
                //VerticalSpace(10.dp)
                ChecklistSection(
                    checklist = state.checklist,
                    checklistProgress = state.checklistProgress,
                    onCheck = {id,checked->
                        onAction(
                            OverviewActions.CheckChecklistItem(
                                id,
                                checked
                            )
                        )
                    },
                    onDelete = {id->
                        onAction(OverviewActions.DeleteChecklistItem(id))
                    },
                    collapsed = state.checklistCollapsed,
                    onCollapse = {
                        onAction(OverviewActions.OnChecklistCollapse)
                    },
                    trapeziumChecklist = state.trapeziumChecklist
                )
                /*
                Column {

                    ChecklistHeader(
                        collapsed = state.checklistCollapsed ,
                        onCollapse = {
                            onAction(OverviewActions.OnChecklistCollapse)
                        }
                    )

                    VerticalSpace(8.dp)

                    AnimatedVisibility(!state.checklistCollapsed) {

                        when{
                            state.checklist.isEmpty()->{
                                Text(
                                    "Seems like you haven't created a checklist..." ,
                                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                                )
                            }
                            else->{
                                Column {
                                    LineProgressBar(
                                        modifier = Modifier.fillMaxWidth(),
                                        progress = state.checklistProgress,
                                    )
                                    VerticalSpace(10.dp)
                                    LazyColumn(
                                        Modifier
                                            .fillMaxWidth()
                                            .heightIn(max = 400.dp) ,
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
                                                trapeziumChecklist = state.trapeziumChecklist
                                            )

                                        }
                                    }
                                }

                            }
                        }


                    }


                }

                 */


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
                            ExpandableBookLikeTextField(
                                modifier = Modifier.padding(4.dp) ,
                                value = state.expenseNote ,
                                onValueChange = {
                                    onAction(OverviewActions.OnExpenseNoteValueChange(it))
                                } ,
                                onSave = {
                                    onAction(OverviewActions.UpdateExpenseNote)
                                }
                                )
                            /*
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
                                        onValueChange = {
                                            onAction(OverviewActions.OnExpenseNoteValueChange(it))
                                        } ,
                                        onSave = {
                                            onAction(OverviewActions.UpdateExpenseNote)
                                        }
                                    )
                                }
                                VerticalSpace(10.dp)
                            }

                             */

                        }

                        1 -> {
                            ExpensePage(
                                totalExpense = state.totalExpense ,
                                expenses = state.expenses ,
                                groupedExpenses = state.groupedExpenses ,
                                launchAddExpenseSheet = {
                                    wanderaSheetState.show()
                                } ,
                                onExpenseItemClick = { itemId: Long ->
                                    onAction(OverviewActions.SelectExpenseItem(itemId))
                                    wanderaSheetState.show()
                                },
                                shareExpenses = {
                                    onAction(OverviewActions.ShareTripExpenses(context))
                                },
                                interactionsEnabled = !wanderaSheetState.visible
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

        OverviewPageFab(
            collapsed = state.fabCollapsed ,
            onCollapse = { collapsed ->
                onAction(OverviewActions.OnFabCollapse(collapsed))
            } ,
            onEdit = {
                onAction(OverviewActions.CleanUpResources)

                state.trip?.id?.let {
                    navToEditTrip(it)
                }

            } ,
            onShare = {
                state.trip?.id?.let {
                    shareTrip(it)
                }
            } ,
            onMarkAsDone = {
                onAction(OverviewActions.OnFabCollapse(true))
                onAction(OverviewActions.MarkTripAsDone(true))
            } ,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp , bottom = 8.dp) ,

            )
        when {
            wanderaSheetState.visible -> {
                AddExpenseSheet(
                    tripId = state.trip?.id ,
                    itemId = state.selectedExpenseId ,
                    sheetState = wanderaSheetState ,
                    onClosed = {
                        onAction(OverviewActions.SelectExpenseItem(null))
                    },
                )
            }
        }

        WanderaToast(
            state = wanderaToastState ,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        AnimatedVisibility(
            state.playMarkAsDoneAnimation,
            enter = fadeIn(),
            exit = fadeOut()
        ){
            MarkedAsDoneRoot(
                onAnimationFinish = {
                    onAction(OverviewActions.CleanUpResources)
                    navigateUp()
                }
            )
        }

    }


}

@Preview
@Composable
private fun TopBarPrev() {
    MaterialTheme {

    }
}
