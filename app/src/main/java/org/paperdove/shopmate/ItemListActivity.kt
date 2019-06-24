package org.paperdove.shopmate

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

import org.paperdove.shopmate.data.DataContent
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_edit.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*
import org.paperdove.shopmate.data.model.Basket
import kotlin.concurrent.thread

class ItemListActivity : AppCompatActivity() {
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (item_detail_container != null) {
            twoPane = true
        }

        if (twoPane) {
            fab.hide()
        } else {
            fab.setOnClickListener { view ->
                newBasket()
            }
        }

        ShopMateApp.dataReadyNotification = {
            setupRecyclerView(item_list)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (twoPane) {
            menuInflater.inflate(R.menu.main, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.new_basket -> newBasket()
        else -> super.onOptionsItemSelected(item)
    }

    private fun newBasket(): Boolean {
        thread {
            // TODO: Be a little more subtle about changing basket lists
            val basket = ShopMateApp.productSource.newBasket()
            DataContent.refresh()
            fab.post {
                openDetails(basket)
                setupRecyclerView(item_list)
            }
        }
        return true
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        thread {
            val items = DataContent.ITEMS
            recyclerView.post {
                recyclerView.adapter = SimpleItemRecyclerViewAdapter(items, View.OnClickListener { v ->
                    val basket = v.tag as Basket
                    openDetails(basket)
                })
            }
        }
    }

    private fun openDetails(basket: Basket) {
        if (twoPane) {
            val fragment = if (basket.open) EditBasketFragment() else ItemDetailFragment()
            fragment.apply {
                arguments = Bundle().apply {
                    putString(ItemDetailFragment.ARG_ITEM_ID, basket.name)
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit()


        } else {
            val intent = Intent(this, ItemDetailActivity::class.java).apply {
                putExtra(ItemDetailFragment.ARG_ITEM_ID, basket.name)
            }
            startActivity(intent)
        }
    }

    class SimpleItemRecyclerViewAdapter(
        private val values: List<Basket>,
        private val onClickListener: View.OnClickListener
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.name
            //holder.contentView.text = item.receipt

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
