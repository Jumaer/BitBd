package com.example.bitbd.ui.fragment.otp_verify

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.INFO
import com.example.bitbd.constant.OTP_PHONE
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.constant.WARNING
import com.example.bitbd.databinding.FragmentOtpInputBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.activity.login.LogInActivity
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch


class OtpInputFragment : Fragment() {

    private var _binding: FragmentOtpInputBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var phoneNumber = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phoneNumber = it.getString(OTP_PHONE).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this)[OTPViewModel::class.java]
        // Inflate the layout for this fragment
        _binding = FragmentOtpInputBinding.inflate(inflater, container, false)
        val root: View = binding.root
        countDownForNextOtp()
        binding.submitOtpButton.setOnClickListener {
              submitOtp(viewModel)
        }

        binding.countDownResendOtp.setOnClickListener {
            if (resendOTPEnable == true) {
                resendOTP(viewModel)
            }
        }

        var loading: LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(requireContext())
                } else {
                    loading?.dismiss()

                }
            }
        }

        return root
    }

    private fun submitOtp(viewModel: OTPViewModel) {
        val OTP = binding.otpLayout.text.toString().trim()

        if(OTP.isEmpty()){
            BitBDUtil.showMessage("Enter 5 Digit OTP", WARNING)
            return
        }
        if(OTP.length !=5){
            BitBDUtil.showMessage("OTP is 5 Digit", WARNING)
            return
        }


        viewModel.submitOtp.observe(viewLifecycleOwner){
            if(it != null){
                BitBDUtil.showMessage(it.get("message").asString, SUCCESS)
                BitBDPreferences(requireContext()).logOut()
                startActivity(Intent(requireContext(),LogInActivity::class.java))
                requireActivity().finish()
            }
        }

        lifecycleScope.launch {
            viewModel.submitOTPToVerify(requireContext(),OTP)
        }

    }

    private fun resendOTP(viewModel: OTPViewModel) {

        viewModel.sendOtp.observe(viewLifecycleOwner) {
            if (it != null) {
                BitBDUtil.showMessage("Please don't press back button", WARNING)
                countDownForNextOtp()
            }
        }


        lifecycleScope.launch {
            viewModel.requestForOtp(requireContext(), phoneNumber)
        }

        resendOTPEnable = false
    }

    private var resendOTPEnable: Boolean? = null
    private fun countDownForNextOtp() {
        lifecycleScope.launch {
            // time count down for seconds,
            // with 1 second as countDown interval
            object : CountDownTimer(18000, 1000) {

                // Callback function, fired on regular interval
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    if (_binding == null) {
                        return
                    }
                    var seconds = (millisUntilFinished / 1000).toInt()
                    val minutes = seconds / 60
                    seconds %= 60
                    binding.countDownResendOtp.text = "Seconds remaining : " + String.format(
                        "%02d",
                        minutes
                    ) + ":" + String.format("%02d", seconds)
                    binding.countDownResendOtp.setTextColor(Color.parseColor("#FF0000"))
                    resendOTPEnable = false
                }

                // Callback function, fired
                // when the time is up
                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    if (_binding == null) {
                        return
                    }

                    binding.countDownResendOtp.text = "Resend OTP"
                    binding.countDownResendOtp.setTextColor(Color.parseColor("#7E398A"))
                    resendOTPEnable = true
                }
            }.start()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}