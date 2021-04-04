package io.github.mklkj.androidappwidgetexample.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Titles(
    @Json(name = "canonical")
    val canonical: String,
    @Json(name = "normalized")
    val normalized: String,
    @Json(name = "display")
    val display: String
)