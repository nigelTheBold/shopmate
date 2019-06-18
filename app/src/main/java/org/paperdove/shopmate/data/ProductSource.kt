package org.paperdove.shopmate.data

import org.paperdove.shopmate.data.model.Basket
import org.paperdove.shopmate.data.model.Product
import org.paperdove.shopmate.data.model.Tax

interface ProductSource {
    fun allBasketsNames(): List<String>
    fun allProductNames(): List<String>
    fun product(named: String): Product
    fun taxesForProduct(named: String): List<Tax>
    fun basket(named: String): Basket
}