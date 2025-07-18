package com.zzz.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A container that wraps content inside a Box with ime padding. Can be used to avoid keyboard overlap with content
 */
@Composable
fun KeyboardAware(
    modifier: Modifier = Modifier,
    content : @Composable ()->Unit
) {
    Box(
        modifier
            .imePadding()
    ){
        content()
    }
}