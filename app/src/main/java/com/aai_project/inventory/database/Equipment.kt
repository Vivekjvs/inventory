package com.aai_project.inventory.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Equipment(
    @PrimaryKey val equipmentId: UUID = UUID.randomUUID(),
    var serialNumber: Int,
    var equipmentName: String,
    var dateOfInstallation: Date = Date(),
)
