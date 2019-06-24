package org.paperdove.shopmate.data

import org.paperdove.shopmate.ShopMateApp
import org.paperdove.shopmate.data.model.Basket
import java.util.ArrayList
import java.util.HashMap

object DataContent {
    val ITEMS: MutableList<Basket> = ArrayList()
    val ITEM_MAP: MutableMap<String, Basket> = HashMap()

    init {
        refresh()
    }

    fun refresh() {
        ITEMS.clear()
        ITEM_MAP.clear()
        val baskets = ShopMateApp.productSource.allBasketsNames()
        baskets.forEach {
            addItem(ShopMateApp.productSource.basket(it))
        }
    }

    private fun addItem(item: Basket) {
        ITEMS.add(item)
        ITEM_MAP.put(item.name, item)
    }
}
