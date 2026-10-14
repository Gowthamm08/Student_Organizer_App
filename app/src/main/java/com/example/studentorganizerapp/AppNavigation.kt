package com.example.studentorganizerapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Assignments : Screen("assignments")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object AddAssignment : Screen("add_assignment")
}

data class Assignment(
    val id: Int,
    val title: String,
    val course: String,
    val dueDate: String,
    val priority: String,
    val description: String = "",
    val completed: Boolean = false
)

data class Course(
    val name: String,
    val code: String,
    val instructor: String,
    val progress: Float
)

@Composable
fun StudentOrganizerApp() {
    val navController = rememberNavController()

    var darkMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    var defaultStart by remember { mutableStateOf("Dashboard") }
    var nextId by remember { mutableIntStateOf(4) }

    val assignments = remember {
        mutableStateListOf(
            Assignment(
                1, "Database Design Report",
                "DBMS", "Oct 16, 2026", "High"
            ),
            Assignment(
                2, "Sorting Algorithms",
                "DAA", "Oct 18, 2026", "Medium"
            ),
            Assignment(
                3, "UI Prototype Review",
                "Android Development",
                "Oct 21, 2026", "Low",
                completed = true
            )
        )
    }

    val courses = listOf(
        Course("Database Systems", "CS301", "Dr. Rao", 0.72f),
        Course("Algorithms", "CS302", "Prof. Mehta", 0.58f),
        Course("Android Development", "CS305", "Dr. Kumar", 0.84f),
        Course("Computer Networks", "CS303", "Prof. Shah", 0.45f)
    )

    androidx.compose.material3.MaterialTheme(
        colorScheme = if (darkMode) {
            androidx.compose.material3.darkColorScheme()
        } else {
            androidx.compose.material3.lightColorScheme()
        }
    ) {
        val backStack by navController.currentBackStackEntryAsState()
        val currentRoute = backStack?.destination?.route

        Scaffold(
            bottomBar = {
                if (currentRoute != Screen.AddAssignment.route) {
                    BottomBar(navController, currentRoute)
                }
            }
        ) { padding ->

            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        courses = courses,
                        assignments = assignments,
                        onAdd = {
                            navController.navigate(
                                Screen.AddAssignment.route
                            )
                        },
                        onToggle = {
                            toggleAssignment(assignments, it)
                        }
                    )
                }

                composable(Screen.Assignments.route) {
                    AssignmentsScreen(
                        assignments = assignments,
                        onAdd = {
                            navController.navigate(
                                Screen.AddAssignment.route
                            )
                        },
                        onToggle = {
                            toggleAssignment(assignments, it)
                        }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreen()
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        darkMode = darkMode,
                        onDarkModeChange = { darkMode = it },
                        notifications = notifications,
                        onNotificationsChange = {
                            notifications = it
                        },
                        defaultStart = defaultStart,
                        onDefaultStartChange = {
                            defaultStart = it
                        }
                    )
                }

                composable(Screen.AddAssignment.route) {
                    AddAssignmentScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        onSave = { title, course, date, priority, description ->
                            assignments.add(
                                0,
                                Assignment(
                                    id = nextId++,
                                    title = title.trim(),
                                    course = course.trim(),
                                    dueDate = date.trim(),
                                    priority = priority,
                                    description = description
                                )
                            )
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

fun toggleAssignment(
    assignments: MutableList<Assignment>,
    assignment: Assignment
) {
    val index = assignments.indexOfFirst {
        it.id == assignment.id
    }

    if (index >= 0) {
        assignments[index] = assignments[index].copy(
            completed = !assignments[index].completed
        )
    }
}