package com.zzz.data.trip

import androidx.room.Embedded
import androidx.room.Relation
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip

data class TripWithDaysAndTodos(
    @Embedded val trip: Trip ,
    @Relation(
        parentColumn = "id",
        entityColumn = "tripId",
        entity = Day::class
    )
    val daysWithTodos : List<DayWithTodos>
)

data class DayWithTodos(
    @Embedded val day: Day ,
    @Relation(
        parentColumn = "id",
        entityColumn = "dayId"
    )
    val todosAndLocations : List<TodoLocation>
)