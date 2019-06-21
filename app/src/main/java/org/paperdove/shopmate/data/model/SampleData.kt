package org.paperdove.shopmate.data.model

class SampleData {
    val basicTax = DbTax( "Basic Tax", 10f, 0.05f)
    val importTax = DbTax("Import Duty", 5f, 0.05f)

    val skittles = DbProduct("16lb bag of Skittles",16.000f)
    val walkman = DbProduct("Walkman", 99.990f)
    val popcorn = DbProduct("bag of microwave Popcorn", 0.990f)
    val vanillaHazelnutCoffee = DbProduct("imported bag of Vanilla-Hazelnut Coffee", price = 11.00f)
    val vespa = DbProduct("Imported Vespa", 15001.25f)
    val almondSnickers = DbProduct("imported crate of Almond Snickers",75.99f)
    val discman = DbProduct("Discman", 55.00f)
    val wine = DbProduct("Imported Bottle of Wine", 10.00f)
    val fairTradeCoffee = DbProduct("300# bag of Fair-Trade Coffee", 997.99f)

    fun sampleTaxes(): List<DbTax> {
        return listOf(basicTax, importTax)
    }

    fun sampleProducts(): List<DbProduct> {
        return listOf(skittles, walkman, popcorn, vanillaHazelnutCoffee,
            vespa, almondSnickers, discman, wine, fairTradeCoffee)
    }

    fun sampleProductTax(): List<DbProductTax> {
        return listOf(
            DbProductTax(walkman.name, basicTax.name),
            DbProductTax(vanillaHazelnutCoffee.name, importTax.name),
            DbProductTax(vespa.name, basicTax.name),
            DbProductTax(vespa.name, importTax.name),
            DbProductTax(almondSnickers.name, importTax.name),
            DbProductTax(discman.name, basicTax.name),
            DbProductTax(wine.name, basicTax.name),
            DbProductTax(wine.name, importTax.name)
        )
    }

    fun sampleBaskets(): List<DbBasket> {
        return listOf(
            DbBasket("Shopping Basket 1", true),
            DbBasket("Shopping Basket 2", true),
            DbBasket("Shopping Basket 3", true)
        )
    }

    fun sampleBasketItems(): List<DbBasketItem> {
        return listOf(
            // Basket 1
            DbBasketItem("Shopping Basket 1", skittles.name, 1),
            DbBasketItem("Shopping Basket 1", walkman.name, 1),
            DbBasketItem("Shopping Basket 1", popcorn.name, 1),

            // Basket 2
            DbBasketItem("Shopping Basket 2", vanillaHazelnutCoffee.name, 1),
            DbBasketItem("Shopping Basket 2", vespa.name, 1),

            // Basket 3
            DbBasketItem("Shopping Basket 3", almondSnickers.name, 1),
            DbBasketItem("Shopping Basket 3", discman.name, 1),
            DbBasketItem("Shopping Basket 3", wine.name, 1),
            DbBasketItem("Shopping Basket 3", fairTradeCoffee.name, 1)
        )
    }
}