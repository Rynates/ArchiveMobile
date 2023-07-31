package com.example.helper.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.helper.domen.models.Fund
import com.example.helper.domen.models.RelativeResponce
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun toMeaningJson(meaning: List<RelativeResponce>): String {
        return Gson().toJson(
            meaning,
            object : TypeToken<ArrayList<RelativeResponce>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromMeaningsJson(json: String): List<RelativeResponce> {
        return Gson().fromJson<ArrayList<RelativeResponce>>(
            json,
            object : TypeToken<ArrayList<RelativeResponce>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun saveAddressList(listOfString: List<Fund?>?): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun getAddressList(listOfString: String?): List<Fund?>? {
        return Gson().fromJson(
            listOfString,
            object : TypeToken<List<String?>?>() {}.type
        )
    }

    @TypeConverter
    fun toListOfStrings(flatStringList: String): List<String> {
        return flatStringList.split(",")
    }

    @TypeConverter
    fun fromListOfStrings(listOfString: List<String>): String {
        return listOfString.joinToString(",")
    }
}