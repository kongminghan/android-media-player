package com.minghan.lomotif.media.extension

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.util.ArrayList
import kotlin.math.roundToInt

fun Context.checkPermissions(
    permissions: Array<String>,
    onGranted: () -> Unit = { },
    onDenied: (deniedPermissions: ArrayList<String>?) -> Unit = { },
    onBlocked: (blockedPermissions: ArrayList<String>?) -> Unit = { },
    onJustBlocked: (justBlockedList: ArrayList<String>?, deniedPermissions: ArrayList<String>?) -> Unit = { _, _ -> }
) {
    Permissions.check(this, permissions, null, null, object : PermissionHandler() {
        override fun onGranted() {
            onGranted()
        }

        override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
            onDenied(deniedPermissions)
            return super.onDenied(context, deniedPermissions)
        }

        override fun onBlocked(context: Context?, blockedList: ArrayList<String>?): Boolean {
            onBlocked(blockedList)
            return super.onBlocked(context, blockedList)
        }

        override fun onJustBlocked(
            context: Context?,
            justBlockedList: ArrayList<String>?,
            deniedPermissions: ArrayList<String>?
        ) {
            onJustBlocked(justBlockedList, deniedPermissions)
            super.onJustBlocked(context, justBlockedList, deniedPermissions)
        }
    })
}

fun Context.dpToPx(dp: Int): Float {
    val displayMetrics = this.resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt().toFloat()
}

val Context.screenWidth: Int
    get() {
        var screenWidth = 0
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        wm?.let {
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(metrics)
            screenWidth = metrics.widthPixels
        }
        return screenWidth
    }

val Context.statusBarHeight: Int
    get() {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }
