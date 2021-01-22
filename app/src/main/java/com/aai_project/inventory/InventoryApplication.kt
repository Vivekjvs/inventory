package com.aai_project.inventory

import android.app.Application

class InventoryApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        InventoryRepository.initialize(this)
    }
}