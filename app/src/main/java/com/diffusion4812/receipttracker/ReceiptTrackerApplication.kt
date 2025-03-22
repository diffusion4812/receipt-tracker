package com.diffusion4812.receipttracker

import android.app.Application
import com.diffusion4812.receipttracker.data.AppContainer
import com.diffusion4812.receipttracker.data.AppDataContainer

class ReceiptTrackerApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}