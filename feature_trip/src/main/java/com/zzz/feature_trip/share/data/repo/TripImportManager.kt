package com.zzz.feature_trip.share.data.repo

import com.zzz.core.domain.result.Result
import com.zzz.data.trip.source.DaySource
import com.zzz.data.trip.source.TodoSource
import com.zzz.data.trip.source.TripSource
import com.zzz.feature_trip.share.domain.models.ExportableTripWithDays
import com.zzz.feature_trip.share.domain.models.toDayEntity
import com.zzz.feature_trip.share.domain.models.toTodoLocationEntity
import com.zzz.feature_trip.share.domain.models.toTripEntity
import com.zzz.feature_trip.share.domain.result.ExportError
import com.zzz.feature_trip.share.domain.result.ImportProgress
import com.zzz.feature_trip.share.domain.source.TripImportSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class TripImportManager(
    private val tripSource: TripSource,
    private val daySource: DaySource,
    private val todoSource : TodoSource
) :TripImportSource{

    override val json: Json
        get() = Json{
            prettyPrint = true
            encodeDefaults = true
        }

    override suspend fun importTripFromJson(encodedTripJson: String): Flow<Result<ImportProgress , ExportError>> {
        return flow {
            try {
                emit(
                    Result.Success(ImportProgress(progressMsg = "Importing Trip..."))
                )

                delay(1000)
                val exportableTrip = json.decodeFromString(
                    ExportableTripWithDays.serializer() ,
                    encodedTripJson
                )
                val exportableDayWithTodos = exportableTrip.days
                emit(
                    Result.Success(ImportProgress(progressMsg = "Preparing data..."))
                )

                delay(1000)
                // --- ADD TRIP ----
                val tripId = kotlin.run {
                    val trip = exportableTrip.trip.toTripEntity()
                    tripSource.addTrip(trip)
                }
                emit(
                    Result.Success(ImportProgress(progressMsg = "Adding itinerary..."))
                )

                delay(1000)
                exportableDayWithTodos.onEach {
                    // --- ADD DAY ----
                    val dayId = kotlin.run {
                        val day = it.day.toDayEntity().copy(tripId = tripId)
                        daySource.addDay(day)
                    }

                    // --- ADD TODOS ----
                    it.todos.onEach { exportableTodo->
                        val todo = exportableTodo.toTodoLocationEntity().copy(dayId = dayId)
                        todoSource.addTodo(todo)
                    }
                }
                emit(
                    Result.Success(ImportProgress(progressMsg = "Trip saved!" , inProgress = false))
                )


            }catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    Result.Error(
                        ExportError.Error("An unknown error occurred importing the Trip!")
                    )
                )

            }
        }.flowOn(Dispatchers.IO)
    }
}