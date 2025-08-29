package tw.com.andyawd.fastbookkeeping.ui.expense_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tw.com.andyawd.fastbookkeeping.data.database.ExpenseWithCategory
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpenseListScreen(
    viewModel: ExpenseListViewModel = hiltViewModel()
) {
    val expenses by viewModel.expenses.collectAsState()
    var expenseToDelete by remember { mutableStateOf<ExpenseWithCategory?>(null) }

    val showDeleteDialog = expenseToDelete != null

    if (expenses.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("尚無支出紀錄")
        }
    } else {
        val groupedExpenses = expenses.groupBy {
            OffsetDateTime.parse(it.expense.transactionDateTime)
                .toLocalDate()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        }

        LazyColumn(modifier = Modifier.padding(8.dp)) {
            groupedExpenses.forEach { (date, expensesOnDate) ->
                stickyHeader {
                    DateHeader(date = date)
                }
                items(
                    items = expensesOnDate,
                    key = { it.expense.id }
                ) { expenseItem ->
                    val overallIndex = expenses.indexOf(expenseItem) + 1
                    ExpenseListItem(
                        index = overallIndex,
                        item = expenseItem,
                        onLongClick = { expenseToDelete = expenseItem }
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { expenseToDelete = null },
            title = { Text("確認刪除") },
            text = { Text("您確定要刪除這筆消費紀錄嗎？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        expenseToDelete?.let {
                            viewModel.deleteExpense(it)
                        }
                        expenseToDelete = null
                    }
                ) {
                    Text("刪除")
                }
            },
            dismissButton = {
                TextButton(onClick = { expenseToDelete = null }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun DateHeader(date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = date, style = MaterialTheme.typography.titleMedium)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpenseListItem(
    index: Int,
    item: ExpenseWithCategory,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .combinedClickable(
                onClick = { /* For future use */ },
                onLongClick = onLongClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index.",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.expense.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = item.category.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "TWD ${item.expense.twdAmount.toPlainString()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${item.expense.localAmount.toPlainString()} ${item.expense.localCurrency}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
