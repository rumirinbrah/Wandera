package com.zzz.feature_trip.share.presentation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.feature_trip.R
import com.zzz.feature_trip.share.presentation.viewmodel.ShareTripViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImportTripPage(
    tripJsonUri: Uri? = null,
    modifier: Modifier = Modifier
) {
    val shareViewModel = koinViewModel<ShareTripViewModel>()
    val state by shareViewModel.state.collectAsStateWithLifecycle()



    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Image(
            painter = painterResource(
                R.drawable.share_link_undraw
            ) ,
            contentDescription = "share image illustration",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )
        VerticalSpace(5.dp)
        Text(
            text = buildAnnotatedString {
                pushStyle(
                    SpanStyle(
                        fontSize = 18.sp,
                    )
                )
                append("Before importing any Trips, make sure you and your friend have the same ")

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium
                    )
                ){
                    append("app version")
                }
                append(", or the import function may not work.")

            } ,
            textAlign = TextAlign.Center
        )


        if(state.primaryButtonVisible){
            Button(
                onClick = {
                    tripJsonUri?.let {
                        shareViewModel.exportTripJsonFromUri(it)
                    }
                }
            ) {
                Text("Import Trip")
            }
        }


        VerticalSpace(10.dp)
        if(state.inProgress){
            CircularProgressIndicator(
                modifier = Modifier.size(25.dp),
                strokeWidth = 3.dp,
            )
        }
        Text(
            state.progressMsg,
            fontSize = 13.sp
        )
    }
}