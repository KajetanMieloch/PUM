package com.example.lista7

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lista7.ui.theme.Purple
import com.example.lista7.ui.theme.Yellow
import kotlin.random.Random

data class Student(
    val index: String,
    val firstName: String,
    val lastName: String,
    val averageGrade: Double,
    val year: Int
)

class StudentViewModel : ViewModel() {
    private val _studentList = mutableStateListOf<Student>()
    val studentList: List<Student> get() = _studentList

    fun initializeStudents() {
        val firstNames = listOf("Anna", "Piotr", "Kasia", "Tomek", "Magda", "Jan")
        val lastNames = listOf("Kowalski", "Nowak", "Wiśniewski", "Zieliński", "Kamiński")
        _studentList.clear()
        repeat(20) {
            _studentList.add(
                Student(
                    index = (100000..999999).random().toString(),
                    firstName = firstNames.random(),
                    lastName = lastNames.random(),
                    averageGrade = Random.nextDouble(2.0, 5.0),
                    year = Random.nextInt(1, 5)
                )
            )
        }
    }
}

sealed class Screen(val route: String) {
    object StudentListScreen : Screen("student_list")
    object StudentDetailScreen : Screen("student_detail/{index}") {
        fun createRoute(index: String) = "student_detail/$index"
    }
}

@Composable
fun NavigationUI(viewModel: StudentViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        content = { NavigationGraph(navController, viewModel, modifier) }
    )
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: StudentViewModel, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.StudentListScreen.route
    ) {
        composable(Screen.StudentListScreen.route) { StudentListScreen(navController, viewModel) }
        composable(
            route = Screen.StudentDetailScreen.route,
            arguments = listOf(navArgument("index") { type = NavType.StringType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index") ?: ""
            val student = viewModel.studentList.find { it.index == index }
            if (student != null) {
                StudentDetailScreen(navController, student)
            } else {
                Text("Student not found")
            }
        }
    }
}

@Composable
fun StudentListScreen(navController: NavHostController, viewModel: StudentViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        Text(
            text = "All students",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize().padding(top = 10.dp)
        ) {
            items(viewModel.studentList) { student ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Purple)
                        .clickable { navController.navigate(Screen.StudentDetailScreen.createRoute(student.index)) }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = student.index,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${student.firstName} ${student.lastName}",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentDetailScreen(navController: NavHostController, student: Student) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(20.dp).background(Yellow)
    ) {
        Text(
            "Student Details",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(10.dp)
        )
        Text("Index: ${student.index}", fontSize = 22.sp, color = Color.Black, modifier = Modifier.padding(10.dp))
        Text("Name: ${student.firstName} ${student.lastName}", fontSize = 22.sp, color = Color.Black, modifier = Modifier.padding(10.dp))
        Text("Average Grade: %.2f".format(student.averageGrade), fontSize = 22.sp, color = Color.Black, modifier = Modifier.padding(10.dp))
        Text("Year: ${student.year}", fontSize = 22.sp, color = Color.Black, modifier = Modifier.padding(10.dp))
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.padding(20.dp)) {
            Text("Back")
        }
    }
}

class MainActivity : ComponentActivity() {
    private val studentViewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        studentViewModel.initializeStudents()
        setContent {
            NavigationUI(studentViewModel)
        }
    }
}
