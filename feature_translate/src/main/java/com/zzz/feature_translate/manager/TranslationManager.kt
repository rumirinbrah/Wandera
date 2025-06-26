package com.zzz.feature_translate.manager

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.zzz.data.translate.model.TranslationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Responsible for handling downloads, translations etc
 */
class TranslationManager {
    private var translator: Translator? = null

    private val modelManager: RemoteModelManager = RemoteModelManager.getInstance()

    private var loggingEnabled = true


    //---------- TRANSLATOR STUFF ----------

    //create
    fun createTranslator(
        sourceLang: String ,
        targetLang: String
    ) {
        log {
            "Creating translator for $sourceLang to $targetLang"
        }
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()
        translator = Translation.getClient(options)
    }
    suspend fun translateText(text : String) : String{
        return suspendCancellableCoroutine<String> {continuation->
            if(translator == null){
                continuation.resumeWithException(Exception("Translator not initialized"))
            }
            translator!!.translate(text)
                .addOnSuccessListener {result->
                    continuation.resume(result)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }


    //---------- MANAGER STUFF ----------

    //download
    suspend fun downloadModel(
        languageCode: String ,
        wifiRequired : Boolean = true
    ) = suspendCancellableCoroutine<Unit> { continuation ->


        val newModel = TranslateRemoteModel.Builder(languageCode).build()

        //check if already downloaded
        modelManager.isModelDownloaded(newModel).addOnSuccessListener { downloaded ->
            if (downloaded) {
                log {
                    "Model $languageCode already downloaded!"
                }
                continuation.resume(Unit)
            }else{
                log {
                    "Downloading model $languageCode..."
                }
                val conditions = DownloadConditions.Builder()
                    .apply {
                        if(wifiRequired){
                            requireWifi()
                        }
                    }.build()

                modelManager.download(newModel , conditions)
                    .addOnFailureListener {
                        log {
                            "Failed to download $languageCode..."
                        }
                        continuation.resumeWithException(it)
                    }
                    .addOnSuccessListener {
                        log {
                            "Downloaded $languageCode..."
                        }
                        continuation.resume(Unit)
                    }
            }
        }

    }

    //delete
    suspend fun deleteModel(
        languageCode: String
    ): Unit = suspendCancellableCoroutine { continuation ->
        log {
            "Deleting model $languageCode"
        }
        val model = TranslateRemoteModel.Builder(languageCode).build()
        modelManager.deleteDownloadedModel(model)
            .addOnSuccessListener {
                log {
                    "Deleted $languageCode..."
                }
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                log {
                    "Failed to delete $languageCode..."
                }
                continuation.resumeWithException(it)
            }
    }

    //downloaded models
    suspend fun getDownloadedModels(

    ):List<String>{
        return suspendCancellableCoroutine { continuation->
            log {
                "Fetching downloaded models"
            }
            modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
                .addOnSuccessListener {models->
                    log {
                        "Successfully fetched models!!"
                    }
                    val modelNames = models.map {
                        it.language
                    }
                    continuation.resume(modelNames)
                }
                .addOnFailureListener{
                    log {
                        "FAILED to get downloaded models..."
                    }
                    continuation.resumeWithException(it)
                }
        }
    }

    //clear
    fun clearTranslator() {
        log {
            "Clearing translator"
        }
        translator?.close()
        translator = null
    }

    private fun log(
        msg: () -> String ,
    ) {
        if (loggingEnabled) {
            Log.d("tManager" , msg())
        }
    }


}