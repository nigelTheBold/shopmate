package org.paperdove.shopmate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.paperdove.shopmate.data.model.*

import org.paperdove.shopmate.data.model.DbBasketContents
import java.util.concurrent.Executors

fun ioThread(f : () -> Unit) {
    Executors.newSingleThreadExecutor().execute(f)
}

@Database(entities = [DbTax::class, DbProduct::class, DbProductTax::class, DbBasketContents::class],
        version = 1)
abstract class DbProductSource: RoomDatabase(), ProductSource {
        abstract fun taxDao(): TaxDao
        abstract fun productDao(): ProductDao
        abstract fun productTaxDao(): ProductTaxDao
        abstract fun basketDao(): BasketDao

        companion object {

                @Volatile
                private var INSTANCE: DbProductSource? = null
                @Volatile
                private var MEMORY_INSTANCE: DbProductSource? = null
                @Volatile
                private var sampleData = SampleData()

                fun getInMemoryInstance(context: Context) = MEMORY_INSTANCE ?: synchronized(this) {
                        INSTANCE ?: buildInMemoryDatabase(context).also { MEMORY_INSTANCE = it }
                }


                fun getInstance(context: Context, onReady: (() -> Unit?)? = null): DbProductSource =
                        INSTANCE ?: synchronized(this) {
                                INSTANCE ?: buildDatabase(context, onReady).also { INSTANCE = it }
                        }


                // TODO: Refactor to reduce code duplication
                private fun buildDatabase(context: Context, onReady: (() -> Unit?)? = null) =
                        Room.databaseBuilder(
                                context.applicationContext,
                                DbProductSource::class.java, "products.db"
                        )
                                .addCallback(object : Callback() {
                                        override fun onCreate(db: SupportSQLiteDatabase) {
                                                super.onCreate(db)
                                                ioThread {
                                                        getInstance(context).taxDao().insert(sampleData.sampleTaxes())
                                                        getInstance(context).productDao()
                                                                .insert(sampleData.sampleProducts())
                                                        getInstance(context).productTaxDao()
                                                                .insert(sampleData.sampleProductTax())
                                                        getInstance(context).basketDao()
                                                                .insert(sampleData.sampleBaskets())
                                                        onReady?.invoke()
                                                }
                                        }
                                        override fun onOpen(db: SupportSQLiteDatabase) {
                                                super.onOpen(db)
                                                onReady?.invoke()
                                        }
                                })
                                .build()

                private fun buildInMemoryDatabase(context: Context) =
                        Room.inMemoryDatabaseBuilder(
                                context.applicationContext,
                                DbProductSource::class.java
                        ).build()
        }

        override fun allBasketsNames(): List<String> {
                return basketDao().getBasketNames()
        }
        override fun allProductNames(): List<String> {
                return productDao().getProductNames()
        }

        override fun product(named: String): Product {
                return productDao().getProduct(named)
        }
        override fun taxesForProduct(named: String): List<Tax> {
                return productTaxDao().getProductTax(named).map {
                        taxDao().getTax(it.tax)
                }
        }

        override fun basket(named: String): Basket {

                return Basket(named, basketDao().getBasketContents(named).map {
                        BasketItem(productDao().getProduct(it.product), it.quantity, taxesForProduct(it.product))
                })
        }
}
