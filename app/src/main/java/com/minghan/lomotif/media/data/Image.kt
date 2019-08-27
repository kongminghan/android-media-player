package com.minghan.lomotif.media.data

data class Image(
    val id: Int?,
    val largeImageURL: String?,
    val previewURL: String?,
    val views: Int?,
    val likes: Int?,
    val favorites: Int?,
    val user: String?,
    val userImageURL: String?,
    val tags: String?,
    val imageSize: Long
) {
    class ImageJsonResponse(
        val hits: List<Image>?
    )
}
