package com.zzz.feature_trip.overview.domain

import androidx.annotation.DrawableRes
import com.zzz.data.trip.model.ExpenseEntity
import com.zzz.feature_trip.R

internal data class ExpenseType(
    @DrawableRes val icon: Int ,
    val title: String = "Other" ,
    val shortTitle: String = "other"
)

private fun String.getDrawableResource(): Int {
    return when(this) {
        "ff" -> {
            R.drawable.fastfood
        }
        "fl" -> {
            R.drawable.flight
        }
        "tr" -> {
            R.drawable.taxi
        }
        "sh" -> {
            R.drawable.shopping_bag
        }
        "ht" -> {
            R.drawable.hotel
        }
        "med" -> {
            R.drawable.hospital
        }
        "rt" -> {
            R.drawable.cycle
        }
        else -> R.drawable.dollar
    }
}

internal val expenseTypes by lazy {
    listOf(
        ExpenseType(
            R.drawable.fastfood ,
            "Food" ,
            shortTitle = "ff"
        ) ,
        ExpenseType(
            R.drawable.flight ,
            "Flight" ,
            shortTitle = "fl"
        ) ,
        ExpenseType(
            R.drawable.taxi ,
            "Transport" ,
            shortTitle = "tr"
        ) ,
        ExpenseType(
            R.drawable.shopping_bag ,
            "Shopping" ,
            shortTitle = "sh"
        ) ,
        ExpenseType(
            R.drawable.hotel ,
            "Hotel" ,
            shortTitle = "ht"
        ) ,
        ExpenseType(
            R.drawable.hospital ,
            "Medic" ,
            shortTitle = "med"
        ) ,
        ExpenseType(
            R.drawable.cycle ,
            "Rent" ,
            shortTitle = "rt"
        ) ,
        ExpenseType(
            R.drawable.dollar ,
            "Other" ,
            shortTitle = "other"
        )
    )
}
