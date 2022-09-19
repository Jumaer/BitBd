package com.example.bitbd.ui.fragment.deposit

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bitbd.R
import com.example.bitbd.databinding.FragmentDepositBinding
import com.example.bitbd.databinding.FragmentDepositsContainerBinding
import com.example.bitbd.ui.activity.main.MainActivity
import com.example.bitbd.ui.activity.submit.SubmitDepositActivity
import com.example.bitbd.ui.fragment.deposit.adapter.DepositPagerAdapter
import com.example.bitbd.util.BitBDUtil
import com.google.android.material.tabs.TabLayoutMediator


class DepositsContainerFragment : Fragment() {

    private var _binding: FragmentDepositsContainerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDepositsContainerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewPager.adapter = DepositPagerAdapter(requireActivity())
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