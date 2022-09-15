package com.example.bitbd.sharedPref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.bitbd.constant.*


class BitBDPreferences(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun putAuthToken(token: String?) {
        Log.d(PREFERENCE_TITLE, "putAuthToken: $token")
        editor.putString(AUTH_TOKEN, token)
        Log.d(PREFERENCE_TITLE, "putAuthToken: $token")
        editor.apply()
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
    }

    fun putStringSet(prefKeyCookies: String, cookies: HashSet<String>) {


    }

    companion object {
        private const val PREFERENCE_TITLE = "bit-bd-Preference"
    }

}