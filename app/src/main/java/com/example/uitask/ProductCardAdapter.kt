package com.example.uitask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uitask.databinding.ItemProductCardBinding

data class MyDataModel(
    val imageResource: Int,
    val productTitle: String,
    val productSubtitle: String,
    val productDescription: String,
    val productButton: String
)

class ProductCardAdapter(private val dataList: List<MyDataModel>) :
    RecyclerView.Adapter<ProductCardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bind =  ItemProductCardBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind.productImage.setImageResource(item.imageResource)
        holder.bind.productTextOne.text = item.productTitle
        holder.bind.productTextTwo.text = item.productSubtitle
        holder.bind.productTextThree.text = item.productDescription
        holder.bind.appCompatButton.text = item.productButton
    }
}