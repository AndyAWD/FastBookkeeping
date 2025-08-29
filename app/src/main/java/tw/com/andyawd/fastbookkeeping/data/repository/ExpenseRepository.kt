package tw.com.andyawd.fastbookkeeping.data.repository

import kotlinx.coroutines.flow.Flow
import tw.com.andyawd.fastbookkeeping.data.database.Category
import tw.com.andyawd.fastbookkeeping.data.database.CategoryDao
import tw.com.andyawd.fastbookkeeping.data.database.Expense
import tw.com.andyawd.fastbookkeeping.data.database.ExpenseDao
import tw.com.andyawd.fastbookkeeping.data.database.ExpenseWithCategory

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) {
    fun getAllExpenses(): Flow<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesWithCategory()
    }

    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

    suspend fun insertExpense(expense: Expense) {
        expenseDao.insert(expense)
    }

    suspend fun insertCategory(category: Category): Long {
        return categoryDao.insert(category)
    }
}
