package com.example.bitbd.ui.activity.submit

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.ERROR
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.fragment.deposit.model.DepositSubmit
import com.example.bitbd.util.BitBDUtil
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class SubmitDepositViewModel : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress


    private val _deposit = MutableLiveData<DepositSubmit>()
    val deposit: LiveData<DepositSubmit>
        get() = _deposit


    suspend fun submitDeposit(
        context: Context,
        account: RequestBody,
        method_id: RequestBody,
        trx_id: RequestBody,
        amount: RequestBody,
        part: MultipartBody.Part

    ) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.submitDepositWithImage(account, method_id, trx_id, amount, part)
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to show anything", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _deposit.value = response.body()
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }


}