package tw.com.andyawd.fastbookkeeping.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object AddEditExpense : Screen("add_edit_expense", "記帳", Icons.Filled.Edit)
    object ExpenseList : Screen("expense_list", "列表", Icons.Filled.List)
}
