package io.github.mklkj.androidappwidgetexample.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikipediaPageSummary(
    @Json(name = "type")
    val type: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "displaytitle")
    val displaytitle: String,
    @Json(name = "namespace")
    val namespace: Namespace,
    @Json(name = "wikibase_item")
    val wikibaseItem: String,
    @Json(name = "titles")
    val titles: Titles,
    @Json(name = "pageid")
    val pageid: Int,
    @Json(name = "thumbnail")
    val thumbnail: Thumbnail?,
    @Json(name = "originalimage")
    val originalimage: Originalimage,
    @Json(name = "lang")
    val lang: String,
    @Json(name = "dir")
    val dir: String,
    @Json(name = "revision")
    val revision: String,
    @Json(name = "tid")
    val tid: String,
    @Json(name = "timestamp")
    val timestamp: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "description_source")
    val descriptionSource: String?,
    @Json(name = "content_urls")
    val contentUrls: ContentUrls,
    @Json(name = "extract")
    val extract: String,
    @Json(name = "extract_html")
    val extractHtml: String
)
