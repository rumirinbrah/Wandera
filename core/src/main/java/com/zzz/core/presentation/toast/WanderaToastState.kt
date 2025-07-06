package com.zzz.core.presentation.toast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.zzz.core.theme.blueToastSweep

class WanderaToastState(
) {

    var visible by mutableStateOf(false)
        private set

    var message by mutableStateOf("")
        private set

    var sweepColor by mutableStateOf(blueToastSweep)
        private set


    /**
     * @param message - Toast message to be displayed
     * @param sweepColor - Color that sweeps across the toast when shown
     */
    fun showToast(message: String , sweepColor: Color = blueToastSweep) {
        this.message = message
        this.sweepColor = sweepColor
        visible = true
    }

    /**
     * Dismiss the toast
     */
    fun dismiss() {
        message = ""
        visible = false
        //sweepColor = blueToastSweep
    }

}
