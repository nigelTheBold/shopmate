package org.paperdove.shopmate.tax

class Taxable(public var item: String, public var quantity: Int, private var price: Float) {
    private var applicableTaxes = mutableListOf<Tax>()

    fun applyTax(tax: Tax) {
        applicableTaxes.add(tax)
    }

    fun priceAfterTaxes(): Float {
        val total = price * quantity
        return applicableTaxes.fold(total) {sum, tax -> sum + tax.calculateTax(total)}
    }

    override fun toString(): String {
        return super.toString()
    }
}