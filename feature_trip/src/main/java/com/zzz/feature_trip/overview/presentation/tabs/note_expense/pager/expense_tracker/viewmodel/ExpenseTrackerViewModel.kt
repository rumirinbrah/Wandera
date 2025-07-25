package com.zzz.feature_trip.overview.presentation.tabs.note_expense.pager.expense_tracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import com.zzz.data.trip.model.ExpenseEntity
import com.zzz.data.trip.source.ExpenseSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Handles user input while creating Expense, CRUD
 */
class ExpenseTrackerViewModel(
    private val dataSource : ExpenseSource
) : ViewModel() {

    private var loggingEnabled = true

    private val _state = MutableStateFlow(AddExpenseState())
    val state = _state
        .onStart {
            log {
                "Collecting flow..."
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )
    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()


    fun onAction(action: ExpenseActions){
        when(action){
            is ExpenseActions.FetchExpenseData->{
                fetchExpenseData(action.itemId)
            }

            is ExpenseActions.OnAmountChange -> {
                onAmountChange(action.amount)
            }
            is ExpenseActions.OnExpenseTypeChange -> {
                changeExpenseType(action.expenseType)
            }
            is ExpenseActions.OnSplitIntoChange -> {
                onSplitIntoChange(action.splitInto)
            }
            is ExpenseActions.OnTitleChange -> {
                onTitleChange(action.value)
            }

            ExpenseActions.Discard -> {
                resetState()
            }
            is ExpenseActions.Save -> {
                saveExpense(action.tripId)
            }
            is ExpenseActions.Update ->{
                updateExpense(action.tripId)
            }
            ExpenseActions.DeleteExpense ->{
                deleteExpense()
            }
        }
    }

    //------ UI ------
    private fun onTitleChange(value : String){
        viewModelScope.launch {
            _state.update {
                it.copy(title = value)
            }
        }
    }
    private fun onAmountChange(amount : String){
        if(amount!="" && !Regex("^\\d+").matches(amount)){
            log { "Invalid amount..." }
            return
        }
        viewModelScope.launch {
            _state.update {
                it.copy(amount = amount)
            }

        }
    }
    private fun onSplitIntoChange(splitInto : String){
        if(splitInto!="" && !Regex("^\\d+").matches(splitInto)){
            log { "Invalid split..." }
            return
        }
        viewModelScope.launch {
            _state.update {
                it.copy(splitInto = splitInto)
            }
        }
    }
    private fun changeExpenseType(type : String){
        viewModelScope.launch {
            _state.update {
                it.copy(expenseType = type)
            }
        }
    }

    /**
     * Fetch details about the selected expense to update, delete
     */
    private fun fetchExpenseData(expenseId : Long){
        viewModelScope.launch {
            log {
                "fetching expense $expenseId..."
            }
            try {
                _state.update {
                    it.copy(
                        loading = true,
                        updating = true
                    )
                }
                withContext(Dispatchers.IO){
                    val item = dataSource.getExpenseById(expenseId)
                    log {
                        "fetched expense ${item.title}"
                    }
                    withContext(Dispatchers.Main){
                        _state.update {
                            it.copy(
                                amount = item.amount.toString(),
                                title = item.title,
                                expenseType = item.expenseType,
                                splitInto = item.splitInto?.toString(),
                                timestamp = item.timestamp
                            )
                        }
                    }
                }


                _state.update {
                    it.copy(loading = false , expenseId = expenseId)
                }
            }catch (e : Exception){
                _state.update {
                    it.copy(loading = false)
                }
                ensureActive()
                e.printStackTrace()
            }
        }
    }

    /**
     * Cleanup
     */
    private fun resetState(){
        log {
            "RESET STATE"
        }
        viewModelScope.launch {
            _state.update {
                AddExpenseState()
            }
        }
    }

    //---- SAVE ----
    private fun saveExpense(tripId : Long){
        if(_state.value.saving){
            log {
                "Already saving"
            }
            return
        }
        val valid = validateUserInput()
        if(!valid){
            return
        }
        viewModelScope.launch {
            val values = _state.value
            try {

                val entity = ExpenseEntity(
                    amount = values.amount.toInt() ,
                    title = values.title ,
                    splitInto = values.splitInto?.toInt() ,
                    expenseType = values.expenseType,
                    tripId = tripId,
                )
                val id =dataSource.addExpense(entity)
                log {
                    "Added expense $id"
                }
                _events.send(UIEvents.Success)


            } catch (e: NumberFormatException) {
                e.printStackTrace()
                _events.trySend(UIEvents.Error("Please enter valid input!"))
                log {
                    "Error formatting number!"
                }

            }catch (e : Exception){
                e.printStackTrace()
                _events.trySend(UIEvents.Error("Failed to save expense!"))

            }

        }
    }
    private fun updateExpense(tripId: Long){
        if(_state.value.saving){
            log {
                "Already saving"
            }
            return
        }
        val valid = validateUserInput()
        if(!valid){
            log {
                "Invalid data"
            }
            return
        }
        viewModelScope.launch {
            log {
                "Updating..."
            }
            val values = _state.value
            val itemId = values.expenseId ?: kotlin.run {
                log {
                    "Update : Expense ID not found!!"
                }
                return@launch
            }

            try {
                _state.update {
                    it.copy(saving = true)
                }
                val entity = ExpenseEntity(
                    id =  itemId,
                    amount =  values.amount.toInt(),
                    title =  values.title,
                    expenseType =  values.expenseType,
                    splitInto =  values.splitInto?.toInt(),
                    timestamp =  values.timestamp,
                    tripId = tripId
                )

                dataSource.updateExpense(entity)
                log{
                    "Updated $itemId"
                }
                _events.send(UIEvents.Success)
                _state.update {
                    it.copy(saving = false)
                }
            }catch (e: NumberFormatException){
                e.printStackTrace()
                _state.update {
                    it.copy(saving = false)
                }
                _events.trySend(UIEvents.Error("Please enter valid input!"))
                log {
                    "Error formatting number!"
                }
            }catch (e : Exception){
                e.printStackTrace()
                _state.update {
                    it.copy(saving = false)
                }
                _events.trySend(UIEvents.Error("Failed to save expense!"))
            }
        }
    }
    private fun deleteExpense(){
        val itemId = _state.value.expenseId ?: return
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(loading = true)
                }
                dataSource.deleteExpense(itemId)
                _state.update {
                    it.copy(loading = false)
                }
                _events.send(UIEvents.Success)
            } catch (e : Exception) {
                _state.update {
                    it.copy(loading = false)
                }
                e.printStackTrace()
                _events.send(UIEvents.Error("Failed to delete expense!"))
            }

        }
    }

    private fun validateUserInput(

    ):Boolean{
        log {
            "Validating user input..."
        }
        val values = _state.value
        try {
            if(values.amount.isEmpty()){
                _events.trySend(UIEvents.Error("Please enter valid amount!"))
                return false
            }
            values.splitInto?.let {
                if(it.isEmpty()){
                    _events.trySend(UIEvents.Error("Please enter a valid split number!"))
                    return false
                }
            }

        }catch (e : NumberFormatException){
            _events.trySend(UIEvents.Error("Please enter valid data!"))
            e.printStackTrace()
            log {
                "Error formatting number!"
            }
            return false
        }
        return true
    }

    private fun log(msg : ()->String){
        if(loggingEnabled){
            Log.d("expenseTrackVm" , msg())
        }
    }

    override fun onCleared() {
        super.onCleared()
        log {
            "Clearing vm..."
        }
    }

}