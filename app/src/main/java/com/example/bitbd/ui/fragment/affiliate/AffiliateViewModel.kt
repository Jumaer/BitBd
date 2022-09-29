package com.example.bitbd.ui.fragment.affiliate

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.ERROR
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.fragment.affiliate.model.BaseAffiliateResponse
import com.example.bitbd.ui.fragment.transaction.model.BaseTransactionResponse
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AffiliateViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Affiliate Fragment"
    }
    val text: LiveData<String> = _text


    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress


    private val _baseAffiliate = MutableLiveData<BaseAffiliateResponse>()
    val baseAffiliate: LiveData<BaseAffiliateResponse>
        get() = _baseAffiliate


    suspend fun baseAffiliationCall(context : Context){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.getAffiliateData()
                }

            }  catch (e: Exception) {
                 BitBDUtil.showMessage("Unable to show anything", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _baseAffiliate.value = response.body()
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