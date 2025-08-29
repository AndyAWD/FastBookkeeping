package tw.com.andyawd.fastbookkeeping.ui.add_edit_expense

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tw.com.andyawd.fastbookkeeping.data.database.Category
import tw.com.andyawd.fastbookkeeping.data.database.Currency

@Composable
fun AddEditExpenseScreen(
    viewModel: AddEditExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val currencies by viewModel.currencies.collectAsState()
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar("儲存成功！")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("品項描述") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = uiState.localAmount,
                    onValueChange = viewModel::onLocalAmountChange,
                    label = { Text("當地金額") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = uiState.localCurrency,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("幣別") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showCurrencyDialog = true }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.twdAmount,
                onValueChange = viewModel::onTwdAmountChange,
                label = { Text("台幣金額") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Category Selector
            Box {
                OutlinedTextField(
                    value = uiState.selectedCategory?.name ?: "選擇分類",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("分類") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showCategoryDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = viewModel::saveExpense,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("儲存")
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }


    if (showCategoryDialog) {
        CategorySelectionDialog(
            categories = categories,
            onDismissRequest = { showCategoryDialog = false },
            onCategorySelected = {
                viewModel.onCategorySelected(it)
                showCategoryDialog = false
            },
            onAddNewCategory = viewModel::onAddNewCategory
        )
    }

    if (showCurrencyDialog) {
        CurrencySelectionDialog(
            currencies = currencies,
            onDismissRequest = { showCurrencyDialog = false },
            onCurrencySelected = {
                viewModel.onCurrencySelected(it)
                showCurrencyDialog = false
            },
            onAddNewCurrency = viewModel::onAddNewCurrency
        )
    }
}

@Composable
private fun CurrencySelectionDialog(
    currencies: List<Currency>,
    onDismissRequest: () -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    onAddNewCurrency: (String) -> Unit
) {
    var showAddCurrencyField by remember { mutableStateOf(false) }
    var newCurrencyName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("選擇幣別") },
        text = {
            Column {
                LazyColumn {
                    items(currencies) { currency ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = currency.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onCurrencySelected(currency) }
                                    .padding(vertical = 12.dp)
                            )
                            Divider()
                        }
                    }
                }
                if (showAddCurrencyField) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newCurrencyName,
                            onValueChange = { newCurrencyName = it },
                            label = { Text("新幣別名稱") },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            if (newCurrencyName.isNotBlank()) {
                                onAddNewCurrency(newCurrencyName)
                                newCurrencyName = ""
                                showAddCurrencyField = false
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "新增")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { showAddCurrencyField = true }) {
                Text("新增幣別")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun CategorySelectionDialog(
    categories: List<Category>,
    onDismissRequest: () -> Unit,
    onCategorySelected: (Category) -> Unit,
    onAddNewCategory: (String) -> Unit
) {
    var showAddCategoryField by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("選擇分類") },
        text = {
            Column {
                LazyColumn {
                    items(categories) { category ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = category.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onCategorySelected(category) }
                                    .padding(vertical = 12.dp)
                            )
                            Divider()
                        }
                    }
                }
                if (showAddCategoryField) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newCategoryName,
                            onValueChange = { newCategoryName = it },
                            label = { Text("新分類名稱") },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            if (newCategoryName.isNotBlank()) {
                                onAddNewCategory(newCategoryName)
                                newCategoryName = ""
                                showAddCategoryField = false
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "新增")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { showAddCategoryField = true }) {
                Text("新增分類")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("取消")
            }
        }
    )
}