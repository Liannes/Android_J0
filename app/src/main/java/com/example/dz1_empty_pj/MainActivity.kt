package com.example.dz1_empty_pj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContactListFragment()
    }

    private fun setContactListFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.main_fragment, ContactListFragment.inst())
        transaction.commit()

    }

    private fun setContactDetailsFragment(){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.main_fragment, ContactDetailsFragment.inst())
        transaction.commit()

    }
}