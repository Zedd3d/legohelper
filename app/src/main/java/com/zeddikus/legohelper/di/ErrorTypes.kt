package com.zeddikus.legohelper.di

sealed interface ErrorTypes {
    data object NoNetwork: ErrorTypes

    data object Unknown: ErrorTypes

    data object NoData: ErrorTypes
}