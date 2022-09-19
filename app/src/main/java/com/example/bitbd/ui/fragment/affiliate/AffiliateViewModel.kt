package com.example.bitbd.ui.fragment.affiliate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AffiliateViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Affiliate Fragment"
    }
    val text: LiveData<String> = _text
}