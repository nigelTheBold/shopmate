package org.paperdove.shopmate

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.paperdove.shopmate.data.DbProductSource
import org.paperdove.shopmate.data.model.SampleData
import java.io.IOException

class DbProductSourceTest {
    private lateinit var db: DbProductSource
    private lateinit var context: Context
    @Before
    fun createDb() {
        val sampleData = SampleData()
        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = DbProductSource.getInMemoryInstance(context)
        db.taxDao().insert(sampleData.sampleTaxes())
        db.productDao().insert(sampleData.sampleProducts())
        db.productTaxDao().insert(sampleData.sampleProductTax())
        db.basketDao().insert(sampleData.sampleBaskets())
        db.basketContentsDao().insert(sampleData.sampleBasketItems())
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        //db.close()
    }

    @Test
    @Throws(Exception::class)
    fun fetchTaxes() {
        val taxes = db.taxDao().getAll()
        assertEquals("Wrong number of taxes", 2, taxes.count())
    }

    @Test
    @Throws(Exception::class)
    fun fetchProducts() {
        val products = db.productDao().getAll()
        assertEquals("Wrong number of products", 9, products.count())
    }
    @Test
    @Throws(Exception::class)
    fun fetchProductTaxes() {
        val productTaxes = db.productTaxDao().getAll()
        assertEquals("Wrong number of products taxes", 8, productTaxes.count())
    }

    @Test
    @Throws(Exception::class)
    fun fetchBasket1() {
        var basket = db.basket("Shopping Basket 1")
        assertNotNull("No basket 1!", basket)
        assertEquals("Tax", 10f, basket.totalTax)
        assertEquals("Pretax total", 116.98f, basket.totalPretax)
        assertEquals("Total", 126.98f, basket.total)
    }

    @Test
    @Throws(Exception::class)
    fun fetchBasket2() {
        var basket = db.basket("Shopping Basket 2")
        assertNotNull("No basket 2!", basket)
        assertEquals("Tax", 2250.80f, basket.totalTax)
        assertEquals("Total", 17263.05f, basket.total)
    }

    @Test
    @Throws(Exception::class)
    fun fetchBasket3() {
        var basket = db.basket("Shopping Basket 3")
        assertNotNull("No basket 3!", basket)
        assertEquals("Tax", 10.80f, basket.totalTax)
        assertEquals("Total", 1149.78f, basket.total)
    }
}