package com.zzz.core.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zzz.core.presentation.components.DotsLoadingAnimation
import com.zzz.core.theme.WanderaTheme

/**
 * @author zyzz
 */
@Composable
fun LoadingDialog(
    title : String? = null,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Column (
            modifier.clip(Shapes().large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            title?.let {
                Text(title)
            }
            DotsLoadingAnimation()
        }
    }
}

@Preview
@Composable
private fun IDK() {
    WanderaTheme {
        LoadingDialog()
    }
}