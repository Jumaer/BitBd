package com.example.bitbd.ui.fragment.withdraw_money

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.bitbd.databinding.FragmentDepositsContainerBinding
import com.example.bitbd.databinding.FragmentWithdrawBinding
import com.example.bitbd.ui.fragment.deposit.adapter.DepositPagerAdapter
import com.example.bitbd.ui.fragment.withdraw_money.adapter.WithdrawPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class WithdrawFragment : Fragment() {

    private var _binding: FragmentWithdrawBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       

        _binding = FragmentWithdrawBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.viewPager.adapter = WithdrawPagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab, index ->
            tab.text= when(index){
                0-> "View All"
                1-> "Add New"
                else -> {throw Resources.NotFoundException("Position not found")}
            }
        }.attach()
        return root
    }


}