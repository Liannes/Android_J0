package com.example.dz1_empty_pj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.os.bundleOf


class ContactDetailsFragment : Fragment() {
    companion object {
        private const val CONTACT_ID = "contact_id"
        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(CONTACT_ID, contactId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.contactDetails)
    }
}