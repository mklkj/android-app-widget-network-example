package io.github.mklkj.androidappwidgetexample.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentUrls(
    @Json(name = "desktop")
    val desktop: Desktop,
    @Json(name = "mobile")
    val mobile: Mobile
)