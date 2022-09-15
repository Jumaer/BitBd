package com.example.bitbd.ui.fragment.deposit

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.fragment.deposit.model.BaseDepositResponse
import com.example.bitbd.ui.fragment.deposit.model.DepositSubmit
import com.example.bitbd.ui.fragment.deposit.model.GetPaymentBaseResponse
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class DepositViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is deposit Fragment"
    }
    val text: LiveData<String> = _text


    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress


    private val _deposit = MutableLiveData<BaseDepositResponse>()
    val deposit: LiveData<BaseDepositResponse>
        get() = _deposit


    suspend fun deposit(context: Context) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.deposit()
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to show anything", context)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _deposit.value = response.body()
                _progress.value = false
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }

    private val _payment = MutableLiveData<GetPaymentBaseResponse>()
    val payment: LiveData<GetPaymentBaseResponse>
        get() = _payment

    suspend fun getPaymentInfo(context: Context) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.getPaymentInfo()
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to get payment info", context)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _payment.value = response.body()
                _progress.value = false
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                    BitBDUtil.showMessage(response.message(), context)
                }
            }
        }
    }

    private val _depositSubmit = MutableLiveData<DepositSubmit>()
    val depositSubmit: LiveData<DepositSubmit>
        get() = _depositSubmit

    suspend fun depositSubmit(
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
                    networkCall(context)?.depositDataStore(account, method_id, trx_id, amount, part)
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to show anything", context)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _depositSubmit.value = response.body()
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