package com.example.bitbd.ui.activity.accounts

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.bitbd.R
import com.example.bitbd.databinding.ActivityAccountManagementBinding
import com.example.bitbd.databinding.ActivityNotificationBinding
import com.example.bitbd.ui.activity.BaseActivity
import com.example.bitbd.ui.activity.accounts.adapter.TabFragmentAdapter
import com.example.bitbd.ui.fragment.deposit.adapter.DepositPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class AccountManagementActivity : BaseActivity() {
    private lateinit var binding: ActivityAccountManagementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.accounts)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.viewPager.adapter = TabFragmentAdapter(this)
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab, index ->
            tab.text= when(index){
                0-> "View All"
                1-> "Add New"
                else -> {throw Resources.NotFoundException("Position not found")}
            }
        }.attach()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}