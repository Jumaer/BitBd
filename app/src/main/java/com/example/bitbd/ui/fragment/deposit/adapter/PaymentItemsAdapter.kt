package com.example.bitbd.ui.fragment.deposit.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bitbd.databinding.LayoutPaymentItemBinding
import com.example.bitbd.ui.fragment.deposit.model.PaymentMethod
import com.example.bitbd.util.BitBDUtil
import com.example.bitbd.BuildConfig

class PaymentItemsAdapter (
    private val payList: List<PaymentMethod>,
    private val onItemClicked: (position: Int) -> Unit, private val context: Context
    ) : RecyclerView.Adapter<PaymentItemsAdapter.PaymentItemViewHolder>() {


    inner class PaymentItemViewHolder (val binding: LayoutPaymentItemBinding,
                                 private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
           binding.baseView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onItemClicked(bindingAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentItemViewHolder {
        val binding = LayoutPaymentItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentItemViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: PaymentItemViewHolder, position: Int) {
        with(holder) {
            with(payList[position]) {
                 binding.baseTitle.text = this.name
                 val loadUrl =  BuildConfig.SERVER_URL + this.image.toString()
                 BitBDUtil.loadImage(binding.shapeImageView,binding.animationView,loadUrl, context)
            }
        }
    }

    override fun getItemCount() = payList.size
}
