package com.minghan.lomotif.media.data

import android.net.Uri

data class Music (
    val id: Long,
    val albumName: String,
    val albumArtUri: Uri,
    val title: String,
    val path: String,
    val artist: String,
    val duration: String
)
