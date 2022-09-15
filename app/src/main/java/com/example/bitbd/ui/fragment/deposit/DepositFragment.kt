package com.example.bitbd.ui.fragment.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.databinding.FragmentDepositBinding
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch


class DepositFragment : Fragment() {

    private var _binding: FragmentDepositBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(DepositViewModel::class.java)

        _binding = FragmentDepositBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        getExpectedData(slideshowViewModel)

        return root
    }

    private fun getExpectedData(viewModel: DepositViewModel) {
        var loading : LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner){
            if(it != null){
                if(it) {
                    loading =  BitBDUtil.showProgress(requireContext())
                }
                else loading?.dismiss()
            }
        }

        viewModel.deposit.observe(viewLifecycleOwner){
            if(it != null){
                BitBDUtil.showMessage(it.message.toString(), requireContext())
                loading?.dismiss()
            }
        }

        viewModel.payment.observe(viewLifecycleOwner){
            if(it != null){
                BitBDUtil.showMessage(it.message.toString(), requireContext())
                loading?.dismiss()
            }
        }

        lifecycleScope.launch {
            viewModel.getPaymentInfo(requireContext())
            viewModel.deposit(requireContext())
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}