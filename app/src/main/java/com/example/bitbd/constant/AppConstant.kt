package com.example.bitbd.constant

import android.content.Context
import com.example.bitbd.network.ApiClient
import com.example.bitbd.network.ApiInterface






fun networkCall(context : Context) : ApiInterface? {
  return  ApiClient().getApiClient(context)?.create(ApiInterface::class.java)
}

const val AUTH_TOKEN = "auth-token"
const val USERNAME = "user-id-email"
const val FIRST_TIME = "first-time"
const val LANGUAGE = "language"
const val SERVICE_TYPE = "service-details-type"
const val SERVICE_TYPE_DETAILS = "service-type-details"
const val AUTH = "Authorization"
const val BEARER = "Bearer bearer "
const val COOKIE = "Cookie"
const val PREF_KEY_COOKIES = "Cookie-List"
const val TYPE_TOKEN_COOKIE = "XSRF-TOKEN"
const val ACCEPT = "Accept"
const val CONTENT_TYPE = "Content-Type"
const val APPLICATION_JSON = "application/json"
const val CACHE_CONTROL = "Cache-Control"
const val MOBILE_NUMBER = "mobile-number"
const val NAME = "name"








