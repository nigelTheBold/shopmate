package org.paperdove.shopmate.data.model

import androidx.room.*

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<T>)
}

//----------------------------------------------------------------------
//--- DbTax ------------------------------------------------------------
@Entity(tableName = "tax",
    indices = [Index("name")])
data class DbTax(
    @PrimaryKey override val name: String,
    override val rate: Float,
    @ColumnInfo (name = "round_to") override val roundTo: Float
): Tax

@Dao
interface TaxDao: BaseDao<DbTax> {
    @Query("SELECT * FROM tax")
    fun getAll(): List<DbTax>

    @Query("SELECT * FROM tax WHERE name = :tax")
    fun getTax(tax: String): DbTax

}


//----------------------------------------------------------------------
//--- DbProduct --------------------------------------------------------
@Entity(tableName = "product",
    indices = [Index("name")])
data class DbProduct (
    @PrimaryKey override val name: String,
    override val price: Float
): Product

@Dao
interface ProductDao: BaseDao<DbProduct> {
    @Query("SELECT * FROM product")
    fun getAll(): List<DbProduct>

    @Query("SELECT name FROM product")
    fun getProductNames(): List<String>

    @Query("SELECT * FROM product WHERE name = :product")
    fun getProduct(product: String): DbProduct
}

//----------------------------------------------------------------------
//--- DbProductTax -----------------------------------------------------
@Entity(tableName = "product_tax",
    primaryKeys = ["product", "tax"],
    indices = [Index("product"), Index("tax")],
    foreignKeys = [
        ForeignKey(
            entity = DbProduct::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("product")),
        ForeignKey(
            entity = DbTax::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("tax"))
    ])

data class DbProductTax (
    override val product: String,
    override val tax: String
): ProductTax

@Dao
interface ProductTaxDao: BaseDao<DbProductTax> {
    @Query("SELECT * FROM product_tax")
    fun getAll(): List<DbProductTax>

    @Query("SELECT * FROM product_tax WHERE product = :product")
    fun getProductTax(product: String): List<DbProductTax>
}

