package com.zzz.data.trip

import androidx.room.Embedded
import androidx.room.Relation
import com.zzz.data.trip.model.Day
import com.zzz.data.trip.model.TodoLocation
import com.zzz.data.trip.model.Trip
import com.zzz.data.trip.model.UserDocument

data class TripWithDaysAndTodos(
    @Embedded val trip: Trip ,
    @Relation(
        parentColumn = "id",
        entityColumn = "tripId",
        entity = Day::class
    )
    val daysWithTodos : List<DayWithTodos>
)
data class TripWithDays(
    @Embedded
    val trip: Trip,
    @Relation(
        parentColumn = "id",
        entityColumn = "tripId",
        entity = Day::class
    )
    val days : List<Day>
)

data class DayWithTodos(
    @Embedded val day: Day ,
    @Relation(
        parentColumn = "id",
        entityColumn = "dayId"
    )
    val todosAndLocations : List<TodoLocation>
)