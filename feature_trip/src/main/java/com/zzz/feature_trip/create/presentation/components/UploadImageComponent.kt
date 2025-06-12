package com.zzz.feature_trip.create.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.NormalButton
import com.zzz.core.presentation.components.ImageComponent

/**
 * @author zyzz
 */
@Composable
internal fun UploadImageComponent(
    image : Uri? = null,
    dayTitle : String,
    onImagePick : (Uri)->Unit,
    isViewOnly : Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION)
            onImagePick(uri)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if(!isViewOnly){
            Text(
                "Have a photo of the location?" ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
            )
            NormalButton(
                title = "Select image" ,
                onClick = {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } ,
                contentDescription = "Add image for the location" ,
                shape = RoundedCornerShape(50) ,
            )
        }
        AnimatedVisibility(image != null) {
            ImageComponent(
                title = dayTitle ,
                imageUri = image ,
                modifier = Modifier.size(70.dp)
            )
        }
    }
}