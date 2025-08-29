package tw.com.andyawd.fastbookkeeping.ui.add_edit_expense

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tw.com.andyawd.fastbookkeeping.data.database.Category
import tw.com.andyawd.fastbookkeeping.data.database.Expense
import tw.com.andyawd.fastbookkeeping.data.repository.ExpenseRepository
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class AddEditExpenseUiState(
    val description: String = "",
    val localAmount: String = "",
    val localCurrency: String = "",
    val twdAmount: String = "",
    val selectedCategory: Category? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditExpenseUiState())
    val uiState: StateFlow<AddEditExpenseUiState> = _uiState.asStateFlow()

    val categories: StateFlow<List<Category>> = expenseRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onLocalAmountChange(newAmount: String) {
        if (newAmount.matches(Regex("^\\d*\\.?\\d*$"))) {
            _uiState.update { it.copy(localAmount = newAmount) }
        }
    }

    fun onLocalCurrencyChange(newCurrency: String) {
        _uiState.update { it.copy(localCurrency = newCurrency.uppercase()) }
    }

    fun onTwdAmountChange(newAmount: String) {
        if (newAmount.matches(Regex("^\\d*\\.?\\d*$"))) {
            _uiState.update { it.copy(twdAmount = newAmount) }
        }
    }

    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onAddNewCategory(categoryName: String) {
        viewModelScope.launch {
            val newCategory = Category(name = categoryName)
            val newId = expenseRepository.insertCategory(newCategory)
            onCategorySelected(newCategory.copy(id = newId))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveExpense() {
        val currentState = _uiState.value
        if (currentState.description.isBlank() ||
            currentState.localAmount.isBlank() ||
            currentState.twdAmount.isBlank() ||
            currentState.localCurrency.isBlank() ||
            currentState.selectedCategory == null
        ) {
            // Handle validation error
            return
        }

        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            try {
                val expense = Expense(
                    description = currentState.description,
                    localAmount = BigDecimal(currentState.localAmount),
                    localCurrency = currentState.localCurrency,
                    twdAmount = BigDecimal(currentState.twdAmount),
                    transactionDateTime = OffsetDateTime.now()
                        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    categoryId = currentState.selectedCategory.id
                )
                expenseRepository.insertExpense(expense)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveSuccess = true,
                        // Reset fields
                        description = "",
                        localAmount = "",
                        localCurrency = "",
                        twdAmount = "",
                        selectedCategory = null
                    )
                }
            } catch (e: Exception) {
                // Handle the error, e.g., show a snackbar
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveSuccess = false
                        // Optionally, you can add an error message to the state
                        // errorMessage = "儲存失敗: ${e.message}"
                    )
                }
            }
        }
    }
}
