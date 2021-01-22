package com.aai_project.inventory

import androidx.lifecycle.ViewModel
import com.aai_project.inventory.database.Equipment


class EquipmentGenViewModel:ViewModel() {
    val equipment= Equipment(serialNumber = 0,equipmentName = "")
}