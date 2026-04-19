package com.zeddikus.legohelper.di

sealed interface ErrorTypes {
    data object NoNetwork: ErrorTypes

    data class Unknown(val code: Int? = null): ErrorTypes

    data object NoData: ErrorTypes
}