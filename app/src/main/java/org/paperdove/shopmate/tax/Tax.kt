package org.paperdove.shopmate.tax

import java.lang.RuntimeException
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round


/**
 * A tax item.
 *
 * Defines the fields required for a tax item.
 *
 * @property name an arbitrary name for this tax
 * @property rate the tax rate as a percentage (ex: 2.5% tax rate would be 2.5f)
 * @property roundTo the nearest number to round up to (ex: $.05 would be .05f)
 */
interface Tax {
    val name: String
    val rate: Float
    val roundTo: Float

    private fun roundTax(taxAmount: Float) : Float {
        val roundAmount = (roundTo * 100).toInt()
        val taxAsPennies = round(taxAmount * 100).toInt()
        var remaining = roundAmount - (taxAsPennies).rem(roundAmount)

        /*
        if (remaining.toInt() == roundAmount) {
            remaining = 0f
        }
        */
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

        return roundTax(onPrice * (rate / 100))
    }
}