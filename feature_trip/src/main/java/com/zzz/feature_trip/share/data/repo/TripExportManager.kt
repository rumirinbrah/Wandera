package com.zzz.feature_trip.share.data.repo

import com.zzz.core.domain.result.Result
import com.zzz.data.trip.source.TripSource
import com.zzz.feature_trip.share.domain.result.ExportProgress
import com.zzz.feature_trip.share.domain.models.ExportableTripWithDays
import com.zzz.feature_trip.share.domain.models.toExportableDayWithTodos
import com.zzz.feature_trip.share.domain.models.toExportableTrip
import com.zzz.feature_trip.share.domain.result.ExportError
import com.zzz.feature_trip.share.domain.source.TripExportSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

class TripExportManager(
    private val tripSource: TripSource ,
) : TripExportSource {

    override val json: Json
        get() = Json{
            prettyPrint = true
            encodeDefaults = true
        }

    /**
     * ADD ENSURE ACTIVES!
     */
    override suspend fun exportTripToJson(tripId: Long): Flow<Result<ExportProgress , ExportError>> {
        return flow {
            //withContext(Dispatchers.IO) {
                try {
                    val tripWithDays = tripSource.getTripWithDaysAndTodosById(tripId)
                    val trip = tripWithDays.trip
                    emit(
                        Result.Success(
                            ExportProgress(progressMsg = "Preparing Trip...")
                        )
                    )


                    //Adding delays for testing of results; ignore these
                    delay(1000)
                    val daysWithTodos = tripWithDays.daysWithTodos
                    emit(
                        Result.Success(
                            ExportProgress(progressMsg = "Fetching your itinerary...")
                        )
                    )


                    delay(1000)
                    val days = daysWithTodos
                    emit(
                        Result.Success(
                            ExportProgress(progressMsg = "Handling TODOs...")
                        )
                    )


                    delay(1000)
                    val mappedDays = days.map {
                        it.toExportableDayWithTodos()
                    }
                    val mappedTrip = trip.toExportableTrip()
                    emit(
                        Result.Success(
                            ExportProgress(progressMsg = "Almost there...")
                        )
                    )

                    val exportableTrip = ExportableTripWithDays(
                        trip = mappedTrip,
                        days = mappedDays
                    )

                    val encodedJson = json.encodeToString(ExportableTripWithDays.serializer() , exportableTrip)

                    emit(
                        Result.Success(
                            ExportProgress(
                                progressMsg = "Good to go!",
                                done = true,
                                exportedTrip = encodedJson
                            )
                        )
                    )


                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(
                        Result.Error(
                            ExportError.Error("An unknown error occurred sharing the trip!")
                        )
                    )
                }


            //}

        }.flowOn(Dispatchers.IO)

    }

}