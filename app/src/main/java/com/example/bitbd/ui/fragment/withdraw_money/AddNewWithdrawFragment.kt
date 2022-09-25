package com.example.bitbd.ui.fragment.withdraw_money

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.databinding.FragmentAddNewBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.fragment.withdraw_money.model.WithdrawAccountResponse
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch


class AddNewWithdrawFragment : Fragment() {

    private var _binding: FragmentAddNewBinding? = null
    var slideshowViewModel : WithdrawViewModel? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    var preferences : BitBDPreferences? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        slideshowViewModel = ViewModelProvider(this)[WithdrawViewModel::class.java]
        _binding = FragmentAddNewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        preferences = BitBDPreferences(requireContext())

        observeWithdrawAccountInfoData(slideshowViewModel!!)


        return root
    }

    @SuppressLint("SetTextI18n")
    private fun observeWithdrawAccountInfoData(viewModel: WithdrawViewModel) {
        var loading: LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(requireContext())
                } else loading?.dismiss()
            }
        }
        viewModel.withdrawAccount.observe(viewLifecycleOwner) {
            if (it != null) {
                val baseResponse = it.data ?: return@observe
                val dataResponseList = baseResponse.accounts?:return@observe
                for (typeGet in dataResponseList){
                    typeList.add(typeGet.type.toString())
                }

                var dateList = ""
                if(baseResponse.withdrawDate != null){
                    var counter  = 0
                    for(date in baseResponse.withdrawDate!!){
                        counter ++
                        dateList = if(counter == 1){
                            date
                        } else{
                            "$dateList , $date"
                        }

                    }
                }

                binding.balanceHintDate.text = dateList
                binding.maxWithdraw.text = "( ${baseResponse.maxWithdraw + "*"} )"
                binding.balanceHintBase.text= preferences?.getAvailableBalance().toString()
                setTextChangeListener(dataResponseList,loading)

            }
        }



        lifecycleScope.launch {
            viewModel.withdrawAccountInfo(requireContext())
        }


    }


    private var typeList = ArrayList<String>()
    private var valueOfType : String = ""

    private var accountList = ArrayList<String>()
    private var valueOfAccount : String = ""

    private fun setTextChangeListener(
        dataList: List<WithdrawAccountResponse>,
        loading: LoadingProgress?
    ) {
        binding.typeLayout.setOnKeyListener(null)
        binding.typeTextView.inputType = InputType.TYPE_NULL
        context?.let {
            ArrayAdapter(
                it, android.R.layout.simple_list_item_1,
                typeList
            ).also { adapter ->
                binding.typeTextView.setAdapter(adapter)
                adapter.notifyDataSetChanged()
            }
        }
        binding.typeLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                valueOfType = text.toString()

                val tempAccountList = ArrayList<String>()
                for (i in dataList) {
                    if(i.type.toString() == valueOfType){
                        tempAccountList.add(i.name.toString())
                    }
                }
                accountList = tempAccountList

                context?.let {
                    ArrayAdapter(
                        it, android.R.layout.simple_list_item_1,
                        accountList
                    ).also { adapter ->
                        binding.accountTextView.setAdapter(adapter)
                        adapter.notifyDataSetChanged()
                        if(accountList.size >0)
                        binding.accountTextView.text = BitBDUtil.editable(accountList[0])

                    }
                }
            }
        }

        binding.accountLayout.setOnKeyListener(null)
        binding.accountTextView.inputType = InputType.TYPE_NULL

        binding.accountLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                valueOfAccount = text.toString()
            }
        }

        loading?.dismiss()
    }


}