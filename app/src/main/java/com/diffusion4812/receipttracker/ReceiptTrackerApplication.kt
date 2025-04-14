package com.diffusion4812.receipttracker

import android.app.Application
import com.diffusion4812.receipttracker.data.AppContainer
import com.diffusion4812.receipttracker.data.AppDataContainer

class ReceiptTrackerApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
