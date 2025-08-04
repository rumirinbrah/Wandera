package com.zzz.core.domain.file

import android.content.Context
import android.net.Uri
import com.zzz.core.domain.result.Result
import com.zzz.core.platform.files.util.FileManagerError
import com.zzz.core.platform.files.util.FileManagerProgress

/**
 * File manager interface for handling writing a json into a file as well
 * as reading a data entity from the json.
 */
interface FileManager {

    /**
     * Writes the json into a file. Returns uri on success through `@FileManagerProgress`
     */
    suspend fun writeJsonToFile(
        context: Context ,
        json: String ,
        tripName: String = "untitled"
    ) : Result<FileManagerProgress , FileManagerError>

    /**
     * Reads trip from uri and returns the json
     */
    suspend fun readTripJsonFromUri(
        context: Context,
        jsonUri : Uri
    ) : Result<String? , FileManagerError>

}