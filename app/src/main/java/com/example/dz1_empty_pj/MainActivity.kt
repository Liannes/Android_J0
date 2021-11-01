package com.example.dz1_empty_pj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar

class MainActivity : AppCompatActivity(), ContactInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            navigationToContactListFragment()
        }
    }

    private fun navigationToContactListFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.main_fragment, ContactListFragment.newInstance())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun navigationToContactDetailsFragment(CONTACT_ID: String){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment, ContactDetailsFragment.newInstance(CONTACT_ID))
        transaction.addToBackStack("DetailContact")
        transaction.commit()
    }
}