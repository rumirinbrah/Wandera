package com.zzz.feature_trip.create.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.ElevatedTextButton
import com.zzz.core.presentation.modifiers.customShadow

/*
image: Uri? = null ,
    dayTitle: String ,
    onImagePick: (Uri) -> Unit ,
 */
/**
 * @author zyzz
 */
@Composable
internal fun UploadImageComponent(
    launchPicker : () ->Unit,
    isViewOnly: Boolean = false ,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    /*
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                uri ,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            onImagePick(uri)
        }
    }

    val docPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                uri ,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            onImagePick(uri)
        }
    }
    val useImagePicker = remember {
        isSdkVersionGreaterThanEqualTo(Build.VERSION_CODES.TIRAMISU)
    }

     */

    Box(){
        Column(
            modifier = modifier ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isViewOnly) {
                Text(
                    "Have a photo of the location?" ,
                    fontSize = 16.sp ,
                    fontWeight = FontWeight.Bold ,
                )
                ElevatedTextButton(
                    text = "Select image" ,
                    onClick = {
//                        if(useImagePicker){
//                            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                        }else{
//                            docPicker.launch(arrayOf("image/*"))
//                        }
                        launchPicker()
                    } ,
                    contentDescription = "Add image for the location" ,
                    modifier = Modifier
                        .customShadow(
                            color = MaterialTheme.colorScheme.onBackground,
                            shadowRadius = 15f,
                            alpha = 0.2f
                        )
                )
            }
            /*
            AnimatedVisibility(image != null) {
                ImageComponentWithDefaultBackground(
                    title = dayTitle ,
                    imageUri = image ,
                    modifier = Modifier.size(70.dp)
                )
            }

             */
        }
    }

}