package tw.com.andyawd.fastbookkeeping.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tw.com.andyawd.fastbookkeeping.data.database.AppDatabase
import tw.com.andyawd.fastbookkeeping.data.database.CategoryDao
import tw.com.andyawd.fastbookkeeping.data.database.CurrencyDao
import tw.com.andyawd.fastbookkeeping.data.database.ExpenseDao
import tw.com.andyawd.fastbookkeeping.data.repository.ExpenseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideExpenseDao(appDatabase: AppDatabase): ExpenseDao {
        return appDatabase.expenseDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }

    @Singleton
    @Provides
    fun provideExpenseRepository(
        expenseDao: ExpenseDao,
        categoryDao: CategoryDao,
        currencyDao: CurrencyDao
    ): ExpenseRepository {
        return ExpenseRepository(expenseDao, categoryDao, currencyDao)
    }
}


