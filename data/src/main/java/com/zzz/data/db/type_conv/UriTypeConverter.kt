package com.zzz.data.db.type_conv

import android.net.Uri
import androidx.room.TypeConverter


class UriTypeConverter {

    @TypeConverter
    fun uriListToString(list : List<Uri>) : String{
        return list.joinToString(","){
            it.toString()
        }
    }

    @TypeConverter
    fun stringToUriList(stringList : String) : List<Uri>{
        return stringList.split(",").map {uriString->
            Uri.parse(uriString)
        }
    }

}