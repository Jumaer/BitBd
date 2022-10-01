package com.example.bitbd.ui.fragment.affiliate

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
import com.example.bitbd.databinding.FragmentAffiliateBinding
import com.example.bitbd.ui.fragment.affiliate.adapter.AffiliateItemAdapter
import com.example.bitbd.ui.fragment.affiliate.model.AffiliateObject

import com.example.bitbd.ui.fragment.profile.ProfileViewModel
import com.example.bitbd.ui.fragment.transaction.TransactionViewModel
import com.example.bitbd.ui.fragment.transaction.adapter.TransactionItemAdapter
import com.example.bitbd.ui.fragment.transaction.model.TransactionObject
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class AffiliateFragment : Fragment() {
    private var _binding: FragmentAffiliateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this)[AffiliateViewModel::class.java]

        _binding = FragmentAffiliateBinding.inflate(inflater, container, false)
        val root: View = binding.root


        createDisplayAdapter()
        getExpectedData(viewModel)

        binding.appCompatEditText.doAfterTextChanged {
            val searchableText = it.toString()
            affiliateItemList.clear()
            adapter.notifyDataSetChanged()
            searchItem(searchableText)
        }
        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchItem(searchableText: String) {
        lifecycleScope.launch {
            if (searchableText.isNotEmpty()) {
                affiliateItemList.addAll(
                    BitBDUtil.getResultListFromAllTypeAffiliateLists(
                        searchableText,
                        searchListsCollection,
                        serverAffiliateItemList
                    )
                )
            } else {
                affiliateItemList.addAll(serverAffiliateItemList)
            }
            adapter.notifyDataSetChanged()
        }

    }

    lateinit var adapter: AffiliateItemAdapter
    private var affiliateItemList: MutableList<AffiliateObject> = ArrayList()


    @SuppressLint("NotifyDataSetChanged")
    private fun createDisplayAdapter() {
        adapter = AffiliateItemAdapter(affiliateItemList, ::onAdapterItemClick, requireContext())
        binding.affiliateRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.affiliateRecycle.adapter = adapter
        binding.affiliateRecycle.adapter?.notifyDataSetChanged()
    }

    private fun onAdapterItemClick(position: Int) {

        Log.d("Implement" , "Not yet for $position")
    }

    var dateList: MutableList<String> = ArrayList()
    var commissionList: MutableList<String> = ArrayList()
    var trxTypeList: MutableList<String> = ArrayList()
    var amountList: MutableList<String> = ArrayList()


    var searchListsCollection: MutableList<List<String>> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createExpectedSearchList(
        itemList: MutableList<AffiliateObject>,
        loading: LoadingProgress?
    ) {
        clearAllSearchList()
        for (item in itemList) {
            dateList.add(
                ZonedDateTime.parse(item.createdAt)
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .format(
                        DateTimeFormatter.ofPattern(
                            "dd MMMM yyyy"
                        )
                    )
            )
            item.refCommission?.let { commissionList.add(it.toString()) }
            item.trxType?.let { trxTypeList.add(it) }
            item.amount?.let { amountList.add(it.toString()) }
        }

        searchListsCollection.add(dateList)
        searchListsCollection.add(commissionList)
        searchListsCollection.add(trxTypeList)
        searchListsCollection.add(amountList)


        loading?.dismiss()
    }

    private fun clearAllSearchList() {
        dateList.clear()
        commissionList.clear()
        trxTypeList.clear()
        amountList.clear()
        searchListsCollection.clear()
    }

    private var serverAffiliateItemList: MutableList<AffiliateObject> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun getExpectedData(viewModel: AffiliateViewModel) {

        var loading: LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(requireContext())
                } else loading?.dismiss()
            }
        }

        viewModel.baseAffiliate.observe(viewLifecycleOwner) {
            if (it != null) {
                val baseResponse = it.data ?: return@observe
                serverAffiliateItemList.clear()
                for (response in baseResponse) {
                    response?.let { obj -> serverAffiliateItemList.add(obj) }
                }
                affiliateItemList.clear()
                affiliateItemList.addAll(serverAffiliateItemList)
                adapter.notifyDataSetChanged()
                createExpectedSearchList(serverAffiliateItemList, loading)

            }
        }

        lifecycleScope.launch {
            viewModel.baseAffiliationCall(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}