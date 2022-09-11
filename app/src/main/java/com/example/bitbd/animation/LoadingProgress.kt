package com.example.bitbd.animation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.core.content.ContextCompat
import com.example.bitbd.R
import com.example.bitbd.databinding.CustomDialogLoadingBinding
import com.example.bitbd.sharedPref.BitBDPreferences



class  LoadingProgress(context: Context) : Dialog(context) {

    init {
        setCancelable(false)
    }
    private lateinit var binding: CustomDialogLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = CustomDialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(BitBDPreferences(context).getAuthToken().isNullOrEmpty()){
            binding.animationView.setAnimation(R.raw.anim_loader)
        }
        else binding.animationView.setAnimation(R.raw.others_load)

    }
}