package org.paperdove.shopmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.basket_item_list_content.view.*
import org.paperdove.shopmate.data.model.Basket
import org.paperdove.shopmate.data.model.Product
import kotlin.concurrent.thread


class ProductSelection(val basket: Basket, val productList: List<Product>): RecyclerView.Adapter<SelectItemViewHolder>() {
    var selected = HashMap<String, Product>()

    init {
        thread {
            basket.items.forEach {
                selected[it.product.name] = it.product
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SelectItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.basket_item_list_content, parent, false))

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: SelectItemViewHolder, position: Int) {
        val product = productList[position]
        holder.product.text = product.name
        holder.price.text = String.format("$%,.02f", product.price)

        holder.checkBox.isChecked = selected.containsKey(product.name)

        holder.checkBox.setOnCheckedChangeListener { v, isChecked ->
            if (isChecked) {
                selected[product.name] = product
            } else {
                selected.remove(product.name)
            }
        }
    }

    fun getSelected(): List<Product> {
        return ArrayList(selected.values)
    }
}

class SelectItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val checkBox = view.checkBox
    val product = view.product
    val price = view.price
    init {
        view.count.visibility = View.GONE
    }
}
