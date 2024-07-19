package com.zeddikus.legohelper.base

import androidx.fragment.app.Fragment

interface NavEvent {
    fun navigate(fragment: Fragment?)
}
