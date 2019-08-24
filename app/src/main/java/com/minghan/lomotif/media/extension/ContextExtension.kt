package com.minghan.lomotif.media.extension

import android.content.Context
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.util.ArrayList

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