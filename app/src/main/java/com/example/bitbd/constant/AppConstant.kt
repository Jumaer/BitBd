package com.example.bitbd.constant

import android.content.Context
import com.example.bitbd.network.ApiClient
import com.example.bitbd.network.ApiInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull


fun networkCall(context : Context) : ApiInterface? {
  return  ApiClient().getApiClient(context)?.create(ApiInterface::class.java)
}

const val AUTH_TOKEN = "auth-token"
const val EMAIL = "user-id-email"
const val FIRST_TIME = "first-time"
const val LANGUAGE = "language"
const val SERVICE_TYPE = "service-details-type"
const val SERVICE_TYPE_DETAILS = "service-type-details"
const val AUTH = "Authorization"
const val BEARER = "Bearer bearer "
const val ACCEPT = "Accept"
const val CONTENT_TYPE = "Content-Type"
const val APPLICATION_JSON = "application/json"
const val MOBILE_NUMBER = "mobile-number"
const val NAME = "name"
const val IMAGE_URL = "image-url"
const val AFFILIATE_STATUS = "affiliate"
const val AFFILIATE_CODE = "affiliate-code"
const val USERNAME = "username"
val MEDIA_TYPE = "text/plain".toMediaTypeOrNull();
const val MESSAGE = "message"
const val TIME_CREATED = "created-time"
const val TIME_UPDATED = "updated-time"
const val SLUG = "slug"
const val BALANCE = "balance"
const val DEPOSIT = "Deposit-item"
const val MIN_DEPOSIT = "Deposit-minimum"
const val RATE_DEPOSIT = "Deposit-rate"
const val IS_LIST_UPDATED = "updated-deposit"
const val IS_LIST_UPDATED_WITHDRAW = "updated-withdraw"
const val RECALL_WITHDRAW = "updated-withdraw-recall"
const val IS_LIST_UPDATED_ACCOUNT = "updated-Account"
const val Edit_INFO_OBJECT = "edit-Info-Object"
const val MOBILE_STATUS = "mobile-status"
const val OTP_PHONE  = "otp-phone"
const val SUCCESS = "success"
const val INFO = "info"
const val WARNING = "warning"
const val ERROR = "error"








