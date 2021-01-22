package com.aai_project.inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate

import java.util.*

class MainActivity : AppCompatActivity(),EquipmentListFragment.Navigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (supportFragmentManager.findFragmentById(R.id.container) == null){
            val f = EquipmentListFragment()
            f.setHasOptionsMenu(true)
            supportFragmentManager.beginTransaction()
                    .add(R.id.container,f)
                    .commit()
        }

    }

    override fun onNavigate(id: String) {
        val f = EquipmentFragment()
        f.arguments = Bundle().apply {
            putString(EquipmentFragment.KEY_EQUIPMENT_ID,id)
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.container,f)
                .addToBackStack(null)
                .commit()
    }

    override fun onAdd() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container,EquipmentGen())
                .addToBackStack(null)
                .commit()
    }


}