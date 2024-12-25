package com.example.lista6

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlin.random.Random

data class Exercise(
    val content: String,
    val points: Int
)

data class Subject(
    val name: String
)

data class GradeSummary(
    val subject: Subject,
    val average: Double,
    val listCount: Int
)

data class ExerciseList(
    val exercises: MutableList<Exercise>,
    val subject: Subject,
    val grade: Float
)


val subjects = listOf(
    Subject("Matematyka"),
    Subject("PUM"),
    Subject("Fizyka"),
    Subject("Elektronika"),
    Subject("Algorytmy")
)

val exerciseLists = mutableListOf<ExerciseList>()
val gradeSummaries = mutableListOf<GradeSummary>()

fun generateExerciseLists() {
    val loremOptions = listOf(
        "Lorem ipsum dolor sit amet.",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    )

    repeat(20) {
        val exercises = MutableList(Random.nextInt(1, 10)) {
            Exercise(
                content = loremOptions.random(),
                points = Random.nextInt(1, 11)
            )
        }
        exerciseLists.add(
            ExerciseList(
                exercises = exercises,
                subject = subjects.random(),
                grade = Random.nextInt(6, 11).toFloat() / 2
            )
        )
    }
}

fun calculateAverageGrades() {
    subjects.forEach { subject ->
        val relatedLists = exerciseLists.filter { it.subject == subject }
        val totalGrade = relatedLists.sumOf { it.grade.toDouble() }
        val listCount = relatedLists.size
        if (listCount > 0) {
            gradeSummaries.add(
                GradeSummary(
                    subject = subject,
                    average = totalGrade / listCount,
                    listCount = listCount
                )
            )
        }
    }
}

// Screens
sealed class Screen(val route: String) {
    object ExerciseListScreen : Screen("exercise_list")
    object GradesScreen : Screen("grades")
    object ExerciseDetailScreen : Screen("exercise_detail/{index}") {
        fun createRoute(index: Int) = "exercise_detail/$index"
    }
}

sealed class BottomNavigationItem(val route: String, val title: String, val icon: ImageVector) {
    object ExerciseList : BottomNavigationItem(Screen.ExerciseListScreen.route, "Lists", Icons.Default.Home)
    object Grades : BottomNavigationItem(Screen.GradesScreen.route, "Grades", Icons.Default.Info)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationUI(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = { NavigationGraph(navController, modifier) }
    )
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.ExerciseListScreen.route
    ) {
        composable(Screen.ExerciseListScreen.route) { ExerciseListScreen(navController) }
        composable(Screen.GradesScreen.route) { GradesScreen() }
        composable(
            route = Screen.ExerciseDetailScreen.route,
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: -1
            val exerciseList = exerciseLists.getOrNull(index)
            if (exerciseList != null) {
                ExerciseDetailScreen(exerciseList)
            } else {
                Text("Invalid exercise list index")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(BottomNavigationItem.ExerciseList, BottomNavigationItem.Grades)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                icon = { Icon(screen.icon, contentDescription = null) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}

@Composable
fun ExerciseListScreen(navController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "Exercise Lists",
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp)
        )
        LazyColumn(modifier = Modifier.padding(bottom = 100.dp)) {
            items(exerciseLists.size) { index ->
                val exerciseList = exerciseLists[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFD1C4E9))
                        .clickable { navController.navigate(Screen.ExerciseDetailScreen.createRoute(index)) }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(exerciseList.subject.name, fontSize = 20.sp)
                        Text("Grade: ${exerciseList.grade}", fontSize = 20.sp)
                    }
                    Text("Tasks: ${exerciseList.exercises.size}", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun GradesScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "Grade Summaries",
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp)
        )
        LazyColumn(modifier = Modifier.padding(bottom = 100.dp)) {
            items(gradeSummaries.size) { index ->
                val summary = gradeSummaries[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFDD835))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(summary.subject.name, fontSize = 20.sp)
                        Text("Average: %.2f".format(summary.average), fontSize = 20.sp)
                    }
                    Text("Lists: ${summary.listCount}", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun ExerciseDetailScreen(exerciseList: ExerciseList) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        Text(
            "${exerciseList.subject.name} - Details",
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp)
        )
        LazyColumn(modifier = Modifier.padding(bottom = 100.dp)) {
            items(exerciseList.exercises.size) { index ->
                val exercise = exerciseList.exercises[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFD1C4E9))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Task ${index + 1}", fontSize = 20.sp)
                        Text("Points: ${exercise.points}", fontSize = 20.sp)
                    }
                    Text(exercise.content, fontSize = 16.sp)
                }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            generateExerciseLists()
            calculateAverageGrades()
            NavigationUI(Modifier)
        }
    }
}
