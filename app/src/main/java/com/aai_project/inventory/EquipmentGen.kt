package com.aai_project.inventory

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aai_project.inventory.database.Equipment
import java.util.*

class EquipmentGen:Fragment(),DatePickerDialog.OnDateSetListener {

    private lateinit var equipment:Equipment
    private lateinit var serialText: TextView
    private lateinit var nameText: TextView
    private lateinit var dateBtn: Button
    private lateinit var generateBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var qrImage: ImageView
    private val viewModel:EquipmentGenViewModel by lazy {
        ViewModelProvider(this).get(EquipmentGenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        equipment = viewModel.equipment


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_equipment,container,false)

    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        serialText = v.findViewById(R.id.text_serial)
        nameText = v.findViewById(R.id.text_equipment_name)
        generateBtn = v.findViewById(R.id.btn_generate)
        qrImage = v.findViewById(R.id.qrImage)
        dateBtn = v.findViewById<Button>(R.id.btn_date).apply{
            text = equipment.dateOfInstallation.toString()
        }
        saveBtn = v.findViewById(R.id.btn_save)

    }

    override fun onStart() {
        super.onStart()
        serialText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()){
                    equipment.serialNumber = s.toString().toInt()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        nameText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                equipment.equipmentName = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        dateBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(),this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        saveBtn.setOnClickListener{
            if(nameText.text.isNotEmpty() && serialText.text.isNotEmpty()) {
                InventoryRepository.get().insertEquipment(equipment)
                Toast.makeText(
                        requireContext(),
                        R.string.equipment_saved,
                        Toast.LENGTH_LONG
                ).show()
                requireActivity().supportFragmentManager.popBackStackImmediate()
            }
        }
        generateBtn.setOnClickListener {
            val bitmap = InventoryRepository.get().generateQRCode(equipment.equipmentId.toString())
            qrImage.setImageBitmap(bitmap)
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val inMillis = Calendar.getInstance().apply {
            set(year,month,dayOfMonth)
        }.timeInMillis
        equipment.dateOfInstallation = Date(inMillis)
        dateBtn.text = equipment.dateOfInstallation.toString()
    }




}