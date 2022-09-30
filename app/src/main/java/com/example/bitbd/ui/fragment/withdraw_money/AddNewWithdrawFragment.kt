package com.example.bitbd.ui.fragment.withdraw_money

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.INFO
import com.example.bitbd.constant.WARNING
import com.example.bitbd.databinding.FragmentAddNewBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.fragment.withdraw_money.model.WithdrawAccountResponse
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class AddNewWithdrawFragment : Fragment() {

    private var _binding: FragmentAddNewBinding? = null
    var slideshowViewModel: WithdrawViewModel? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    var preferences: BitBDPreferences? = null
    private val binding get() = _binding!!
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        slideshowViewModel = ViewModelProvider(this)[WithdrawViewModel::class.java]
        _binding = FragmentAddNewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        preferences = BitBDPreferences(requireContext())

        observeWithdrawAccountInfoData(slideshowViewModel!!)

        binding.submitButton.setOnClickListener {
            submitWithdraw()
        }

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
                val dataResponseList = baseResponse.accounts ?: return@observe
                typeList.clear()
                for (typeGet in dataResponseList) {
                    typeList.add(typeGet.type.toString().trim())
                    typeList = typeList.distinct().toMutableList()
                }
                typeListToDisplay.clear()
                for (typeGet in typeList){
                    if(typeGet.trim() == "Mobile"){
                        typeListToDisplay.add("Mobile Banking")
                    }
                    else if(typeGet.trim() == "Bank"){
                        typeListToDisplay.add("Bank Account")
                    }
                    else{
                        typeListToDisplay.add(typeGet.trim())
                    }
                }

                var dateList = ""
                if (baseResponse.withdrawDate != null) {
                    var counter = 0
                    dayList.clear()
                    for (date in baseResponse.withdrawDate!!) {
                        counter++
                        dateList = if (counter == 1) {
                            date
                        } else {
                            "$dateList , $date"
                        }

                        dayList.add(date)

                    }
                }

                binding.balanceHintDate.text = dateList
                binding.maxWithdraw.text = "( Maximum ${baseResponse.maxWithdraw + "*"} )"
                maximumAmount = baseResponse.maxWithdraw.toString()
                binding.balanceHintBase.text = preferences?.getAvailableBalance().toString()
                setTextChangeListener(dataResponseList, loading)

            }
        }



        viewModel.withdrawStore.observe(viewLifecycleOwner){
            if(it != null){
                preferences?.setAnyChangeWithdraw(true)
            }
        }


        lifecycleScope.launch {
            viewModel.withdrawAccountInfo(requireContext())
        }


    }
    private var dayList = ArrayList<String>()


    @RequiresApi(Build.VERSION_CODES.O)
    private fun isPossibleTodayWithdraw() : Boolean {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd")
        val formatted = current.format(formatter)
        val day = formatted

        for(date in dayList){
            if(date == day.toString()){
                return true
            }
        }
        return false
    }

    private var maximumAmount = ""


    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitWithdraw() {

        if (valueOfType.isEmpty()) {
            binding.typeLayout.error = getString(R.string.this_field_is_required)
        } else binding.typeLayout.error = null

        if (valueOfAccount.isEmpty()) {
            binding.accountLayout.error = getString(R.string.this_field_is_required)
        } else binding.accountLayout.error = null

        val valueOfAmount = binding.trxAmountLayout.editText?.text.toString().trim()

        if (valueOfAmount.isEmpty()) {
            binding.trxAmountLayout.error = getString(R.string.this_field_is_required)
        } else binding.trxAmountLayout.error = null


        if(valueOfAmount.isEmpty() || valueOfAccount.isEmpty() || valueOfType.isEmpty()){
            return
        }

        if(maximumAmount.toDouble() < valueOfAmount.toDouble()){
            BitBDUtil.showMessage("Crossing maximum amount : ${maximumAmount.toDouble()}",
                WARNING)
            return
        }

        if(preferences?.getAvailableBalance().toString().toDouble() < valueOfAmount.toDouble()){
            BitBDUtil.showMessage("Available amount is : ${preferences?.getAvailableBalance().toString().toDouble()}",
                WARNING)
            return
        }

        if(!isPossibleTodayWithdraw()){
            BitBDUtil.showMessage("Withdraw is not possible today", INFO)
            return
        }

        lifecycleScope.launch {
            slideshowViewModel?.withdrawSubmit(
                requireContext(),
                valueOfAccount,
                valueOfAmount,
                valueOfType
            )
        }
    }

    private var typeListToDisplay : MutableList<String> = ArrayList()
    private var typeList : MutableList<String> = ArrayList()
    private var valueOfType: String = ""

    private var accountList : MutableList<String> = ArrayList()
    private var valueOfAccount: String = ""

    private fun setTextChangeListener(
        dataList: List<WithdrawAccountResponse>,
        loading: LoadingProgress?
    ) {
        binding.typeLayout.setOnKeyListener(null)
        binding.typeTextView.inputType = InputType.TYPE_NULL
        context?.let {
            ArrayAdapter(
                it, android.R.layout.simple_list_item_1,
                typeListToDisplay
            ).also { adapter ->
                binding.typeTextView.setAdapter(adapter)
                adapter.notifyDataSetChanged()
            }
        }
        binding.typeLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                valueOfType = if(text.toString().trim() == "Mobile Banking"){
                    "Mobile"
                } else if(text.toString().trim() == "Bank Account"){
                    "Bank"
                } else{
                    text.toString().trim()
                }
                val tempAccountList : MutableList<String> = ArrayList()
                for (i in dataList) {
                    if (i.type.toString() == valueOfType) {
                        tempAccountList.add(i.name.toString())
                    }
                }
                accountList = tempAccountList.distinct().toMutableList()
                context?.let {
                    ArrayAdapter(
                        it, android.R.layout.simple_list_item_1,
                        accountList
                    ).also { adapter ->
                        binding.accountTextView.setAdapter(adapter)
                        if (accountList.size > 0)
                            binding.accountTextView.text = BitBDUtil.editable("")
                        adapter.notifyDataSetChanged()
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