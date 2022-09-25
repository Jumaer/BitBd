package com.example.bitbd.ui.fragment.withdraw_money.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bitbd.R
import com.example.bitbd.databinding.LayoutDipositItemListBinding
import com.example.bitbd.databinding.LayoutWithdrawItemListBinding
import com.example.bitbd.ui.fragment.deposit.adapter.DepositItemAdapter
import com.example.bitbd.ui.fragment.deposit.model.DepositDataResponse
import com.example.bitbd.ui.fragment.withdraw_money.model.BaseResponseWithdraw
import com.example.bitbd.util.BitBDUtil
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class WithdrawItemsAdapter (
    private val withdrawItemList: List<BaseResponseWithdraw>,
    private val onItemClicked: (position: Int) -> Unit, private val context: Context
) : RecyclerView.Adapter<WithdrawItemsAdapter.ItemAdapterViewHolder>() {

    inner class ItemAdapterViewHolder(
        val binding: LayoutWithdrawItemListBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {

            binding.ActionPerform.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if(withdrawItemList[adapterPosition].status == "Decline"){
                BitBDUtil.showMessage("Already Declined",context)
            }
            else{
                onItemClicked(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WithdrawItemsAdapter.ItemAdapterViewHolder {
        val binding = LayoutWithdrawItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemAdapterViewHolder(binding, onItemClicked)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: WithdrawItemsAdapter.ItemAdapterViewHolder,
        position: Int
    ) {
        with(holder) {
            with(withdrawItemList[position]) {
                binding.Action.visibility = View.GONE
                binding.ActionPerform.visibility = View.VISIBLE
                binding.Date.text = ZonedDateTime.parse(this.createdAt)
                    .withZoneSameInstant(ZoneId.of("Asia/Dhaka"))
                    .format(
                        DateTimeFormatter.ofPattern(
                            "dd MMMM yyyy"
                        )
                    )

                binding.AccountName.text = this.trxName
                binding.TrxType.text = this.trxType
                binding.Amount.text = this.amount.toString()
                binding.TrxAccNumber.text = this.withdrawAccount
                binding.Status.text = this.status
                if(this.status == "Decline"){
                    binding.Status.background = ContextCompat.getDrawable(context,
                        R.drawable.item_shape_delete
                    )

                    binding.ActionPerform.background = ContextCompat.getDrawable(context,
                        R.drawable.item_shape_not_delete
                    )
                }

                binding.Status.setTextColor(ContextCompat.getColor(context, R.color.white))


            }
        }
    }

    override fun getItemCount() = withdrawItemList.size
}