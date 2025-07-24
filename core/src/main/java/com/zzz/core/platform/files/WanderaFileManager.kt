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
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

class WanderaFileManager : FileManager {

    override suspend fun writeJsonToFile(
        context: Context ,
        json: String ,
        tripName: String
    ): Result<FileManagerProgress , FileManagerError> {
        return withContext(Dispatchers.IO) {

            try {
                val fileName = generateFileName(tripName)
                val file = File(context.cacheDir , fileName)
                FileOutputStream(file)
                    .use { opStream ->
                        opStream.write(
                            json.toByteArray()
                        )
                    }

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
                Result.Error(FileManagerError.WriteError(ResultMessage.ERROR_UNKNOWN))
            }

        }
    }

    private fun generateFileName(tripName: String , extension: String = ".json" , initial : String = "Share Trip"): String {
        return "$initial ${tripName}_${System.currentTimeMillis()}$extension"
        //return "$initial ${tripName.replace(" " , "_")}_${System.currentTimeMillis()}$extension"
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
                                    opString?.ifEmpty{null}
                                )
                            }
                    } ?: return@withContext Result.Error(
                    FileManagerError.WriteError("Import failed! Couldn't open the file.")
                )

            } catch (e: IOException) {
                e.printStackTrace()
                Result.Error(FileManagerError.WriteError("Import failed! The file appears to be corrupt."))
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(FileManagerError.WriteError(ResultMessage.ERROR_UNKNOWN))
            }
        }
    }
}