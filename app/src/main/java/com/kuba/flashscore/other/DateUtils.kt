package com.kuba.flashscore.other

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormatMonthDayYear = SimpleDateFormat("MMMMM d, yyyy")

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormatDayMonthYear =
        SimpleDateFormat(Constants.DATE_FORMAT_DAY_MONTH_YEAR)

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormatDayOfWeek = SimpleDateFormat(Constants.DATE_FORMAT_DAY_OF_WEEK)

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormatYearMonthDay =
        SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY)

    @SuppressLint("SimpleDateFormat")
    fun formatDateCurrentDate(formatPattern: String): String {
        return SimpleDateFormat(formatPattern).format(Date(System.currentTimeMillis()))
    }

    @SuppressLint("SimpleDateFormat")
    fun parseAndFormatDate(
        date: String,
        formatPattern: String,
        returnedFormatPattern: String
    ): String {
        val formattedDate = SimpleDateFormat(formatPattern).parse(date)
        return SimpleDateFormat(returnedFormatPattern).format(formattedDate!!)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date, formatPattern: String) : String {
        return SimpleDateFormat(formatPattern).format(date)
    }
    @SuppressLint("SimpleDateFormat")
    fun parseDate(date: String, formatPattern: String) : Date {
        return SimpleDateFormat(formatPattern).parse(date)
    }
    fun longToDate(long: Long): Date {
        return Date(long)
    }

    fun dateToLong(date: Date): Long {
        return date.time / 1000 // return seconds
    }

    // Ex: November 4, 2021
    fun dateToString(date: Date): String {
        return simpleDateFormatMonthDayYear.format(date)
    }

    fun stringToDate(string: String): Date {
        return simpleDateFormatMonthDayYear.parse(string)
            ?: throw NullPointerException("Could not convert date string to Date object.")
    }

    fun createTimestamp(): Date {
        return Date()
    }
}