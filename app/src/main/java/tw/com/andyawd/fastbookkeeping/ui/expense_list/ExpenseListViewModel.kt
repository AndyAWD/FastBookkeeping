package tw.com.andyawd.fastbookkeeping.ui.expense_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tw.com.andyawd.fastbookkeeping.data.database.ExpenseWithCategory
import tw.com.andyawd.fastbookkeeping.data.repository.ExpenseRepository
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    val expenses: StateFlow<List<ExpenseWithCategory>> =
        expenseRepository.getAllExpensesWithCategory()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun deleteExpense(expenseWithCategory: ExpenseWithCategory) {
        viewModelScope.launch {
            expenseRepository.deleteExpense(expenseWithCategory.expense)
        }
    }
}
