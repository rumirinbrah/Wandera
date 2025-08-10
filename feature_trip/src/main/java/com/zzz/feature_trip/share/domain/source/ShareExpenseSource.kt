package com.zzz.feature_trip.share.domain.source

import com.zzz.core.domain.result.Result
import com.zzz.feature_trip.overview.domain.ExpenseEntityUI
import com.zzz.feature_trip.share.domain.result.ExportError
import com.zzz.feature_trip.share.domain.result.ShareProgress

interface ShareExpenseSource {

    fun convertExpensesToString(expenses : List<ExpenseEntityUI>) : Result<ShareProgress,ExportError>

}