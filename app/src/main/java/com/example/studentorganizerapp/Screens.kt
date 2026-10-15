package com.example.studentorganizerapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    courses: List<Course>,
    assignments: List<Assignment>,
    onAdd: () -> Unit,
    onToggle: (Assignment) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add assignment")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Text(
                    "Welcome back, Gowthamm!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text("Ready to make today count?")
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        "Active Courses",
                        "4",
                        Icons.Default.MenuBook,
                        Modifier.weight(1f)
                    )
                    StatCard(
                        "Pending Tasks",
                        assignments.count { !it.completed }.toString(),
                        Icons.Default.Assignment,
                        Modifier.weight(1f)
                    )
                }
            }

            item {
                SectionHeading(
                    "Active Courses",
                    "Your current semester"
                )
            }

            item {
                Row(
                    modifier = Modifier.horizontalScroll(
                        rememberScrollState()
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    courses.forEach { course ->
                        CourseInfo(
                            course.name,
                            course.code,
                            course.instructor,
                            course.progress
                        )
                    }
                }
            }

            item {
                SectionHeading(
                    "Urgent Deadlines",
                    "Upcoming assignments"
                )
            }

            items(
                assignments.filter { !it.completed }.take(2),
                key = { it.id }
            ) { assignment ->
                AssignmentCard(
                    assignment.title,
                    assignment.course,
                    assignment.dueDate,
                    assignment.priority,
                    assignment.completed
                ) {
                    onToggle(assignment)
                }
            }
        }
    }
}

@Composable
fun AssignmentsScreen(
    assignments: List<Assignment>,
    onAdd: () -> Unit,
    onToggle: (Assignment) -> Unit
) {
    var filter by remember { mutableStateOf("All") }

    val filtered = assignments.filter {
        when (filter) {
            "Pending" -> !it.completed
            "Completed" -> it.completed
            else -> true
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add assignment")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AppBar("My Assignments")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("All", "Pending", "Completed").forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { filter = option }
                    ) {
                        RadioButton(
                            selected = filter == option,
                            onClick = { filter = option }
                        )
                        Text(option)
                    }
                }
            }

            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No $filter assignments found.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtered, key = { it.id }) { assignment ->
                        AssignmentCard(
                            assignment.title,
                            assignment.course,
                            assignment.dueDate,
                            assignment.priority,
                            assignment.completed
                        ) {
                            onToggle(assignment)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AppBar("Student Profile")

        StudentProfile(
            "Gowtham B",
            "Student ID: PES1UG24CS172",
            "Computer Science Engineering"
        )

        Card {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    "Academic Statistics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                StatLine("GPA", "3.82")
                StatLine("Completed Credits", "84")
                StatLine("Current Semester", "Fall 2026")
            }
        }

        Button(
            onClick = {
                message = "Profile editing is a UI demonstration."
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Edit, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Edit Profile")
        }

        OutlinedButton(
            onClick = {
                message = "Academic summary export is a UI demonstration."
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.FileDownload, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Export Academic Summary")
        }

        if (message.isNotBlank()) {
            Text(message)
        }
    }
}

@Composable
fun SettingsScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    notifications: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    defaultStart: String,
    onDefaultStartChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AppBar("Settings")

        Text(
            "Preferences",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Card {
            Column(Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DarkMode, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("Dark Mode", modifier = Modifier.weight(1f))
                    Switch(
                        checked = darkMode,
                        onCheckedChange = onDarkModeChange
                    )
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Push Notifications",
                        modifier = Modifier.weight(1f)
                    )
                    Checkbox(
                        checked = notifications,
                        onCheckedChange = onNotificationsChange
                    )
                }
            }
        }

        Text(
            "Default Start Screen",
            style = MaterialTheme.typography.titleMedium
        )

        listOf("Dashboard", "Assignments").forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDefaultStartChange(option) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = defaultStart == option,
                    onClick = { onDefaultStartChange(option) }
                )
                Text(option)
            }
        }
    }
}

@Composable
fun SectionHeading(title: String, subtitle: String) {
    Column {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(subtitle, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun StatLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}