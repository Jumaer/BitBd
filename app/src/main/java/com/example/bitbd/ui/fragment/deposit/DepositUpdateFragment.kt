package com.example.bitbd.ui.fragment.deposit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.DEPOSIT
import com.example.bitbd.databinding.FragmentDepositUpdateBinding
import com.example.bitbd.ui.activity.submit.SubmitDepositActivity
import com.example.bitbd.ui.fragment.deposit.adapter.PaymentItemsAdapter
import com.example.bitbd.ui.fragment.deposit.model.PaymentMethod
import com.example.bitbd.util.BitBDUtil
import com.example.bitbd.util.RunTimeValue
import kotlinx.coroutines.launch


class DepositUpdateFragment : Fragment() {

    private var _binding: FragmentDepositUpdateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val slideshowViewModel =
            ViewModelProvider(this)[DepositViewModel::class.java]

        _binding = FragmentDepositUpdateBinding.inflate(inflater, container, false)
        val root: View = binding.root


        createDisplayForDeposits()
        getExpectedData(slideshowViewModel)

        return root
    }


    lateinit var adapter: PaymentItemsAdapter
    private var itemList: MutableList<PaymentMethod> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    private fun createDisplayForDeposits() {
        adapter = PaymentItemsAdapter(itemList, ::onAdapterItemClick, requireContext())
        binding.gridPayamentRecycle.layoutManager = GridLayoutManager(requireContext(),2)
        binding.gridPayamentRecycle.adapter = adapter
        binding.gridPayamentRecycle.adapter?.notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getExpectedData(viewModel: DepositViewModel) {
        var loading: LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(requireContext())
                } else loading?.dismiss()
            }
        }

        viewModel.payment.observe(viewLifecycleOwner) {
            if (it != null) {
                RunTimeValue.getBaseResponsePayment = it
                val dataList = it.data?.methods ?: return@observe

                itemList.clear()
                for (value in dataList) {
                    itemList.add(value)
                }
                adapter.notifyDataSetChanged()
                loading?.dismiss()
            }
        }

        lifecycleScope.launch {
            viewModel.getPaymentInfo(requireContext())
        }
    }

    private fun onAdapterItemClick(position: Int) {
           val intent  = Intent(requireContext(),SubmitDepositActivity::class.java)
           intent.putExtra(DEPOSIT, itemList[position]);
           startActivity(Intent(requireContext(),SubmitDepositActivity::class.java))
    }

   }




