package com.zzz.feature_trip.share.domain.source

import com.zzz.core.domain.result.Result
import com.zzz.feature_trip.share.domain.result.ExportProgress
import com.zzz.feature_trip.share.domain.result.ExportError
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

interface TripExportSource {

    val json : Json

    suspend fun exportTripToJson(tripId : Long) : Flow<Result<ExportProgress , ExportError>>

}