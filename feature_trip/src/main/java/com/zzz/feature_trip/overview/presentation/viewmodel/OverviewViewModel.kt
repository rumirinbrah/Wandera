package com.zzz.feature_trip.overview.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.domain.result.Result
import com.zzz.core.domain.result.map
import com.zzz.core.platform.files.WanderaFileManager
import com.zzz.core.platform.files.util.FileManagerError
import com.zzz.core.util.toLocalDate
import com.zzz.data.common.SettingsPreferences
import com.zzz.data.note.model.ChecklistEntity
import com.zzz.data.note.source.ChecklistSource
import com.zzz.data.note.source.ExpenseNoteSource
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.ExpenseEntity
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.ExpenseSource
import com.zzz.data.trip.source.TripSource
import com.zzz.data.trip.source.UserDocSource
import com.zzz.feature_trip.overview.data.local.ItineraryLayoutPref
import com.zzz.feature_trip.overview.domain.toUIEntity
import com.zzz.feature_trip.share.data.repo.ShareExpenseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class OverviewViewModel(
    private val tripSource: TripSource ,
    private val daySource: DaySource ,
    private val docSource: UserDocSource ,
    private val notesSource: ExpenseNoteSource ,
    private val checklistSource: ChecklistSource ,
    private val expenseSource: ExpenseSource ,
    context: Context
) : ViewModel() {

    private val layoutPref = ItineraryLayoutPref(context)
    private val settingsPref = SettingsPreferences(context)
    private var sessionOnGoing = false

    private val _overviewState = MutableStateFlow(OverviewState())
    internal val overviewState = _overviewState.asStateFlow()

    private val _days = MutableStateFlow<List<Day>>(emptyList())
    val days = _days.stateIn(
        viewModelScope ,
        SharingStarted.WhileSubscribed(5000L) ,
        _days.value
    )

    private val _events = Channel<OverviewEvents>()
    internal val events = _events.receiveAsFlow()

    private var collectDaysJob: Job? = null
    private var collectChecklistJob: Job? = null

    init {
        Log.d("overviewVm" , "Init....")
        _overviewState.update {
            it.copy(
                itineraryPagerLayout = layoutPref.isLayoutPager() ,
                trapeziumChecklist = settingsPref.isChecklistBoxTrapezium()
            )
        }
    }

    fun onAction(action: OverviewActions) {
        when (action) {
            is OverviewActions.FetchTripData -> {
                fetchTripData(action.tripId)
            }

            //------ day -----
            is OverviewActions.UpdateDayStatus -> {
                updateDayStatus(action.dayId , action.done)
            }

            // ------ LAYOUT ------
            OverviewActions.ChangeItineraryLayout -> {
                changeItineraryLayout()
            }

            OverviewActions.CleanUpResources -> cleanUpResources()


            //------ NOTE -----
            OverviewActions.UpdateExpenseNote -> {
                updateNote()
            }

            is OverviewActions.OnExpenseNoteValueChange -> {
                onNoteValueChange(action.value)
            }

            is OverviewActions.SelectExpenseItem -> {
                updateSelectedExpenseItem(action.itemId)
            }

            //------ Cheklist -----
            OverviewActions.OnDocsListCollapse -> {
                onDocsListCollapse()
            }

            //------ Cheklist -----
            is OverviewActions.CheckChecklistItem -> {
                checkChecklistItem(action.itemId , action.checked)
            }

            is OverviewActions.DeleteChecklistItem -> {
                deleteChecklistItem(action.itemId)
            }

            OverviewActions.OnChecklistCollapse -> {
                onChecklistCollapse()
            }

            is OverviewActions.ShareTripExpenses->{
                shareTripExpenses(context = action.context)
            }


            is OverviewActions.OnFabCollapse -> {
                onFabCollapse(action.collapsed)
            }

            is OverviewActions.PlayMarkAsDoneAnimation -> {
                playMarkAsDoneAnimation(action.playAnimation)
            }

            is OverviewActions.MarkTripAsDone -> {
                markTripAsDone(action.done)
            }
            //dELETE
            OverviewActions.DeleteTrip -> {
                deleteTrip()
            }
        }
    }


    //fetch
    private fun fetchTripData(tripId: Long) {
        if (sessionOnGoing) {
            return
        }
        viewModelScope.launch {
            Log.d("overviewVM" , "fetchTripData: Fetching trip $tripId...")


            _overviewState.update {
                it.copy(
                    loading = true ,
                    itineraryPagerLayout = layoutPref.isLayoutPager()
                )
            }

            try {
                val trip = tripSource.getTripById(tripId)
                val docs = docSource.getUserDocumentsByTripIdOnce(tripId)
                _overviewState.update {
                    it.copy(
                        trip = trip ,
                        docs = docs ,
                        loading = false
                    )
                }
                sessionOnGoing = true

                ensureActive()

                collectDaysFlow(tripId)
                collectChecklistFlow(tripId)
                collectExpensesFlow(tripId)

                val expenseNote = notesSource.getNote(tripId)

                _overviewState.update {
                    it.copy(expenseNote = expenseNote.text , expenseNoteId = expenseNote.id)
                }


            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }

        }
    }

    //--------------------- SHARE EXPENSES -----------------
    /**
     * Converts the expenses into a string, calls other function to write it to a file.
     */
    private fun shareTripExpenses(context: Context) {
        if(_overviewState.value.loading){
            return
        }else if(_overviewState.value.expenses.isEmpty()){
            return
        }
        viewModelScope.launch {
            _overviewState.update {
                it.copy(loading = true)
            }
            val shareManger = ShareExpenseManager()
            val tripName = _overviewState.value.trip?.tripName ?: "Untitled"
            val result = shareManger.convertExpensesToString(_overviewState.value.expenses)
            when (result) {
                is Result.Error -> {
                    _overviewState.update {
                        it.copy(loading = false)
                    }
                }

                is Result.Success -> {
                    result.map {
                        val expensesString = it.expensesString
                        expensesString?.let {
                            convertExpensesStringToFile(
                                expensesString ,
                                tripName ,
                                context
                            )
                        }
                    }
                }
            }


        }
    }

    /**
     * Writes formatted string of expenses to a .txt file & calls share intent function
     */
    private fun convertExpensesStringToFile(string : String, tripName : String, context: Context) {
        viewModelScope.launch {
            val fileManger = WanderaFileManager()
            val result = fileManger.writeStringToTxt(
                context ,
                string = string,
                fileName = tripName
            )
            when(result){
                is Result.Error -> {
                    when(val fileError = result.error){
                        is FileManagerError.WriteError -> {
                            _events.send(OverviewEvents.Error(fileError.errorMsg))
                        }
                    }
                    _overviewState.update {
                        it.copy(loading = false)
                    }
                }
                is Result.Success -> {
                    result.map {
                        it.fileUri?.let { uri->
                            createShareExpensesIntent(uri, tripName)
                        }
                    }
                }
            }
        }
    }
    /**
     * Creates an intent to share the expenses .txt file and sends an event to UI
     */
    private fun createShareExpensesIntent(fileUri: Uri, tripName: String){
        viewModelScope.launch {
            _overviewState.update {
                it.copy(loading = false)
            }
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"

                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                putExtra(Intent.EXTRA_SUBJECT,"Hey, here's a list of expenses for $tripName")
                putExtra(Intent.EXTRA_TEXT,"You can find all our expenses here!")
            }
            val chooserIntent = Intent.createChooser(shareIntent,"Share expenses via...")
            _events.send(OverviewEvents.ShareExpenseIntent(chooserIntent))
        }
    }


    //-------- NOTE ---------
    //TODO add saving state for the note
    private fun updateNote() {
        val noteId = _overviewState.value.expenseNoteId ?: return
        val text = _overviewState.value.expenseNote
        //Log.d("overviewVM" , "updateNote: Note is $text, id is $noteId")

        viewModelScope.launch {
            notesSource.updateNote(noteId , text)
            _events.send(OverviewEvents.SuccessWithMsg("Note saved!"))
        }
    }

    private fun onNoteValueChange(value: String) {
        viewModelScope.launch {
            _overviewState.update {
                it.copy(expenseNote = value)
            }
        }
    }

    private fun updateSelectedExpenseItem(itemId: Long?) {
        viewModelScope.launch {
            _overviewState.update {
                it.copy(
                    selectedExpenseId = itemId ,
                )
            }
        }
    }

    //-------- DOCS ---------
    private fun onDocsListCollapse() {
        viewModelScope.launch {
            _overviewState.update {
                it.copy(
                    docsListCollapsed = !it.docsListCollapsed
                )
            }
        }
    }

    //-------- Checklist ---------
    private fun checkChecklistItem(itemId: Long , checked: Boolean) {
        viewModelScope.launch {
            checklistSource.checkItem(itemId , checked)
        }
    }

    private fun deleteChecklistItem(itemId: Long) {
        viewModelScope.launch {
            checklistSource.deleteItem(itemId)
        }
    }

    private fun onChecklistCollapse() {
        viewModelScope.launch {
            _overviewState.update {
                it.copy(checklistCollapsed = !it.checklistCollapsed)
            }
        }
    }

    private fun updateChecklistProgress(list: List<ChecklistEntity>) {
        if (list.isEmpty()) {
            return
        }
        viewModelScope.launch {
            val done = list.count {
                it.isChecked
            }
            val progress = (done * 1f) / list.size

            _overviewState.update {
                it.copy(checklistProgress = progress.coerceIn(0f , 1f))
            }
        }
    }

    //-------- DAY ---------
    private fun updateDayStatus(dayId: Long , isDone: Boolean) {
        viewModelScope.launch {
            Log.d("overviewVM" , "updateDayStatus: Marking $dayId as $isDone")

            daySource.markDayAsDone(dayId , isDone)
        }
    }


    //-------- LAYOUT ---------
    private fun changeItineraryLayout() {
        viewModelScope.launch {
            _overviewState.update {

                layoutPref.setPagerLayout(
                    !it.itineraryPagerLayout
                )
                it.copy(itineraryPagerLayout = !it.itineraryPagerLayout)
            }
        }
    }

    private fun onFabCollapse(collapsed: Boolean) {
        _overviewState.update {
            it.copy(fabCollapsed = collapsed)
        }
    }

    //!!!DELETE
    private fun deleteTrip() {
        viewModelScope.launch {

            val tripId = _overviewState.value.trip?.id ?: return@launch

            _overviewState.update {
                it.copy(loading = true)
            }
            cleanUpResources()
            tripSource.deleteTripById(tripId)
            _overviewState.update {
                it.copy(loading = false)
            }
        }
    }

    //flows
    private fun collectDaysFlow(tripId: Long) {
        collectDaysJob = viewModelScope.launch {
            daySource.getDaysByTripId(tripId)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("overviewVM" , "collectDaysFlow: Error")
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.printStackTrace()
                    }
                }
                .onCompletion {
                    if (it is CancellationException) {
                        throw it
                    }
                    Log.d("overviewVM" , "collectDaysFlow: Finished with the flow")
                }
                .collect { newDays ->
                    Log.d("overviewVM" , "collectDaysFlow: NEW DAYS SIZE IS ${newDays.size}")

                    _days.update {
                        newDays
                    }
                }
        }
    }

    private fun collectChecklistFlow(tripId: Long) {
        collectChecklistJob = viewModelScope.launch {
            checklistSource.getChecklistItems(tripId)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("overviewVM" , "collectChecklistFlow: Error")
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.printStackTrace()
                    }
                }
                .onCompletion {
                    if (it is CancellationException) {
                        throw it
                    }
                    Log.d("overviewVM" , "collectChecklistFlow: Finished with the flow")
                }
                .collect { list ->
                    _overviewState.update {
                        it.copy(checklist = list)
                    }
                    updateChecklistProgress(list)
                }
        }
    }

    private fun collectExpensesFlow(tripId: Long) {
        viewModelScope.launch {
            expenseSource.getExpensesByTripId(tripId)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("overviewVM" , "collectExpensesFlow: Error")
                    if (it is CancellationException) {
                        throw it
                    } else {
                        it.printStackTrace()
                    }
                }
                .onCompletion {
                    Log.d("overviewVM" , "collectExpensesFlow: On COMPLETE")
                    if (it is CancellationException) {
                        throw it
                    }
                }
                .collect { data: List<ExpenseEntity> ->
                    val uiList = data.map {
                        it.toUIEntity()
                    }
                    val groupedList = uiList.groupBy {
                        it.timestamp.toLocalDate()
                    }
                    println(groupedList.keys)

                    _overviewState.update {
                        it.copy(
                            expenses = uiList ,
                            groupedExpenses = groupedList
                        )
                    }
                    computeTotalExpense()
                }
        }
    }

    private fun computeTotalExpense() {
        viewModelScope.launch {
            val expenses = _overviewState.value.expenses
            var total = 0
            expenses.onEach {
                total += it.amount
            }
            _overviewState.update {
                it.copy(totalExpense = total)
            }
        }
    }

    private fun playMarkAsDoneAnimation(play: Boolean) {
        viewModelScope.launch {
            _overviewState.update {
                it.copy(playMarkAsDoneAnimation = play)
            }
        }
    }

    private fun markTripAsDone(done: Boolean) {
        //ADD EVENTS!
        val tripId = _overviewState.value.trip?.id ?: return
        viewModelScope.launch {
            _overviewState.update {
                it.copy(playMarkAsDoneAnimation = true)
            }
            tripSource.markTripAsDone(true , tripId)

        }

    }

    //clean up
    private fun cleanUpResources() {
        viewModelScope.launch {
            delay(200)
            _days.update { emptyList() }
            collectDaysJob?.let {
                Log.d("overviewVM" , "cleanUpResources: Cancelling days job")
                it.cancel()
            }
            collectChecklistJob?.let {
                Log.d("overviewVM" , "cleanUpResources: Cancelling checklist job")
                it.cancel()
            }
            _overviewState.update {
                OverviewState(
                    trapeziumChecklist = it.trapeziumChecklist
                )
            }
            sessionOnGoing = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("overviewVM" , "onCleared: Clearing vm.....")
        collectDaysJob?.let {
            Log.d("overviewVM" , "onCleared: Cancelling days job")
            it.cancel()
        }

    }


}