package com.zzz.feature_trip.share.domain.models

import com.zzz.data.trip.DayWithTodos
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip

internal fun Trip.toExportableTrip() : ExportableTrip{
    return ExportableTrip(
        tripName = tripName,
        startDate = startDate ,
        endDate = endDate ,
        dateCreated = dateCreated
    )
}
internal fun ExportableTrip.toTripEntity() : Trip{
    return Trip(
        tripName = tripName,
        startDate = startDate,
        endDate = endDate,
        dateCreated = dateCreated
    )
}


internal fun Day.toExportableDay() : ExportableDay{
    return ExportableDay(
        locationName = locationName,
        isDone = isDone
    )
}
internal fun ExportableDay.toDayEntity() : Day{
    return Day(
        locationName = this.locationName ,
        isDone = this.isDone,
        tripId = 0
    )
}


internal fun TodoLocation.toExportableTodoLocation() : ExportableTodos{
    return ExportableTodos(
        title,
        isTodo
    )
}
internal fun ExportableTodos.toTodoLocationEntity() : TodoLocation{
    return TodoLocation(
        title = title,
        isTodo = isTodo
    )
}


internal fun DayWithTodos.toExportableDayWithTodos() : ExportableDayWithTodos{
    val mappedTodos = todosAndLocations.map {
        it.toExportableTodoLocation()
    }
    return ExportableDayWithTodos(
        day = day.toExportableDay(),
        todos = mappedTodos
    )
}