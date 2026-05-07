package com.nammapustaka.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nammapustaka.ui.BookViewModel

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Shelf : BottomNavItem("shelf", Icons.Filled.Info, "Library")
    object Scan : BottomNavItem("scan", Icons.Filled.Search, "Scan")
    object Leaderboard : BottomNavItem("leaderboard", Icons.Filled.Star, "Leaderboard")
    object Profile : BottomNavItem("profile", Icons.Filled.Person, "Profile")
    object AddBook : BottomNavItem("add_book", Icons.Filled.Add, "Add Book")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: BookViewModel) {
    val navController = rememberNavController()
    val userRole by viewModel.userRole.collectAsState()
    val currentStudent by viewModel.currentStudent.collectAsState()

    val navItems = mutableListOf(
        BottomNavItem.Shelf,
        BottomNavItem.Leaderboard,
        BottomNavItem.Profile
    )
    
    if (userRole == "admin") {
        navItems.add(BottomNavItem.AddBook)
    } else {
        navItems.add(1, BottomNavItem.Scan)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = if (userRole == "admin") "Hi, Librarian \uD83D\uDC4B" else "Hi, ${currentStudent?.name ?: "Student"} \uD83D\uDC4B",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (userRole == "admin") "Admin Mode" else "Student Mode",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (userRole == "admin") "ADMIN" else "STUDENT", style = MaterialTheme.typography.labelSmall)
                        Switch(
                            checked = userRole == "admin",
                            onCheckedChange = { viewModel.toggleRole() },
                            modifier = Modifier.padding(horizontal = 4.dp),
                            thumbContent = { Icon(Icons.Filled.Person, contentDescription = "Role") }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Shelf.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Shelf.route) { ShelfScreen(viewModel) }
            composable(BottomNavItem.Scan.route) {
                ScanScreen(viewModel = viewModel, onBorrowSuccess = {
                    navController.navigate(BottomNavItem.Profile.route) {
                         popUpTo(BottomNavItem.Shelf.route)
                    }
                })
            }
            composable(BottomNavItem.Leaderboard.route) { LeaderboardScreen(viewModel) }
            composable(BottomNavItem.Profile.route) { ProfileScreen(viewModel) }
            composable(BottomNavItem.AddBook.route) { AddBookScreen(viewModel) }
        }
    }
}
