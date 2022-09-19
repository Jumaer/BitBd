package com.example.bitbd.ui.fragment.deposit.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bitbd.ui.fragment.deposit.DepositFragment
import com.example.bitbd.ui.fragment.deposit.DepositUpdateFragment


class DepositPagerAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment){
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
     return when(position){
         0-> DepositFragment()
         1 -> DepositUpdateFragment()
         else -> {throw Resources.NotFoundException("Position not found")}

     }
    }
}