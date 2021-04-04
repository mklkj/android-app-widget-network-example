package io.github.mklkj.androidappwidgetexample.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Namespace(
    @Json(name = "id")
    val id: Int,
    @Json(name = "text")
    val text: String
)