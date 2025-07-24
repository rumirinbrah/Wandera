package com.zzz.core.domain.file

import android.content.Context
import android.net.Uri
import com.zzz.core.domain.result.Result
import com.zzz.core.platform.files.util.FileManagerError
import com.zzz.core.platform.files.util.FileManagerProgress
import kotlinx.coroutines.flow.Flow

interface FileManager {

    suspend fun writeJsonToFile(
        context: Context ,
        json: String ,
        tripName: String = "untitled"
    ) : Result<FileManagerProgress , FileManagerError>

    suspend fun readTripJsonFromUri(
        context: Context,
        jsonUri : Uri
    ) : Result<String? , FileManagerError>

}