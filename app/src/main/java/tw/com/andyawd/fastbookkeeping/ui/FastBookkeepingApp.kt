package tw.com.andyawd.fastbookkeeping.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import tw.com.andyawd.fastbookkeeping.ui.add_edit_expense.AddEditExpenseScreen
import tw.com.andyawd.fastbookkeeping.ui.expense_list.ExpenseListScreen
import tw.com.andyawd.fastbookkeeping.ui.navigation.Screen

@Composable
fun FastBookkeepingApp() {
    val navController = rememberNavController()
    val navItems = listOf(Screen.AddEditExpense, Screen.ExpenseList)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.AddEditExpense.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.AddEditExpense.route) { AddEditExpenseScreen() }
            composable(Screen.ExpenseList.route) { ExpenseListScreen() }
        }
    }
}
