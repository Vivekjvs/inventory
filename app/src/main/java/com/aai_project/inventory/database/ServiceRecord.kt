package com.aai_project.inventory.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class ServiceRecord(
    @PrimaryKey(autoGenerate = true) val recordId: Int,
    val ownerId: Int,
    val technicianName: String,
    val dateOfService: Date
)