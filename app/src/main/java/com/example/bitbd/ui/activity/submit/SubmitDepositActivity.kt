package com.example.bitbd.ui.activity.submit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.bitbd.R
import com.example.bitbd.constant.DEPOSIT
import com.example.bitbd.databinding.ActivityMainBinding
import com.example.bitbd.databinding.ActivitySubmitDepositBinding
import com.example.bitbd.ui.fragment.deposit.model.PaymentMethod
import com.example.bitbd.util.BitBDUtil

class SubmitDepositActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubmitDepositBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitDepositBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.depositDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val depositSubmit : PaymentMethod? =  intent.getParcelableExtra(DEPOSIT)

        BitBDUtil.showMessage(depositSubmit?.name.toString(),this)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}