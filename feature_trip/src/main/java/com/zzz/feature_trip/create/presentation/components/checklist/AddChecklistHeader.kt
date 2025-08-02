package com.zzz.feature_trip.create.presentation.components.checklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.buttons.ElevatedIconTextButton
import com.zzz.core.presentation.modifiers.customShadow

@Composable
internal fun AddChecklistHeader(
    onAdd : ()->Unit,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row (
            modifier.fillMaxWidth()
                ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Create a Checklist",
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,)
            ElevatedIconTextButton(
                icon = com.zzz.core.R.drawable.download_done ,
                text = "Add" ,
                onClick = onAdd,
                modifier = Modifier.customShadow(
                    color = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
        Text(
            "(Don't have it all figured out yet?? Fear not! You can always add more later)",
            //textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}