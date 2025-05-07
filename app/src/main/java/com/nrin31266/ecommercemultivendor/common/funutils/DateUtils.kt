package com.nrin31266.ecommercemultivendor.common.funutils

import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {
    fun timeAgo(date: Date): String {
        val now = Date()
        val diffInMillis = now.time - date.time
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)

        val years = seconds / (365 * 24 * 60 * 60)
        if (years > 1) return "$years years ago"
        if (years == 1L) return "1 year ago"

        val months = seconds / (30 * 24 * 60 * 60)
        if (months > 1) return "$months months ago"
        if (months == 1L) return "1 month ago"

        val days = seconds / (24 * 60 * 60)
        if (days > 1) return "$days days ago"
        if (days == 1L) return "1 day ago"

        val hours = seconds / (60 * 60)
        if (hours > 1) return "$hours hours ago"
        if (hours == 1L) return "1 hour ago"

        val minutes = seconds / 60
        if (minutes > 1) return "$minutes minutes ago"
        if (minutes == 1L) return "1 minute ago"

        return if (seconds > 1) "$seconds seconds ago" else "Just now"
    }


    fun timeAgo(dateString: String): String {
        val date = parseCreatedAt(dateString)
        return timeAgo(date)
    }

    private fun parseCreatedAt(isoDate: String): Date {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            format.isLenient = false
            format.parse(isoDate) ?: Date()
        } catch (e: Exception) {
            e.printStackTrace()
            Date() // fallback nếu parse lỗi
        }
    }
}