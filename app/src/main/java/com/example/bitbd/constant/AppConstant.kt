package com.example.bitbd.constant
import com.example.bitbd.BuildConfig
import com.example.bitbd.network.RetrofitApiCallCreation


var NetworkCall = RetrofitApiCallCreation.getCallingType()
const val AUTH_TOKEN = "auth-token"
const val USERNAME = "user-id-email"
const val FIRST_TIME = "first-time"
const val LANGUAGE = "language"
const val SERVICE_TYPE = "service-details-type"
const val SERVICE_TYPE_DETAILS = "service-type-details"
const val AUTH = "Authorization"
const val TOKEN = "Bearer "
const val MOBILE_NUMBER = "mobile-number"
const val NAME = "name"
var BASE_URL = BuildConfig.SERVER_URL









