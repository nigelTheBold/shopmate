package org.paperdove.shopmate.data.model

import org.paperdove.shopmate.data.ProductSource


/**
 * One item, of any quantity
 *
 * A *BasketItem* provides the itemized line-entry
 *
 * @property product The [Product] being purchased
 * @property quantity The quantity of this product being purchased
 * @property taxes The list of applicable [TaxRate]s (if any)
 * @property pretaxCost The total cost of all items before taxes are applied
 * @property applicableTaxes The total taxes applicable to this *BasketItem*
 */
interface BasketItem {
    val product: Product
    val quantity: Int
    val taxes: List<Tax>
    val source: ProductSource?

    val pretaxCost: Float
        get() = round2(product.price * quantity)
    val applicableTaxes: Float
        get() = taxes.fold(0f) { totalTax, tax -> totalTax + tax.calculateTax(pretaxCost) }
}

data class SimpleBasketItem(
    override val product: Product,
    override val quantity: Int,
    override val taxes: List<Tax>,
    override val source: ProductSource? = null
): BasketItem

/**
 * A basket full of products
 *
 * A *Basket* is a named collection of [BasketItem]s
 *
 * @property name The name of the collection
 * @property items A collection of [BasketItem]
 *
 */

interface Basket {
    val name: String
    var items: List<BasketItem>
    var open: Boolean
    var source: ProductSource?

    val totalPretax: Float
        get() = items.fold(0f) { total, item -> round2(item.product.price * item.quantity + total) }

    val totalTax: Float
        get() = items.fold(0f) { total, item -> round2(total + item.applicableTaxes * item.quantity) }

    val total: Float
        get() = totalPretax + totalTax

    val receipt: String
        get() = """${items.joinToString("\n") { "${it.product.name} at ${String.format("$%.02f", it.product.price)}"}}
            Sales Taxes: ${String.format("$%.02f", totalTax)}
            Total: ${String.format("$%.02f", total)}
            """
}

data class SimpleBasket(override val name: String, override var items: List<BasketItem>, override var open: Boolean = false,
                        override var source: ProductSource? = null
): Basket
