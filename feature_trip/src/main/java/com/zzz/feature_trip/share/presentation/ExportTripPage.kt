package com.zzz.feature_trip.share.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.ElevatedIconTextButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.feature_trip.R
import com.zzz.feature_trip.share.presentation.viewmodel.ShareTripViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExportTripRoot(
    shareTripViewModel: ShareTripViewModel,
    modifier: Modifier = Modifier
) {

}

@Composable
fun ExportTripPage(
    tripId : Long,
    modifier: Modifier = Modifier
) {
    val shareViewModel = koinViewModel<ShareTripViewModel>()
    val state by shareViewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        VerticalSpace(40.dp)
        Text(
            "How sharing with Wandera works?",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground.copy(0.8f),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(
                R.drawable.share_link_undraw
            ),
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
                append("Wandera stores all your data locally. As a result, when you share a Trip, ")

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium
                    )
                ){
                    append("your Trip's Details, Itinerary ")
                }
                append("will be the only things that are shared.")
                append("\nNote that ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium
                    )
                ){
                    append("Images & your Documents")
                }
                append(" are not shared.")

                append("\nThe person you're sharing with can add the images later on.")
            },
            textAlign = TextAlign.Center
        )


        Text("Trip ID $tripId")

        ElevatedIconTextButton(
            icon = com.zzz.core.R.drawable.share ,
            text = "Start Share" ,
            onClick = {
                shareViewModel.exportTrip(tripId)
            }
        )


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

        SelectionContainer {
            state.encodedTrip?.let {
                Text(it)
            }
        }



    }
}

@Preview(showBackground = true)
@Composable
private fun ExportPrev() {
    MaterialTheme {
        Column (
            Modifier.fillMaxSize()
        ){
            ExportTripPage(0)
        }
    }
}