package com.zzz.feature_settings.presentation.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.headers.ActionButtonHeader

@Composable
internal fun ChecklistBoxSettingsPage(
    navUp : ()->Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButtonHeader(
            actionIcon = com.zzz.core.R.drawable.arrow_back ,
            title = "Checklist settings" ,
            fontSize = 20.sp ,
            itemsSpacing = 8.dp ,
            fontWeight = FontWeight.Bold ,
            onAction = {
                navUp()
            }
        )
        VerticalSpace()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(
                selected = true,
                onClick = {

                }
            )
            Text("Edgy containers")
        }
        Text("These containers will look like-")
        Image(
            painter = painterResource(com.zzz.core.R.drawable.itinerary_illustration) ,
            contentDescription = "Ticket-like containers",
            modifier = Modifier.fillMaxWidth()
                .background(Color.Gray),
            contentScale = ContentScale.Fit
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(
                selected = false,
                onClick = {

                }
            )
            Text("Plain box containers")
        }
        Text("These containers will look like-")
    }
}