package com.zzz.feature_trip.create.presentation.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.ElevatedIconTextButton
import com.zzz.core.presentation.dialogs.DialogWithTextField

@Composable
fun UploadDocumentComponent(
    onAddDoc :(uri:Uri , docName : String)->Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var currentDocUri by remember { mutableStateOf<Uri?>(null) }
    var showNameDocDialog by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {uri->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            currentDocUri = uri
            showNameDocDialog = true
        }
    }
    val pdfPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {uri->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            currentDocUri = uri
            showNameDocDialog = true
        }
    }


    //docs
    Column(
        modifier.fillMaxWidth()
    ) {
        Text(
            "Add tickets, documents, ID, etc." ,
            fontSize = 16.sp ,
            fontWeight = FontWeight.Bold ,
        )
        Text(
            text = "(these files are not uploaded anywhere & are stored on your own device, make sure you don't delete them)" ,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //image
            ElevatedIconTextButton(
                icon = com.zzz.core.R.drawable.upload_image ,
                text = "Image" ,
                onClick = {
                    imagePicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
            //pdf
            ElevatedIconTextButton(
                icon = com.zzz.core.R.drawable.pdf ,
                text = "PDF" ,
                onClick = {
                    pdfPicker.launch(arrayOf("application/pdf"))
                }
            )
        }
        if(showNameDocDialog){
            DialogWithTextField(
                title = "Name the document" ,
                textFieldPlaceholder = "Document name" ,
                onDone = {name->
                    if(docNameValid(name)){
                        currentDocUri?.let {
                            onAddDoc(currentDocUri!!,name)
                            currentDocUri = null
                            showNameDocDialog = false
                        }
                    }else{
                        Toast.makeText(context , "Oops! Seems you forgot to name the document" , Toast.LENGTH_SHORT).show()
                    }
                } ,
                onDismiss = {
                    showNameDocDialog = false
                    currentDocUri = null
                },
                dismissEnabled = false
            )
        }

    }

}
private fun docNameValid(str : String) : Boolean{
    return str.isNotEmpty()
}