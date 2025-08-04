package com.zzz.feature_trip.overview.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.data.trip.model.UserDocument

@Composable
internal fun UserDocsList(
    documents : List<UserDocument>,
    collapsed : Boolean = true,
    onCollapseChange : ()->Unit,
    modifier: Modifier = Modifier
) {
    val rotation = animateFloatAsState(
        targetValue = if(collapsed) 0f else 180f,
        animationSpec = tween(500)
    )

    Column(
        modifier
            .fillMaxWidth(),
        //verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                "Your documents" ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
            )
            IconButton(
                onClick = {
                    onCollapseChange()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = if(collapsed) {
                        "Show checklist"
                    }else{
                        "hide checklist"
                    },
                    modifier = Modifier.rotate(rotation.value)
                )
            }
        }

        AnimatedVisibility(!collapsed) {
            VerticalSpace(8.dp)
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp) ,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    documents ,
                    key = {
                        it.id
                    }
                ) { doc ->
                    OverviewDocumentCard(
                        doc
                    )
                }
            }
        }

    }

}