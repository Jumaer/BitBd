package com.example.bitbd.ui.fragment.otp_verify

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.ERROR
import com.example.bitbd.constant.networkCall
import com.example.bitbd.util.BitBDUtil
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OTPViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is otp View Model"
    }
    val text: LiveData<String> = _text

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _sendOtp = MutableLiveData<JsonObject>()
    val sendOtp: LiveData<JsonObject>
        get() = _sendOtp


    suspend fun requestForOtp(context: Context,phone : String){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.sendOTP(phone)
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to update info", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _sendOtp.value = response.body()
                _progress.value = false
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }
            }
        }
    }

    private val _submitOtp = MutableLiveData<JsonObject>()
    val submitOtp: LiveData<JsonObject>
        get() = _submitOtp

    suspend fun submitOTPToVerify(context: Context,otp : String){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.submitOTP(otp)
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to update info", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _submitOtp.value = response.body()
                _progress.value = false
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                    if(response.code()== 404){
                        BitBDUtil.showMessage("Invalid OTP, Plz try again",
                            ERROR)
                    }
                    else BitBDUtil.showMessage("OTP verify failed",
                        ERROR)

                }
            }
        }
    }
}