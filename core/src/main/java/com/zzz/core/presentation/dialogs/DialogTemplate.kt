package com.zzz.core.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zzz.core.presentation.modifiers.generalDialogProperties

/**
 * General template for creating dialogs
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
                .generalDialogProperties(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            content()
        }
    }
}