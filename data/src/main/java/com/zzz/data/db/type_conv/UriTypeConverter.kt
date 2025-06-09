package com.zzz.data.db.type_conv

import android.net.Uri
import android.util.Log
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

    @TypeConverter
    fun uriToString(uri: Uri?) : String?{
        Log.d("typeConv" , "uriToString : Image uri is ${uri?.toString()}")

        return uri?.toString()
    }
    @TypeConverter
    fun stringToUri(string : String?) : Uri?{
        return try {
            Log.d("typeConv" , "stringToUri : Uri string is $string")

            string?.let {
                Uri.parse(string)
            }

        }catch (e : Exception){
            null
        }
    }

}