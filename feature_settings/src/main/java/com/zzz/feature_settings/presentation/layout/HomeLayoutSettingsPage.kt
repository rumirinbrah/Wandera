package com.zzz.feature_settings.presentation.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.ActionButtonHeader
import com.zzz.feature_settings.R
import com.zzz.feature_settings.presentation.viewmodel.AppSettingsState
import com.zzz.feature_settings.presentation.viewmodel.SettingsAction

@Composable
internal fun HomeLayoutSettingsPage(
    state : AppSettingsState,
    onAction : (action : SettingsAction)->Unit,
    navUp : ()->Unit,
    modifier: Modifier = Modifier
) {
    val colScrollState = rememberScrollState()

    Column(
        modifier.fillMaxSize()
            .verticalScroll(colScrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButtonHeader(
            actionIcon = com.zzz.core.R.drawable.arrow_back ,
            title = "Home settings" ,
            fontSize = 20.sp ,
            itemsSpacing = 8.dp ,
            fontWeight = FontWeight.Bold ,
            onAction = {
                navUp()
            }
        )

        VerticalSpace(10.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(
                selected = state.ticketLikeHomeContainer,
                onClick = {
                    onAction(SettingsAction.SetTicketContainer(!state.ticketLikeHomeContainer))
                }
            )
            Text(
                "Ticket-Like containers",
                fontWeight = FontWeight.Medium
            )
        }
        Text("These containers will look like-")
        Image(
            painter = painterResource(R.drawable.home_item_ticket),
            contentDescription = "Ticket-like containers",
            modifier = Modifier.clip(MaterialTheme.shapes.medium)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        VerticalSpace()
        HorizontalDivider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(
                selected = !state.ticketLikeHomeContainer,
                onClick = {
                    onAction(SettingsAction.SetTicketContainer(!state.ticketLikeHomeContainer))
                }
            )
            Text(
                "Plain Box containers",
                fontWeight = FontWeight.Medium
            )
        }
        Text("These containers will look like-")
        Image(
            painter = painterResource(R.drawable.home_item_normal),
            contentDescription = "Normal box containers",
            modifier = Modifier.clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .background(Color.Gray),
            contentScale = ContentScale.Fit
        )

        VerticalSpace()
        Text(
            "you may have to restart the app to see changes.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        VerticalSpace()
    }
}

@Preview(showBackground = true)
@Composable
private fun IDK() {
    MaterialTheme {
        //HomeLayoutSettingsPage(navUp = {})
    }
}