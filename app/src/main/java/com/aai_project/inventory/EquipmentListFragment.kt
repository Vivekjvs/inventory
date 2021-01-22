package com.aai_project.inventory

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aai_project.inventory.database.Equipment
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class EquipmentListFragment:Fragment() {

    private val viewModel:EquipmentListViewModel by lazy {
        ViewModelProvider(this).get(EquipmentListViewModel::class.java)
    }

    private lateinit var adapter:EquipmentAdapter
    private lateinit var qrScanner:IntentIntegrator // zxing
    private var navigation: Navigation? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation = context as Navigation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        qrScanner = IntentIntegrator.forSupportFragment(this)
        qrScanner.setCaptureActivity(OrientationCaptureActivity::class.java)
        qrScanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        qrScanner.setOrientationLocked(false)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_equipment_list,container,false)
    }

    override fun onDetach() {
        super.onDetach()
        navigation = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.fragment_equipment_list,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.scan_equipment){
            qrScanner.initiateScan()
            return true
        }else if(item.itemId == R.id.add_equipment){
            navigation?.onAdd()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if (resultCode == Activity.RESULT_OK && result != null) {
            if (result.contents == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.result_not_found),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                try {
                    val obj = JSONObject(result.contents)
                    val a = obj.getString(EquipmentFragment.KEY_EQUIPMENT_ID)
                    navigation?.onNavigate(a)
                } catch (e: JSONException){
                    e.printStackTrace()
                    Toast.makeText(requireContext(), result.contents, Toast.LENGTH_LONG).show()
                } catch (e: Exception){
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.result_not_found),
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = EquipmentAdapter(listOf())
        recyclerView.adapter = adapter
        viewModel.equipmentList.observe(viewLifecycleOwner,{ list ->
            list?.let{
                adapter.list = it
                adapter.notifyDataSetChanged()
            }
        })
    }


    private inner class EquipmentAdapter(var list:List<Equipment>):RecyclerView.Adapter<EquipmentItemHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentItemHolder {
            val view = requireActivity().layoutInflater.inflate(R.layout.item_equipment, parent,false)
            return EquipmentItemHolder(view)
        }

        override fun onBindViewHolder(holder: EquipmentItemHolder, position: Int) {
            holder.build(list[position])
        }

        override fun getItemCount() = list.size

    }

    private inner class EquipmentItemHolder(view: View): RecyclerView.ViewHolder(view) {
        private val nameText = itemView.findViewById<TextView>(R.id.name)
        private val serialText = itemView.findViewById<TextView>(R.id.serial_number)
        private val dateText = itemView.findViewById<TextView>(R.id.installed_date)
        private var equipment:Equipment? = null

        init {
            itemView.setOnClickListener {
                navigation?.onNavigate(equipment?.equipmentId.toString())
            }

            itemView.setOnLongClickListener {
                equipment?.let {
                    InventoryRepository.get().deleteEquipment(it)
                }
                adapter.notifyDataSetChanged()
                true
            }
        }

        fun build(obj: Equipment){
            equipment = obj
            nameText.text = obj.equipmentName
            serialText.text = "Serial# " + obj.serialNumber
            val c = Calendar.getInstance()
            c.time = obj.dateOfInstallation
            val d = c.get(Calendar.DAY_OF_MONTH).toString()+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR)
            dateText.text = d
        }

    }



    interface Navigation{
        fun onNavigate(id: String)
        fun onAdd()
    }


}