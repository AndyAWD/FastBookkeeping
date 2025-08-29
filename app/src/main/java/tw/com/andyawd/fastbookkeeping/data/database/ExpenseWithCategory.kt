package tw.com.andyawd.fastbookkeeping.data.database

import androidx.room.Embedded
import androidx.room.Relation

data class ExpenseWithCategory(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: Category
)
