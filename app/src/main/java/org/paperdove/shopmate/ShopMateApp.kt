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
        lateinit var productSource: ProductSource
    }

    override fun onCreate() {
        super.onCreate()
        thread {
            productSource = DbProductSource.getInstance(this) {
                init = true
                dataReadyNotification?.invoke()
            }

            productSource.allBasketsNames()
        }
    }
}