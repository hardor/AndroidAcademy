package ru.profapp.androidacademy.Data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MultimediumDTO {

    @SerializedName("url")
    var url: String? = null
    @SerializedName("format")
    var format: String? = null
    @SerializedName("height")
    var height: Int? = null
    @SerializedName("width")
    var width: Int? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("subtype")
    var subtype: String? = null
    @SerializedName("caption")
    var caption: String? = null
    @SerializedName("copyright")
    var copyright: String? = null

}
