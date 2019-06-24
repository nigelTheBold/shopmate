package org.paperdove.shopmate.data.model

import androidx.room.*
import org.paperdove.shopmate.data.ProductSource


@Entity(tableName = "basket")
data class DbBasket (
    @PrimaryKey override var name: String,
    override var open: Boolean,
    @Ignore override var source: ProductSource? = null
): Basket {
    constructor() : this("EmptyBasket", false)

    @Ignore var _items: List<BasketItem>? = null
    private fun getContents(): List<BasketItem> {
        if (_items == null) {
            _items = source?.contentsOf(this)
            _items?.forEach { if (it is DbBasketItem) {it.setSource(source)} }
        }
        return _items ?: ArrayList()
    }

    override var items: List<BasketItem>
        get() = getContents()
        set(value) { _items = value }

    fun setSource(dbSource: ProductSource?): DbBasket {
        source = dbSource
        _items?.forEach { if (it is DbBasketItem) {it.setSource(source)} }
        return this
    }
}

@Dao
abstract class BasketDao: BaseDao<DbBasket> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(basket: DbBasket)

    @Query("SELECT name FROM basket")
    abstract fun getBasketNames(): List<String>

    @Query("SELECT * from basket WHERE name = :basket")
    abstract fun getBasket(basket: String): DbBasket

    @Query("SELECT * from basket")
    abstract fun getAllBaskets(): List<DbBasket>

    @Query("SELECT COUNT(*) FROM basket")
    abstract fun getBasketCount(): Int
}

@Entity(tableName = "basket_item",
    primaryKeys = ["basket", "product"],
    indices = [Index("basket"), Index("product")],
    foreignKeys = [ForeignKey(
        entity = DbProduct::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("product"))]
)
data class DbBasketItem (
    @ColumnInfo(name = "basket") var name: String,
    @ColumnInfo(name = "product") var productName: String,
    override var quantity: Int,
    @Ignore override var source: ProductSource? = null
): BasketItem {
    constructor() : this("EmptyBasket", "NoProduct", 0)

    @Ignore var _product: Product? = null

    fun getProductFromDb(): Product {
        if (_product == null) {
            _product = source?.product(productName)
        }

        return _product ?: object : Product {
            override val name: String = "NONE"
            override val price: Float = 0f
        }
    }

    override val product: Product
        get() = getProductFromDb()

    override val taxes: List<Tax>
        get() = source?.taxesForProduct(productName) ?: ArrayList()

    fun setSource(dbSource: ProductSource?): DbBasketItem {
        source = dbSource
        return this
    }
}

@Dao
abstract class BasketContentsDao: BaseDao<DbBasketItem> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(contents: DbBasketItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertContents(contents: List<DbBasketItem>)

    @Query("SELECT distinct(basket) FROM basket_item")
    abstract fun getBasketNames(): List<String>

    @Query("SELECT * from basket_item WHERE basket = :basket")
    abstract fun getBasketContents(basket: String): List<DbBasketItem>
}
