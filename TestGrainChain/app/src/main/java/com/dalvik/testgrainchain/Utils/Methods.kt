package com.dalvik.testgrainchain.Utils

import java.text.SimpleDateFormat
import java.util.*

class Methods {

    companion object{
        private var calendar: Calendar = Calendar.getInstance()
        private var simpleDateFormat = SimpleDateFormat(Constants.FORMAT_DATE, Locale.getDefault())
        private var simpleTimeFormat = SimpleDateFormat(Constants.FORMAT_TIME, Locale.getDefault())

        fun getTimeFormat(time: String): String{
            calendar = Calendar.getInstance()
            calendar.timeInMillis = time.toLong()
            calendar.time
            return simpleDateFormat.format(calendar.time).toString()
        }

        fun getTimeDiff(timeStart: String, timeEnd: String): String{
            val calendarStart = Calendar.getInstance()
            calendarStart.timeInMillis = timeStart.toLong()
            val calendarEnd= Calendar.getInstance()
            calendarEnd.timeInMillis = timeEnd.toLong()
            val diffInMs: Long = (calendarEnd.time.time - calendarStart.time.time)
            calendar.timeInMillis = diffInMs
            return simpleTimeFormat.format(calendar.time).toString()
        }

    }

}