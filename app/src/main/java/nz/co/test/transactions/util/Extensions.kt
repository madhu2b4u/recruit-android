package nz.co.test.transactions.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toReadableString(): String {
    val localDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm a")
    return localDateTime.format(formatter)
}