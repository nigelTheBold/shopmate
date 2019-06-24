package org.paperdove.shopmate

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlin.concurrent.thread

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */
class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(detail_toolbar)

        fab.setOnClickListener { view ->

        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            thread {
                val basketName = intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID)
                val basket = ShopMateApp.productSource.basket(basketName)
                val fragment = if (basket.open) EditBasketFragment() else ItemDetailFragment()

                fragment.apply {
                    arguments = Bundle().apply {
                        putString(
                            ItemDetailFragment.ARG_ITEM_ID,
                            intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID)
                        )
                    }
                }

                fab.post {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                navigateUpTo(Intent(this, ItemListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
