package org.paperdove.shopmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.basket_item_list_content.view.*
import kotlinx.android.synthetic.main.basket_total.view.*
import org.paperdove.shopmate.data.model.Basket
import kotlinx.android.synthetic.main.item_edit.*
import kotlin.concurrent.thread

class EditBasketFragment : Fragment() {
    private var item: Basket? = null
    private var basketName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                basketName = it.getString(ARG_ITEM_ID) ?: ""
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thread {
            item = ShopMateApp.productSource.basket(basketName)
            val count = item?.items?.count() ?: 0
            fab.post {
                if (item?.open == true) {
                    fab.show()
                    fab.setOnClickListener {
                        addNewItem()
                    }
                } else {
                    fab.hide()
                }

                if (count > 0) {
                    no_contents_message.visibility = View.GONE
                    item?.let {
                        product_list.adapter = BasketAdapter(it)
                    }
                }
            }
        }
    }

    fun addNewItem() {

    }

    fun saveAndClose() {

    }


    class BasketAdapter(private val basket: Basket): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            if (viewType == TYPE_PRODUCT)
                BasketItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.basket_item_list_content, parent, false))
            else
                SummaryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.basket_total, parent, false))


        override fun getItemCount() = if (basket.items.isNotEmpty()) basket.items.size + 1 else 0

        override fun getItemViewType(position: Int) = if (position < basket.items.size) TYPE_PRODUCT else TYPE_SUMMARY

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            thread {
                when (holder) {
                    is BasketItemViewHolder -> {
                        val item = basket.items[position]
                        val count = item.quantity.toString()
                        val name = item.product.name
                        val price = item.pretaxCost + item.applicableTaxes
                        holder.count.post {
                            holder.count.text = count
                            holder.product.text = name
                            holder.price.text = String.format("$%,.02f", price)
                        }
                    }

                    is SummaryViewHolder -> {
                        val taxes = basket.totalTax
                        val total = basket.total
                        holder.taxes.post {
                            holder.taxes.text = String.format("$%,.02f", taxes)
                            holder.total.text = String.format("$%,.02f", total)
                        }
                    }
                }
            }
        }

        companion object {
            const val TYPE_PRODUCT = 0
            const val TYPE_SUMMARY = 1
        }
    }

    class SummaryViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val taxes = view.taxes
        val total = view.total
    }

    class BasketItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val count = view.count
        val product = view.product
        val price = view.price
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}