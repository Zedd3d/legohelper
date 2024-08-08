package com.zeddikus.legohelper.utils

import android.view.View
import android.view.ViewGroup


fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
    view.isEnabled = enabled
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val child = view.getChildAt(i)
            setViewAndChildrenEnabled(child, enabled)
        }
    }
}