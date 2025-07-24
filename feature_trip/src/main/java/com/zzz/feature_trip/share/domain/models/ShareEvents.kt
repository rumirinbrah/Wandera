package com.zzz.feature_trip.share.domain.models

import android.content.Intent
import com.zzz.core.presentation.events.UIEvents

internal sealed interface ShareEvents {

    data class IntentGenerated(val shareIntent : Intent) : ShareEvents

}