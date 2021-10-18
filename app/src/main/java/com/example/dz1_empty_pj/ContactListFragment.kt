package com.example.dz1_empty_pj

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView


class ContactListFragment : Fragment() {

    companion object{
        fun inst(): ContactListFragment {
            val args = Bundle()
            val fragment = ContactListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_list_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cardView: CardView = view.findViewById(R.id.cardView)
        requireActivity().setTitle(R.string.contact_list)
        cardView.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_fragment, ContactDetailsFragment.inst())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
