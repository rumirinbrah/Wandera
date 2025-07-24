package com.zzz.feature_trip.share.presentation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.zzz.core.presentation.buttons.IconTextButton
import com.zzz.core.presentation.buttons.WanderaTextButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.ActionButtonHeader
import com.zzz.feature_trip.R
import com.zzz.feature_trip.share.presentation.viewmodel.ShareAction
import com.zzz.feature_trip.share.presentation.viewmodel.ShareState
import com.zzz.feature_trip.share.presentation.viewmodel.ShareTripViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImportTripRoot(
    tripJsonUri: Uri? = null,
    shareTripViewModel: ShareTripViewModel = koinViewModel() ,
    navToSettings : ()->Unit= {},
    navUp : () ->Unit,
    modifier: Modifier = Modifier
) {
    val state by shareTripViewModel.state.collectAsStateWithLifecycle()

    ImportTripPage(
        tripJsonUri = tripJsonUri,
        state = state,
        onAction = {action->
            shareTripViewModel.onAction(action)
        },
        navToSettings = navToSettings ,
        navUp = navUp,
        modifier = modifier
    )
}
@Composable
internal fun ImportTripPage(
    tripJsonUri: Uri? = null ,
    state : ShareState ,
    onAction : (action : ShareAction)->Unit ,
    navToSettings : ()->Unit= {},
    navUp : () ->Unit,
    modifier: Modifier = Modifier
) {
    //val shareViewModel = koinViewModel<ShareTripViewModel>()
    //val state by shareViewModel.state.collectAsStateWithLifecycle()



    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        ActionButtonHeader(
            actionIcon = com.zzz.core.R.drawable.arrow_back,
            onAction = navUp,
            title = null
        )
        VerticalSpace(40.dp)

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
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append("App Version")
                }
                append(", or the import function may not work.")

            } ,
            textAlign = TextAlign.Center
        )

        VerticalSpace()
        if(state.primaryButtonVisible){
            IconTextButton(
                text = "Import Trip",
                icon = R.drawable.map,
                onClick = {
                    tripJsonUri?.let {
                        onAction(ShareAction.ExportTripJsonFromUri(it))
                    }
                },
                background = MaterialTheme.colorScheme.surfaceContainer.copy(0.5f),
                onBackground = MaterialTheme.colorScheme.onBackground
            )
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
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        VerticalSpace()

        WanderaTextButton(
            text = "How do I import a Trip in Wandera?",
            onClick = {
                navToSettings()
            }
        )
    }
}