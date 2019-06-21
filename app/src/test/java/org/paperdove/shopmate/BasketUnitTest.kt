package org.paperdove.shopmate

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.paperdove.shopmate.data.model.*

class BasketUnitTest {
    val basicTax = object: Tax {
        override val name = "Basic Tax"
        override val rate = 10f
        override val roundTo = 0.05f
    }

    val importDuty = object: Tax {
        override val name = "Import Duty"
        override val rate = 5f
        override val roundTo = 0.05f
    }

    val none = arrayListOf<Tax>()
    val basic = arrayListOf(basicTax)
    val exempt = arrayListOf(importDuty)
    val imported = arrayListOf(basicTax, importDuty)

    val skittles = object: Product { override val name = "16lb bag of Skittles"; override val price = 16.000f }
    val walkman = object: Product { override val name = "Walkman"; override val price = 99.990f }
    val microwavePopcorn = object: Product { override val name = "bag of microwave"; override val price = 0.990f }
    val vanillaHazelnutCoffee = object: Product { override val name = "imported bag of Vanilla-Hazelnut Coffee"; override val price = 11.00f }
    val vespa = object: Product { override val name = "Imported Vespa"; override val price = 15001.25f }
    val almondSnickers = object: Product { override val name = "imported crate of Almond Snickers"; override val price = 75.99f }
    val discman = object: Product { override val name = "Discman"; override val price = 55.00f }
    val bottleOfWine = object: Product { override val name = "Imported Bottle of Wine"; override val price = 10.00f }
    val fairTradeCoffee = object: Product { override val name = "300# bag of Fair-Trade Coffee"; override val price = 997.99f }

    @Test
    fun simpleBasket1() {
        val basket = SimpleBasket(
            "Shopping Basket 1", arrayListOf(
                SimpleBasketItem(skittles, 1, none),
                SimpleBasketItem(walkman, 1, basic),
                SimpleBasketItem(microwavePopcorn, 1, none)
            )
        )

        assertEquals("Tax", 10f, basket.totalTax)
        assertEquals("Pretax total", 116.98f, basket.totalPretax)
        assertEquals("Total", 126.98f, basket.total)
    }

    @Test
    fun simpleBasket2() {
        val basket = SimpleBasket(
            "Shopping Basket 2", arrayListOf(
                SimpleBasketItem(vanillaHazelnutCoffee, 1, exempt),
                SimpleBasketItem(vespa, 1, imported)
            )
        )

        assertEquals("Tax", 2250.80f, basket.totalTax)
        assertEquals("Total", 17263.05f, basket.total)
    }

    @Test
    fun simpleBasket3() {
        val basket = SimpleBasket(
            "Shopping Basket 1", arrayListOf(
                SimpleBasketItem(almondSnickers, 1, exempt),
                SimpleBasketItem(discman, 1, basic),
                SimpleBasketItem(bottleOfWine, 1, imported),
                SimpleBasketItem(fairTradeCoffee, 1, none)
            )
        )

        assertEquals("Tax", 10.80f, basket.totalTax)
        assertEquals("Total", 1149.78f, basket.total)
    }
}