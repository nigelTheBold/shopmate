package org.paperdove.shopmate

import android.app.Application
import org.paperdove.shopmate.data.DbProductSource
import org.paperdove.shopmate.data.ProductSource
import kotlin.concurrent.thread

class ShopMateApp: Application() {
    companion object {
        var init = false
        var dataReadyNotification: (() -> Unit?)? = null
            get() = field
            set(notification) {
                if (init) {
                    notification?.invoke()
                }
                field = notification
            }
        var sources = ArrayList<ProductSource>()
        lateinit var dbSource: DbProductSource
    }

    override fun onCreate() {
        super.onCreate()
        thread {
            dbSource = DbProductSource.getInstance(this) {
                init = true
                dataReadyNotification?.invoke()
            }

            dbSource.basketContentsDao().getBasketNames()
        }
    }
}