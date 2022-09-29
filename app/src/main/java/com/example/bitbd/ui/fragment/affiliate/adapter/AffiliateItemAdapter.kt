package com.example.bitbd.ui.fragment.affiliate.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.bitbd.databinding.LayoutAffiliateItemBinding
import com.example.bitbd.ui.fragment.affiliate.model.AffiliateObject
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AffiliateItemAdapter (private val itemList: List<AffiliateObject>,
                            private val onItemClicked: (position: Int) -> Unit, private val context: Context
) : RecyclerView.Adapter<AffiliateItemAdapter.AffiliateItemAdapterViewHolder>() {

    inner class AffiliateItemAdapterViewHolder(
        val binding: LayoutAffiliateItemBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {

            binding.baseItem.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

            onItemClicked(adapterPosition)

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AffiliateItemAdapter.AffiliateItemAdapterViewHolder {
        val binding = LayoutAffiliateItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AffiliateItemAdapterViewHolder(binding, onItemClicked)
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: AffiliateItemAdapter.AffiliateItemAdapterViewHolder,
        position: Int
    ) {
        with(holder) {
            with(itemList[position]) {
                binding.Date.text = ZonedDateTime.parse(this.createdAt)
                    .withZoneSameInstant(ZoneId.of("Asia/Dhaka"))
                    .format(
                        DateTimeFormatter.ofPattern(
                            "dd MMMM yyyy"
                        )
                    )

                binding.TrxType.text = this.trxType
                binding.Amount.text = this.amount.toString()
                binding.refUser.text = "User Id : ${this.userId}"
                binding.Commission.text = this.refCommission.toString()

            }
        }
    }

    override fun getItemCount() = itemList.size
}