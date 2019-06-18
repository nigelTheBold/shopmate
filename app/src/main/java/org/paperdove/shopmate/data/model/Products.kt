package org.paperdove.shopmate.data.model

import kotlin.math.max
import kotlin.math.round

fun round2(x: Float) = round(x * 100) / 100

interface Tax {
    val name: String
    val rate: Float
    val roundTo: Float

    private fun roundTax(taxAmount: Float) : Float {
        val roundAmount = (roundTo * 100).toInt()
        val taxAsPennies = (taxAmount * 100).toInt()
        var remaining = roundAmount - (taxAsPennies).rem(roundAmount)

        if (remaining == roundAmount) {
            remaining = 0
        }
        return max(roundTo, (taxAsPennies + remaining) / 100.0f)
    }

    /**
     * Calculates the tax amount for [onPrice]
     * @return the calculated tax amount
     */
    fun calculateTax(onPrice: Float) : Float {
        if (rate < 0) {
            throw RuntimeException("$name: Illegal rate $rate")
        }

        if (onPrice < 0) {
            throw RuntimeException("$name: Illegal price $onPrice")
        }

        return roundTax(round2(onPrice * (rate / 100)))
    }
}

interface Product {
    val name: String
    val price: Float
}

interface ProductTax {
    val product: String
    val tax: String
}
