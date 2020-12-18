package com.example.moshimultiplejsondemo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// tell codegen to not generate adapter, as we provide our own custom adapter
@JsonClass(generateAdapter = false)
data class MyModel(
    @Json(name = "Id") val Id: Long,
    @Json(name = "image") val images: List<String>
)
