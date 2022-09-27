package com.example.bitbd.ui.fragment.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.ERROR
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.activity.login.model.UserLogIn
import com.example.bitbd.ui.fragment.profile.model.BaseProfileUpdate
import com.example.bitbd.util.BitBDUtil
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.Path

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text


    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _updateProfile = MutableLiveData<BaseProfileUpdate>()
    val updateProfile: LiveData<BaseProfileUpdate>
        get() = _updateProfile

    fun updateProfileWithImage(
        name: RequestBody,
        email: RequestBody,
        phone: RequestBody,
        password: RequestBody,
        passwordConform: RequestBody,
        slug: String,
        part: MultipartBody.Part, context: Context
    ) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.updateProfile(name, email,phone, password, passwordConform, slug, part)
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to update info", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _updateProfile.value = response.body()
                _progress.value = false
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }
            }
        }

    }


    fun updateProfileWithoutImage(
        name: String,
        email: String,
        phone: String,
        password: String,
        passwordConform: String,
        slug: String,
        context: Context
    ) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.updateProfile(name, email,phone, password, passwordConform, slug)
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to update info", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _updateProfile.value = response.body()
                _progress.value = false
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }
            }
        }

    }
}