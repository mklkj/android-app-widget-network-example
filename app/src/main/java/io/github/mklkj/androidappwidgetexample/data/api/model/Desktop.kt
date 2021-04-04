package io.github.mklkj.androidappwidgetexample.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Desktop(
    @Json(name = "page")
    val page: String,
    @Json(name = "revisions")
    val revisions: String,
    @Json(name = "edit")
    val edit: String,
    @Json(name = "talk")
    val talk: String
)