package com.example.koronaapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity
data class HospitalInfoResponse(
    @Expose
    @SerializedName(value = "data")
    val itemModelList: ArrayList<ItemModel>,

    @Expose
    @PrimaryKey(autoGenerate = true)
    @SerializedName(value = "page")
    var page: Int ,

    @Expose
    @SerializedName(value = "perPage")
    val perPage: Int,

    @Expose
    @SerializedName(value = "totalCount")
    val totalCount: Int,

    @Expose
    @SerializedName(value = "currentCount")
    val currentCount: Int
)