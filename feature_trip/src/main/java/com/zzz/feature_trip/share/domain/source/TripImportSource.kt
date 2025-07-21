package com.zzz.feature_trip.share.domain.source

import com.zzz.core.domain.result.Result
import com.zzz.feature_trip.share.domain.result.ExportError
import com.zzz.feature_trip.share.domain.result.ImportProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

interface TripImportSource {

    val json : Json

    suspend fun importTripFromJson(encodedTripJson : String) : Flow<Result<ImportProgress,ExportError>>

}