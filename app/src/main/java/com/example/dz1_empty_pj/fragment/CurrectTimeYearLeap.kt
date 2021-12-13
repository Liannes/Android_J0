package com.example.dz1_empty_pj.fragment

import java.text.SimpleDateFormat
import java.util.*

fun currectData(date: GregorianCalendar): String {
    val format = SimpleDateFormat("dd.MMM")
    format.calendar = date
    return format.format(date.time)
}
