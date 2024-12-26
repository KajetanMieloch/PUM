package com.example.lista8

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

// Data Models
data class Grade(
    val id: Int,
    val subject: String,
    val grade: Float
)

// ViewModel
class GradeViewModel(private val file: File) : ViewModel() {
    private val _grades = mutableStateListOf<Grade>()
    val grades: List<Grade> get() = _grades

    val averageGrade: Float
        get() = if (_grades.isNotEmpty()) _grades.sumOf { it.grade.toDouble() }.toFloat() / _grades.size else 0f

    init {
        loadGrades()
    }

    private fun loadGrades() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!file.exists()) file.createNewFile()
                val lines = file.readLines()
                val grades = lines.mapIndexed { index, line ->
                    val parts = line.split(",")
                    Grade(index, parts[0], parts[1].toFloat().coerceIn(1f, 6f))
                }
                withContext(Dispatchers.Main) {
                    _grades.clear()
                    _grades.addAll(grades)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _grades.clear()
                }
            }
        }
    }

    fun addGrade(subject: String, grade: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val newGrade = Grade(_grades.size, subject, grade.coerceIn(1f, 6f))
            val updatedGrades = _grades.toMutableList().apply { add(newGrade) }
            file.writeText(updatedGrades.joinToString("\n") { "${it.subject},${it.grade}" })
            withContext(Dispatchers.Main) {
                _grades.add(newGrade)
            }
        }
    }

    fun updateGrade(id: Int, subject: String, grade: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedGrades = _grades.toMutableList().apply {
                this[id] = Grade(id, subject, grade.coerceIn(1f, 6f))
            }
            file.writeText(updatedGrades.joinToString("\n") { "${it.subject},${it.grade}" })
            withContext(Dispatchers.Main) {
                _grades[id] = Grade(id, subject, grade.coerceIn(1f, 6f))
            }
        }
    }

    fun deleteGrade(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedGrades = _grades.toMutableList().apply { removeAt(id) }
            file.writeText(updatedGrades.joinToString("\n") { "${it.subject},${it.grade}" })
            withContext(Dispatchers.Main) {
                _grades.removeAt(id)
            }
        }
    }
}

class GradeViewModelFactory(private val file: File) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GradeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GradeViewModel(file) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Screens
sealed class Screen(val route: String) {
    object GradeList : Screen("grade_list")
    object GradeEdit : Screen("grade_edit/{id}") {
        fun createRoute(id: Int) = "grade_edit/$id"
    }
    object GradeAdd : Screen("grade_add")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationUI(viewModel: GradeViewModel, navController: NavHostController) {
    Scaffold(
        content = { NavigationGraph(navController, viewModel) }
    )
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: GradeViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.GradeList.route
    ) {
        composable(Screen.GradeList.route) { GradeListScreen(navController, viewModel) }
        composable(
            route = Screen.GradeEdit.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            GradeAddEditScreen(navController, viewModel, id)
        }
        composable(Screen.GradeAdd.route) { GradeAddEditScreen(navController, viewModel, null) }
    }
}

@Composable
fun GradeListScreen(navController: NavHostController, viewModel: GradeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp) // Zwiększone marginesy z góry
    ) {
        Text(
            text = "Moje Oceny",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            items(viewModel.grades) { grade ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF3E0))
                        .clickable { navController.navigate(Screen.GradeEdit.createRoute(grade.id)) }
                        .padding(16.dp)
                ) {
                    Text(text = grade.subject, fontSize = 20.sp, modifier = Modifier.weight(1f))
                    Text(text = "%.1f".format(grade.grade), fontSize = 20.sp)
                }
            }
        }

        Text(
            text = "Średnia Ocen: %.2f".format(viewModel.averageGrade),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = { navController.navigate(Screen.GradeAdd.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("NOWY")
        }
    }
}

@Composable
fun GradeAddEditScreen(navController: NavHostController, viewModel: GradeViewModel, id: Int?) {
    val grade = id?.let { viewModel.grades.getOrNull(it) }
    var subject by remember { mutableStateOf(grade?.subject ?: "") }
    var gradeValue by remember { mutableStateOf(grade?.grade?.toString() ?: "") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Nazwa Przedmiotu") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = gradeValue,
                onValueChange = { gradeValue = it },
                label = { Text("Ocena") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (id == null) {
                        viewModel.addGrade(subject, gradeValue.toFloatOrNull()?.coerceIn(1f, 6f) ?: 1f)
                    } else {
                        viewModel.updateGrade(id, subject, gradeValue.toFloatOrNull()?.coerceIn(1f, 6f) ?: 1f)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(if (id == null) "DODAJ" else "ZAPISZ")
            }

            if (id != null) {
                Button(
                    onClick = {
                        viewModel.deleteGrade(id)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("USUŃ")
                }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val file = File(filesDir, "grades.txt")
        val viewModel: GradeViewModel by viewModels { GradeViewModelFactory(file) }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavigationUI(viewModel, navController)
        }
    }
}
