package tw.com.andyawd.fastbookkeeping.ui.expense_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
            // Parse ISO string and format to a simple date string for grouping
            OffsetDateTime.parse(it.expense.transactionDateTime)
                .toLocalDate()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        }

        LazyColumn(modifier = Modifier.padding(8.dp)) {
            groupedExpenses.forEach { (date, expensesOnDate) ->
                stickyHeader {
                    DateHeader(date = date)
                }
                items(expensesOnDate) { expenseItem ->
                    ExpenseListItem(item = expenseItem)
                }
            }
        }
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


@Composable
fun ExpenseListItem(item: ExpenseWithCategory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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