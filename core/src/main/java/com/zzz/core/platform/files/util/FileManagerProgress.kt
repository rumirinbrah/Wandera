package com.zzz.core.platform.files.util

import android.net.Uri

data class FileManagerProgress(
    val progressMsg : String,
    val fileUri : Uri? = null
)
