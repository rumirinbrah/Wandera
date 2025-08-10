package com.zzz.core.platform.files

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.zzz.core.domain.file.FileManager
import com.zzz.core.domain.result.Result
import com.zzz.core.platform.files.util.FileManagerError
import com.zzz.core.platform.files.util.FileManagerProgress
import com.zzz.core.platform.files.util.ResultMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author zyzz
 */
class WanderaFileManager : FileManager {

    
    override suspend fun writeJsonToFile(
        context: Context ,
        json: String ,
        tripName: String
    ): Result<FileManagerProgress , FileManagerError> {
        return withContext(Dispatchers.IO) {

            try {
                //----------- WRITE FILE -------------
                val fileName = generateFileName(tripName)
                val file = File(context.cacheDir , fileName)
                FileOutputStream(file)
                    .use { opStream ->
                        opStream.write(
                            json.toByteArray()
                        )
                    }

                //----------- GET URI -------------
                val fileUri = FileProvider.getUriForFile(
                    context ,
                    "${context.packageName}.provider" ,
                    file
                )
                Result.Success(
                    FileManagerProgress(
                        progressMsg = "Ready to share" ,
                        fileUri = fileUri
                    )
                )

            } catch (e: IllegalArgumentException) {

                e.printStackTrace()
                Result.Error(FileManagerError.WriteError(ResultMessage.ILLEGAL_ARG))
            } catch (e: Exception) {

                e.printStackTrace()
                Result.Error(FileManagerError.WriteError(ResultMessage.ERROR_UNKNOWN))
            }

        }
    }


    override suspend fun writeStringToTxt(
        context: Context ,
        string: String ,
        fileName: String
    ): Result<FileManagerProgress , FileManagerError> {
        return withContext(Dispatchers.IO){
            try {
                val txtFileName = generateFileName(
                    fileName = fileName ,
                    extension = ".txt" ,
                    initial = "Trip Expenses" ,
                    appendTimeMillisLong = false
                )
                val file = File(context.cacheDir , txtFileName)
                FileOutputStream(file)
                    .use { opStream ->
                        opStream.write(
                            string.toByteArray()
                        )
                    }
                val fileUri = FileProvider.getUriForFile(
                    context ,
                    "${context.packageName}.provider" ,
                    file
                )
                Result.Success(
                    FileManagerProgress(
                        progressMsg = "Expenses Ready!" ,
                        fileUri = fileUri
                    )
                )
            } catch (e: IllegalArgumentException) {
                Result.Error(
                    FileManagerError.WriteError("Couldn't open file!")
                )
            } catch (e : Exception) {
                Result.Error(
                    FileManagerError.WriteError(ResultMessage.ERROR_UNKNOWN)
                )
            }
        }
    }

    /**
     * Generated a file name for the json
     * @param extension File extension
     * @param initial File initial to be displayed before name
     * @param appendTimeMillisLong Whether to include long time millis in the file name
     * @return File name
     */
    private fun generateFileName(
        fileName: String ,
        extension: String = ".json" ,
        initial: String = "Share Trip" ,
        appendTimeMillisLong: Boolean = true
    ): String {
        return when {
            appendTimeMillisLong -> {
                "$initial ${fileName}_${System.currentTimeMillis()}$extension"
            }

            else -> {
                "$initial ${fileName}$extension"
            }
        }

    }

    override suspend fun readTripJsonFromUri(
        context: Context ,
        jsonUri: Uri
    ): Result<String? , FileManagerError> {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(jsonUri)
                    ?.use { ipStream ->
                        ipStream.bufferedReader()
                            .use { bufferedReader ->
                                var opString: String? = ""
                                var currentLine: String? = ""
                                currentLine = bufferedReader.readLine()
                                while (
                                    currentLine != null
                                ) {
                                    opString += currentLine
                                    currentLine = bufferedReader.readLine()
                                }
                                return@withContext Result.Success(
                                    opString?.ifEmpty { null }
                                )
                            }
                    } ?: return@withContext Result
                    .Error(
                        FileManagerError.WriteError(ResultMessage.FILE_OPEN_ERROR)
                    )

            } catch (e: IOException) {
                e.printStackTrace()
                Result.Error(FileManagerError.WriteError(ResultMessage.FILE_CORRUPT_ERROR))
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(FileManagerError.WriteError(ResultMessage.ERROR_UNKNOWN))
            }
        }
    }
}