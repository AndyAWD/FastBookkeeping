package tw.com.andyawd.fastbookkeeping.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val description: String,

    @ColumnInfo(name = "local_amount")
    val localAmount: BigDecimal,

    @ColumnInfo(name = "local_currency")
    val localCurrency: String,

    @ColumnInfo(name = "twd_amount")
    val twdAmount: BigDecimal,

    @ColumnInfo(name = "transaction_date_time")
    val transactionDateTime: String,

    @ColumnInfo(name = "category_id", index = true)
    val categoryId: Long
)
