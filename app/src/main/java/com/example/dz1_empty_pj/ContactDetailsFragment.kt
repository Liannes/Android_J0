package com.example.dz1_empty_pj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf


class ContactDetailsFragment : Fragment() {
    companion object {
        fun newInstance(contactID: String? = null): ContactDetailsFragment {
            val contactId = "CONTACT_ID"
            val args = Bundle()
            contactID?.let {
                args.putString(contactId, it)
            }
            val fragment = ContactDetailsFragment()
            fragment.arguments = args
            return fragment
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
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.contactDetails)
    }
}