package com.zzz.feature_trip.share.data.repo

import com.zzz.core.domain.result.Result
import com.zzz.feature_trip.overview.domain.ExpenseEntityUI
import com.zzz.feature_trip.share.domain.result.ExportError
import com.zzz.feature_trip.share.domain.result.ShareProgress
import com.zzz.feature_trip.share.domain.source.ShareExpenseSource

class ShareExpenseManager : ShareExpenseSource {


    /**
     * Converts the list of expenses to a formatted string and returns it via ShareProgress
     *
     * Note that for now, the result will always be `ShareProgress` and never `ExportError`.
     *
     * Format - Title $250 , Split into : 3 ( 83 per head )
     */
    override fun convertExpensesToString(expenses: List<ExpenseEntityUI>): Result<ShareProgress , ExportError> {
        val expensesString = buildString {
            for(expense in expenses){

                append(expense.title ?: "Untitled")
                append(" ")
                append("${expense.currencySymbol}${expense.amount}")

                append(" , ")

                expense.splitInto?.let {
                    val perHead = expense.amount/it
                    append("Split into : $it ")
                    append("( $perHead per head )")
                }
                appendLine()
            }
        }
        return Result.Success(ShareProgress(done = true, expensesString = expensesString))
    }

}