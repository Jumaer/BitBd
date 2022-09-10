package com.example.bitbd.sharedPref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.bitbd.constant.*
import com.example.bitbd.network.ApiClient
import com.example.bitbd.network.RetrofitApiCallCreation
import com.example.bitbd.network.RetrofitApiCallCreation.setApiAuthClient


class BitBDPreferences(context: Context) {
    private val contextVal = context
    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun putAuthToken(token: String?) {
        Log.d(PREFERENCE_TITLE, "putAuthToken: $token")
        var newToken = token?.drop(2)
        newToken = newToken?.dropLast(1)
        editor.putString(AUTH_TOKEN, newToken)
        Log.d(PREFERENCE_TITLE, "putAuthToken: $newToken")
        editor.apply()
        if(token.isNullOrEmpty()){
            RetrofitApiCallCreation.setSession(false)
            return
        }
        setAuthClient()
    }

    private fun setAuthClient(){
        ApiClient().getAuthApiClient(contextVal)?.let { setApiAuthClient(it) }
    }

    fun getAuthToken(): String? {
        Log.d(PREFERENCE_TITLE, "getAuthToken: ${preferences.getString(AUTH_TOKEN, "")}")
        return preferences.getString(AUTH_TOKEN, "")
    }

    fun putMobileNumber(mobileNumber: String?) {
        editor.putString(MOBILE_NUMBER, mobileNumber)
        editor.apply()
    }

    fun getMobileNumber(): String? {
        return preferences.getString(MOBILE_NUMBER, "")
    }

    fun putFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean(FIRST_TIME, isFirstTime)
        editor.apply()
    }

    fun isFirstTimeLaunch(): Boolean {
        return preferences.getBoolean(FIRST_TIME, true)
    }

    fun getSelectedLanguage(): String? {
        return preferences.getString(LANGUAGE, "")
    }

    fun putSelectedLanguage(language: String) {
        editor.putString(LANGUAGE, language)
        editor.apply()
    }
    fun putName(name : String){
        editor.putString(NAME, name)
        editor.apply()
    }
    fun getName(): String?{
        return preferences.getString(NAME, "")
    }
    fun logOut(){
        editor.remove(MOBILE_NUMBER)
        editor.remove(AUTH_TOKEN)
        editor.remove(NAME)
        editor.apply()
        RetrofitApiCallCreation.setSession(false)
    }

    companion object {
        private const val PREFERENCE_TITLE = "bit-bd-Preference"
    }

}