package nz.co.test.transactions.main.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Represents a financial transaction.
 *
 * @property id The unique identifier for the transaction.
 * @property transactionDate The date and time when the transaction occurred, including timezone offset.
 * @property summary A brief description of the transaction.
 * @property debit The amount debited in the transaction, if applicable.
 * @property credit The amount credited in the transaction, if applicable.
 */
@Parcelize
data class Transaction(
    val id: Int,
    val transactionDate: String,
    val summary: String,
    val debit: Double,
    val credit: Double
) : Parcelable