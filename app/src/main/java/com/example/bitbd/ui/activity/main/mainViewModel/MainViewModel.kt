package com.example.bitbd.ui.activity.main.mainViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.NetworkCall
import com.example.bitbd.constant.BEARER
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.util.BitBDUtil
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException

class MainViewModel : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress


    private val _userLogOut = MutableLiveData<JSONObject>()
    val userLogOut: LiveData<JSONObject>
        get() = _userLogOut


    fun logOut(context : Context){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    NetworkCall?.userLogOut(BEARER + BitBDPreferences(context).getAuthToken())
                }

            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            } catch (e: Exception) {
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _userLogOut.value = response.body()
                _progress.value = false
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }
                BitBDUtil.showMessage("Something wrong, Unable to log out",context)
            }
        }
    }
}