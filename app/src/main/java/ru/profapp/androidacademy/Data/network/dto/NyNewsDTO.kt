package ru.profapp.androidacademy.Data.network.dto

import com.google.gson.annotations.SerializedName

class NyNewsDTO {

    @SerializedName("status")
    var status: String? = null
    @SerializedName("copyright")
    var copyright: String? = null
    @SerializedName("section")
    var section: String? = null
    @SerializedName("last_updated")
    var lastUpdated: String? = null
    @SerializedName("num_results")
    var numResults: Int? = null
    @SerializedName("results")
    var results: List<ResultDTO>? = null

}


