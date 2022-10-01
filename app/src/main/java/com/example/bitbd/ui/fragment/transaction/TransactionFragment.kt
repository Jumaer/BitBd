package com.example.bitbd.ui.fragment.transaction

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.databinding.FragmentTransactionBinding
import com.example.bitbd.ui.fragment.deposit.adapter.DepositItemAdapter
import com.example.bitbd.ui.fragment.deposit.model.DepositDataResponse
import com.example.bitbd.ui.fragment.transaction.adapter.TransactionItemAdapter
import com.example.bitbd.ui.fragment.transaction.model.TransactionObject
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val viewModel =
            ViewModelProvider(this).get(TransactionViewModel::class.java)

        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        createDisplayAdapter()

        getExpectedData(viewModel)
        binding.appCompatEditText.doAfterTextChanged {
            val searchableText = it.toString()
            transactionItemList.clear()
            adapter.notifyDataSetChanged()
            searchItem(searchableText)
        }
        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchItem(searchableText: String) {
        lifecycleScope.launch {
            if (searchableText.isNotEmpty()) {
                transactionItemList.addAll(
                    BitBDUtil.getResultListFromAllTypeTransactionLists(
                        searchableText,
                        searchListsCollection,
                        serverTransItemList
                    )
                )
            } else {
                transactionItemList.addAll(serverTransItemList)
            }
            adapter.notifyDataSetChanged()
        }

    }

    private var serverTransItemList : MutableList<TransactionObject> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun getExpectedData(viewModel: TransactionViewModel) {

        var loading : LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner){
            if(it != null){
                if(it) {
                    loading =  BitBDUtil.showProgress(requireContext())
                }
                else loading?.dismiss()
            }
        }

        viewModel.baseTransaction.observe(viewLifecycleOwner){
            if(it != null){
                val baseResponse = it.data ?: return@observe
                serverTransItemList.clear()
                for(response in baseResponse){
                    response?.let { obj -> serverTransItemList.add(obj) }
                }
                transactionItemList.clear()
                transactionItemList.addAll(serverTransItemList)
                adapter.notifyDataSetChanged()
                createExpectedSearchList(serverTransItemList,loading)

            }
        }

        lifecycleScope.launch {
            viewModel.baseTransactionCall(requireContext())
        }
    }

    lateinit var adapter: TransactionItemAdapter
    private var transactionItemList: MutableList<TransactionObject> = ArrayList()


    @SuppressLint("NotifyDataSetChanged")
    private fun createDisplayAdapter() {
        adapter = TransactionItemAdapter(transactionItemList, ::onAdapterItemClick, requireContext())
        binding.transactionRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionRecycle.adapter = adapter
        binding.transactionRecycle.adapter?.notifyDataSetChanged()
    }

    private fun onAdapterItemClick(position: Int) {
     Log.d("Implement" , "Not yet for $position")

    }

    var dateList: MutableList<String> = ArrayList()
    var accNameList: MutableList<String> = ArrayList()
    var typeList: MutableList<String> = ArrayList()
    var trxTypeList: MutableList<String> = ArrayList()
    var trxIdList: MutableList<String> = ArrayList()
    var amountList: MutableList<String> = ArrayList()
    var statusList: MutableList<String> = ArrayList()
    var trxAccountNoList: MutableList<String> = ArrayList()

    var searchListsCollection: MutableList<List<String>> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createExpectedSearchList(
        itemList: MutableList<TransactionObject>,
        loading: LoadingProgress?
    ) {
        clearAllSearchList()
        for (item in itemList) {
            dateList.add(
                ZonedDateTime.parse(item .createdAt)
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .format(
                        DateTimeFormatter.ofPattern(
                            "dd MMMM yyyy"
                        )
                    )
            )
            item .trxName?.let { accNameList.add(it) }
            item .trxId?.let { trxIdList.add(it) }
            item .trxType?.let { trxTypeList.add(it) }
            item .amount?.let { amountList.add(it.toString()) }
            item .type?.let { typeList.add(it) }
            item .status?.let { statusList.add(it) }
            item .trxAccount?.let { trxAccountNoList.add(it) }
        }

        searchListsCollection.add(dateList)
        searchListsCollection.add(accNameList)
        searchListsCollection.add(trxIdList)
        searchListsCollection.add(trxTypeList)
        searchListsCollection.add(amountList)
        searchListsCollection.add(typeList)
        searchListsCollection.add(statusList)
        searchListsCollection.add(trxAccountNoList)

        loading?.dismiss()
    }

    private fun clearAllSearchList() {
        dateList.clear()
        accNameList.clear()
        typeList.clear()
        trxTypeList.clear()
        trxIdList.clear()
        amountList.clear()
        statusList.clear()
        trxAccountNoList.clear()
        searchListsCollection.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}