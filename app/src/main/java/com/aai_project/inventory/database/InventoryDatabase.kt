package com.aai_project.inventory.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Equipment::class,ServiceRecord::class],version = 1)
@TypeConverters(InventoryTypeConverter::class)
abstract class InventoryDatabase:RoomDatabase() {

    abstract fun getDao():InventoryDao

}