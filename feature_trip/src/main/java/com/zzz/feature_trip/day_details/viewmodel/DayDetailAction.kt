package com.zzz.feature_trip.day_details.viewmodel

internal  sealed class DayDetailAction  {

    data class FetchDayDetails(
        val dayId: Long ,
        val collectTodosWithFlow: Boolean = false
    ) : DayDetailAction()

    data class MarkTodoAsDone(
        val todoId : Long,
        val done : Boolean
    ) : DayDetailAction()

}