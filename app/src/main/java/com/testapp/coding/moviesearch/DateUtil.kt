package com.testapp.coding.moviesearch

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        fun formatReleaseDate(dateValue: String): String {
            return DateFormat.getDateInstance().format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateValue))
        }
    }
}