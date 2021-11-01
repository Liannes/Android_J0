package com.example.dz1_empty_pj

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.cardview.widget.CardView

class ContactListFragment : Fragment() {

    private var contactInterface: ContactInterface? = null

    companion object {
        fun newInstance() = Fragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactInterface) {
            contactInterface= context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val cardView: CardView = view.findViewById(R.id.cardView)
        toolbar.title = getString(R.string.contactList)
        cardView.setOnClickListener {
            contactInterface?.navigationToContactDetailsFragment("1")
        }
    }
}
