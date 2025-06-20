package com.zzz.core.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * @author zyzz
 */
@Composable
fun DialogTemplate(
    onDismiss : ()->Unit,
    modifier: Modifier = Modifier,
    content : @Composable() (ColumnScope.()->Unit)
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            content()
        }
    }
}