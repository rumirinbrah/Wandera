package com.zzz.feature_trip.share.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.feature_trip.share.presentation.viewmodel.ShareTripViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImportTripPage(
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
        Button(
            onClick = {
                shareViewModel.importTrip(
                    encodedTripJson = """
                        {
                            "trip": {
                                "tripName": "Italy",
                                "startDate": 1753056000000,
                                "endDate": 1753401600000,
                                "dateCreated": 1753087142913
                            },
                            "days": [
                                {
                                    "day": {
                                        "locationName": "Dolomites",
                                        "isDone": false
                                    },
                                    "todos": [
                                        {
                                            "title": "Lake",
                                            "isTodo": true
                                        },
                                        {
                                            "title": "Alps",
                                            "isTodo": false
                                        }
                                    ]
                                },
                                {
                                    "day": {
                                        "locationName": "France",
                                        "isDone": false
                                    },
                                    "todos": [
                                        {
                                            "title": "Bread",
                                            "isTodo": true
                                        },
                                        {
                                            "title": "Mountains",
                                            "isTodo": false
                                        }
                                    ]
                                }
                            ]
                        }
                    """.trimIndent()
                )
            }
        ) {
            Text("Import")
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