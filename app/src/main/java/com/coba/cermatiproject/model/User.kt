package com.coba.cermatiproject.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean,
    @SerializedName("items")
    var items: MutableList<Item>,
    @SerializedName("total_count")
    var totalCount: Int
)