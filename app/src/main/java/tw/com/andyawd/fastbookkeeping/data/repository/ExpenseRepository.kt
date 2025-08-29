package tw.com.andyawd.fastbookkeeping.data.repository

import tw.com.andyawd.fastbookkeeping.data.database.Category
import tw.com.andyawd.fastbookkeeping.data.database.CategoryDao
import tw.com.andyawd.fastbookkeeping.data.database.Currency
import tw.com.andyawd.fastbookkeeping.data.database.CurrencyDao
import tw.com.andyawd.fastbookkeeping.data.database.Expense
import tw.com.andyawd.fastbookkeeping.data.database.ExpenseDao
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val currencyDao: CurrencyDao
) {
    fun getAllExpensesWithCategory() = expenseDao.getAllExpensesWithCategory()

    suspend fun insertExpense(expense: Expense) {
        expenseDao.insert(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.delete(expense)
    }

    fun getAllCategories() = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(category)
    }



    fun getAllCurrencies() = currencyDao.getAllCurrencies()

    suspend fun insertCurrency(currency: Currency): Long {
        return currencyDao.insertCurrency(currency)
    }

    suspend fun getLatestExpenseCurrency(): String? {
        return expenseDao.getLatestExpense()?.localCurrency
    }

    suspend fun getLatestExpenseCategoryId(): Long? {
        return expenseDao.getLatestExpense()?.categoryId
    }

    suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoryById(id)
    }
}