package com.aai_project.inventory.database

import androidx.room.TypeConverter
import java.util.*

class InventoryTypeConverter {
    @TypeConverter
    fun fromDate(date: Date?):Long?{
        return date?.time
    }

    @TypeConverter
    fun toDate(time :Long?):Date?{
        return time?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun fromUUID(uuid: UUID):String{
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(string: String):UUID{
        return UUID.fromString(string)
    }
}