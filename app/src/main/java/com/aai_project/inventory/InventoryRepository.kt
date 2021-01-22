package com.aai_project.inventory

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.room.Room
import com.aai_project.inventory.database.Equipment
import com.aai_project.inventory.database.InventoryDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import java.util.concurrent.Executors

class InventoryRepository private constructor(context: Context) {
    private val database:InventoryDatabase = Room.databaseBuilder(
        context,
        InventoryDatabase::class.java,
        name
    ).build()

    val dao = database.getDao()
    private val thread = Executors.newSingleThreadExecutor()

    companion object{
        private const val name = "inventory_DB"
        private var repository:InventoryRepository? = null
        fun initialize(context: Context){
            if (repository == null){
                repository = InventoryRepository(context)
            }
        }
        fun get() = repository!!
    }

    fun updateEquipment(obj: Equipment){
        thread.execute {
            dao.updateEquipment(obj)
        }
    }
    fun insertEquipment(obj: Equipment){
        thread.execute {
            dao.insertEquipment(obj)
        }
    }

    fun deleteEquipment(obj: Equipment){
        thread.execute{
            dao.deleteEquipment(obj)
        }
    }

    fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d(TAG, "generateQRCode: ${e.message}")
        }
        return bitmap
    }

}