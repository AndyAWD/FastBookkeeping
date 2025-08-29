package tw.com.andyawd.fastbookkeeping.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Transaction
    @Query("SELECT * FROM expenses ORDER BY transaction_date_time DESC")
    fun getAllExpensesWithCategory(): Flow<List<ExpenseWithCategory>>
}
