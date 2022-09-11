package com.example.bitbd.constant

import com.example.bitbd.network.ApiClient
import com.example.bitbd.network.ApiInterface


var NetworkCall = ApiClient().getApiClient()?.create(ApiInterface::class.java)
const val AUTH_TOKEN = "auth-token"
const val USERNAME = "user-id-email"
const val FIRST_TIME = "first-time"
const val LANGUAGE = "language"
const val SERVICE_TYPE = "service-details-type"
const val SERVICE_TYPE_DETAILS = "service-type-details"
const val AUTH = "Authorization"
const val BEARER = "bearer "
const val MOBILE_NUMBER = "mobile-number"
const val NAME = "name"








