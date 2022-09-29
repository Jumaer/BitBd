package com.example.bitbd.ui.fragment.otp_verify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.INFO
import com.example.bitbd.constant.OTP_PHONE
import com.example.bitbd.databinding.FragmentNumberInputBinding
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch


class NumberInputFragment : Fragment() {
    private var _binding: FragmentNumberInputBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this)[OTPViewModel::class.java]
        // Inflate the layout for this fragment
        _binding = FragmentNumberInputBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.sendOtpButton.setOnClickListener {
            requestForANewOTP(viewModel)
        }



        return root
    }




    private fun requestForANewOTP(viewModel: OTPViewModel) {

        val phone = binding.mobileLayout.editText?.text.toString().trim()

        if(phone.isEmpty() ){
            binding.mobileLayout.error = getString(R.string.this_field_is_required)
            return
        }
        if(phone.length != 11){
            binding.mobileLayout.error = "11 Digit as Bangladeshi number"
            return
        }
        if(phone.substring(0,2) != "01"){
            binding.mobileLayout.error = "First two digit is 0,1"
            return
        }
        binding.mobileLayout.error = null


        var loading : LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner){
            if(it != null){
                if(it) {
                    loading = BitBDUtil.showProgress(requireContext())
                }
                else{
                    loading?.dismiss()

                }
            }
        }

        viewModel.sendOtp.observe(viewLifecycleOwner){
            if(it!= null){
                val numberBundle = Bundle()
                numberBundle.putString(OTP_PHONE, phone)
                findNavController().navigate(R.id.action_numberInputFragment_to_otpInputFragment , numberBundle)
            }
        }

        lifecycleScope.launch {
          viewModel.requestForOtp(requireContext(),phone)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}