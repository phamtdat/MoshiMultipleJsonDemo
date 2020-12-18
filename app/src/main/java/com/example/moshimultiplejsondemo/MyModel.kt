package com.example.moshimultiplejsondemo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyModel(
    @Json(name = "Id") val Id: Long,
    @Json(name = "image") val images: List<String>
)
