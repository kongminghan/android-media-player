package com.minghan.lomotif.media.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(
    @StringRes text: Int,
    duration: Int = Toast.LENGTH_LONG
) {
    Toast.makeText(this, text, duration).show()
}
