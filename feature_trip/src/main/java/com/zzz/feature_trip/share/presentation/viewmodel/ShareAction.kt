package com.zzz.feature_trip.share.presentation.viewmodel

import android.net.Uri

internal sealed class ShareAction {

    data class ExportTrip(val tripId : Long) : ShareAction()

    data class ExportTripJsonFromUri(val jsonUri : Uri) : ShareAction()

}