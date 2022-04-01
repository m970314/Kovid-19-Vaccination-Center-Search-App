package com.example.koronaapp.Model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemModel(
    @SerializedName(value = "id")
    @Expose
    val id: Int,

    @SerializedName(value = "sido")
    @Expose
    val sido: String,

    @SerializedName(value = "sigungu")
    @Expose
    val sigungu: String,

    @SerializedName(value = "facilityName")
    @Expose
    val facilityName: String,

    @SerializedName(value = "zipCode")
    @Expose
    val zipCode: String,

    @SerializedName(value = "createdAt")
    @Expose
    val createdAt: String,

    @SerializedName(value = "centerType")
    @Expose
    val centerType: String,

    @SerializedName(value = "org")
    @Expose
    val org: String,

    @SerializedName(value = "address")
    @Expose
    val address: String,

    @SerializedName(value = "centerName")
    @Expose
    val centerName: String,

    @SerializedName(value = "phoneNumber")
    @Expose
    val phoneNumber: String,

    @SerializedName(value = "updatedAt")
    @Expose
    val updatedAt: String,

    @SerializedName(value = "lat")
    @Expose
    val latitude: String,

    @SerializedName(value = "lng")
    @Expose
    val longitude: String,
)