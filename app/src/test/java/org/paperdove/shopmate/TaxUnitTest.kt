package org.paperdove.shopmate

import org.junit.Test
import org.junit.Assert.*
import org.paperdove.shopmate.data.model.Tax

class TaxUnitTest {
    @Test
    fun simpleTax1() {
        val tax = object: Tax {
            override val name = "simpleTax1"
            override val rate = 5f
            override val roundTo = 0.05f
        }

        assertEquals(1.05f, tax.calculateTax(20.50f))
    }

    @Test
    fun simpleTax2() {
        val tax = object: Tax {
            override val name = "simpleTax1"
            override val rate = 5f
            override val roundTo = 0.05f
        }

        assertEquals(.05f, tax.calculateTax(.01f))
    }

    @Test
    fun negativeTaxException() {
        val tax = object: Tax {
            override val name = "simpleTax1"
            override val rate = -5f
            override val roundTo = 0.05f
        }

        try {
            tax.calculateTax(20.50f)
            fail("Did not throw with negative tax")
        } catch(e:RuntimeException) {
            // Do nothing at all
        }
    }

    @Test
    fun negativePriceException() {
        val tax = object: Tax {
            override val name = "simpleTax1"
            override val rate = 5f
            override val roundTo = 0.05f
        }

        try {
            tax.calculateTax(-20.50f)
            fail("Did not throw with negative price")
        } catch(e:RuntimeException) {
            // Do nothing at all
        }
    }
}

