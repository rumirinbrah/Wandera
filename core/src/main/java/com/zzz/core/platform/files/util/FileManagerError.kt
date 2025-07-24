package com.zzz.core.platform.files.util

import com.zzz.core.domain.result.Error

sealed class FileManagerError : Error {
    data class WriteError(val errorMsg : String) : FileManagerError()
}
