package com.example.bitbd.ui.activity.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.ERROR
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.activity.login.model.LogInResponse
import com.example.bitbd.ui.activity.login.model.UserLogIn
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInViewModel : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress


    private val _userLogin = MutableLiveData<LogInResponse>()
    val userLogin: LiveData<LogInResponse>
        get() = _userLogin



    fun login(userLogin: UserLogIn , context: Context) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.userLogin(userLogin)
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to log in", ERROR)
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                 BitBDUtil.showMessage("Login successfully", SUCCESS)
                _userLogin.value = response.body()
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