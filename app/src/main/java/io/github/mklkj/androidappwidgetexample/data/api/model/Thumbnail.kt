package io.github.mklkj.androidappwidgetexample.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Thumbnail(
    @Json(name = "source")
    val source: String,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int
)