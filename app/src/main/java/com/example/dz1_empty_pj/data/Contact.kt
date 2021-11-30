package com.example.dz1_empty_pj.data

import java.util.*

data class Contact(
    val contactId: String,
    val name: String,
    val firstMobile: String,
    val secondMobile: String,
    val firstEmail: String,
    val secondEmail: String,
    val description: String,
    val avatarUri: Int,
    val birthDay: GregorianCalendar
)