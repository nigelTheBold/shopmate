package org.paperdove.shopmate.data.model

import android.content.Context
import androidx.room.*
import org.paperdove.shopmate.data.DbProductSource

@Entity(tableName = "basket",
    primaryKeys = ["basket", "product"],
    indices = [Index("basket"), Index("product")],
    foreignKeys = [ForeignKey(
        entity = DbProduct::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("product"))]
)
data class DbBasketContents (
    @ColumnInfo(name = "basket") var name: String,
    var product: String,
    var quantity: Int
)

@Dao
abstract class BasketDao: BaseDao<DbBasketContents> {
    @Query("SELECT distinct(basket) FROM basket")
    abstract fun getBasketNames(): List<String>

    @Query("SELECT * from basket WHERE basket = :basket")
    abstract fun getBasketContents(basket: String): List<DbBasketContents>
}
