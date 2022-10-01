package com.example.bitbd.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.bitbd.R
import com.example.bitbd.databinding.ActivityChatBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.fragment.accounts.ViewInformationFragment
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.support.builders.Config
import com.jivosite.sdk.ui.chat.JivoChatFragment


class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var preference: BitBDPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference = BitBDPreferences(this)
        title = "Welcome ${preference.getName()}"


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, JivoChatFragment::class.java, null)
                .commit()
        }


//        binding.jivoBtn.run {
//            setOnClickListener {
//                parentFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.container, JivoChatFragment())
//                    .commit()
//            }
//        }

        Jivo.setConfig(
            Config.Builder()
                .setBackground(R.drawable.jivo_drawable)
                .setTitle(R.string.messageUs)
                .setTitleTextColor(R.color.white)
                .setSubtitleTextColor(R.color.white)
                .setSubtitleTextColorAlpha(0.6f)
                .setWelcomeMessage(R.string.jivosdk_welcome)
                .setOutgoingMessageColor(Config.Color.GREY)
                .build()
        )

    }


}