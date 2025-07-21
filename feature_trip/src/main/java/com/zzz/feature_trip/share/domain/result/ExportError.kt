package com.zzz.feature_trip.share.domain.result

import com.zzz.core.domain.result.Error

sealed class ExportError : Error {
    data class Error(val errorMsg : String) : ExportError()
}