package com.zeddikus.legohelper.di

object ApiConstants {
    const val BASE_URL = "https://www.bricklink.com"
    const val IMG_URL = "https://img.bricklink.com"
    const val CALL_TIMEOUT = 30
    const val READ_TIMEOUT = 30

    const val SUCCESS_CODE = 200
    const val NO_INTERNET_CONNECTION_CODE = -1
    const val BAD_REQUEST_CODE = 400
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val CONFLICT = 409
    const val UNPROCESSABLE_ENTITY = 422
    const val INTERNAL_SERVER_ERROR = 500
    const val BAD_GATEWAY = 502
}
