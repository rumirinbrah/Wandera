package com.zzz.feature_trip.overview.domain

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.zzz.data.trip.model.ExpenseEntity
import com.zzz.feature_trip.R

data class ExpenseEntityUI(
    val id : Long,
    val amount : Int = 0,
    val title : String? = null,
    val splitInto : Int? = null,
    val expenseType : String = "other",
    @DrawableRes
    val icon : Int,
    val iconBackground : Int,
    val timestamp : Long
)

fun ExpenseEntity.toUIEntity() : ExpenseEntityUI{
    return ExpenseEntityUI(
        id = id,
        amount = amount,
        title = title,
        splitInto = splitInto,
        expenseType = expenseType,
        icon = expenseType.toDrawableRes(),
        iconBackground = getIconBackground(expenseType),
        timestamp = timestamp
    )
}

private fun String.toDrawableRes():Int{
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
private fun getIconBackground(shortTitle : String):Int{
    return when(shortTitle) {
        "ff" -> {
            Color(0xFFCEBE36).toArgb()
        }
        "fl" -> {
            Color(0xFFCE382D).toArgb()
        }
        "tr" -> {
            Color(0xFFD79637).toArgb()
        }
        "sh" -> {
            Color(0xFF2EA4D9).toArgb()
        }
        "ht" -> {
            Color(0xFF348CD2).toArgb()
        }
        "med" -> {
            Color(0xFF29BD2F).toArgb()

        }
        "rt" -> {
            Color(0xFF3639CE).toArgb()
        }
        else -> Color(0xFFB08C2C).toArgb()

    }
}
