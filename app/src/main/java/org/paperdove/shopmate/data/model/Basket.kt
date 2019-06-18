package org.paperdove.shopmate.data.model


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
data class BasketItem(
    val product: Product,
    val quantity: Int,
    val taxes: List<Tax>
) {
    val pretaxCost: Float
        get() = round2(product.price * quantity)
    val applicableTaxes: Float
        get() = taxes.fold(0f) { totalTax, tax -> totalTax + tax.calculateTax(pretaxCost) }
}

/**
 * A basket full of products
 *
 * A *Basket* is a named collection of [BasketItem]s
 *
 * @property name The name of the collection
 * @property items A collection of [BasketItem]
 *
 */
data class Basket(val name: String, val items: List<BasketItem>) {
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
