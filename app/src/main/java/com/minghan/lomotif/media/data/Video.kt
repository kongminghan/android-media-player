package com.minghan.lomotif.media.data

import android.graphics.Bitmap

data class Video(
    val id: Long,
    val title: String,
    val path: String,
    val duration: String,
    val albumThumb: Bitmap?
)
