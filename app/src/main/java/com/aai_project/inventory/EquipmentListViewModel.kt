package com.aai_project.inventory

import androidx.lifecycle.ViewModel
import com.aai_project.inventory.database.Equipment

class EquipmentListViewModel:ViewModel() {
    val equipmentList = InventoryRepository.get().dao.getEquipments()

}