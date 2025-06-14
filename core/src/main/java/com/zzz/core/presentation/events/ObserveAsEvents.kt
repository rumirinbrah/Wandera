package com.zzz.core.presentation.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T> ObserveAsEvents(
    events : Flow<T> ,
    key1 : Any? = null ,
    key2 : Any? = null ,
    onEvent : (T) ->Unit
) {
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(lifecycle.lifecycle, key1 , key2) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                events.collect(onEvent)
            }
        }
    }

}