package com.example.koronaapp.network

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.koronaapp.Model.ItemModel
import com.google.gson.Gson

@ProvidedTypeConverter
class Converter(private val gson: Gson) {
    @TypeConverter
    fun listToJson(value: ArrayList<ItemModel>): String? {
        return gson.toJson(value)
    }
    @TypeConverter
    fun jsonToList(value: String): ArrayList<ItemModel> {
        return gson.fromJson(value, Array<ItemModel>::class.java).toList() as ArrayList<ItemModel>
    }
}
