package com.example.bitbd.ui.activity.main.mainViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.ERROR
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.activity.main.model.LogOutResponse
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _progressLogOut = MutableLiveData<Boolean>()
    val progressLogOut: LiveData<Boolean>
        get() = _progressLogOut




    suspend fun logOut(context : Context){
        _progressLogOut.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.userLogOut()
                }

            } catch (e: Exception) {
                 BitBDUtil.showMessage("Unable to log out", ERROR)
                _progressLogOut.value = false
                return@launch
            }

            if (response?.code()== 200 ) {
                _progressLogOut.value = false
            } else {
                if ( response?.code()== 401) {
                    _progressLogOut.value = false
                }
                else{
                    response?.let { Log.d("doneValue :: ", it.message()) }
                }
            }
        }
    }
}