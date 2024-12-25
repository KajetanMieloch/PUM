package com.kajetanmieloch.pumlista1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizApp()
        }
    }
}

@Composable
fun QuizApp() {
    val questions = getMobbynQuestions()
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var showScore by remember { mutableStateOf(false) }

    if (showScore) {
        FinalScoreScreen(score = score, totalQuestions = questions.size)
    } else {
        QuizScreen(
            question = questions[currentQuestionIndex],
            questionNumber = currentQuestionIndex + 1,
            totalQuestions = questions.size,
            onAnswerSelected = { selectedAnswer ->
                if (selectedAnswer == questions[currentQuestionIndex].correctAnswer) {
                    score++
                }
                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                } else {
                    showScore = true
                }
            }
        )
    }
}

@Composable
fun QuizScreen(
    question: Question,
    questionNumber: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int) -> Unit
) {
    var selectedOption by remember { mutableStateOf(-1) } // Track the selected option

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Question $questionNumber / $totalQuestions",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = question.question,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        question.answers.forEachIndexed { index, answer ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == index,
                    onClick = {
                        selectedOption = index
                    },
                    colors = RadioButtonDefaults.colors()
                )
                Text(
                    text = answer,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Button(
            onClick = {
                if (selectedOption != -1) {
                    onAnswerSelected(selectedOption)
                    selectedOption = -1 // Reset selection after moving to the next question
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Next")
        }
    }
}

@Composable
fun FinalScoreScreen(score: Int, totalQuestions: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Completed!",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Your Score: $score / $totalQuestions",
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

fun getMobbynQuestions(): List<Question> {
    return listOf(
        Question(
            "Kto założył grupę Mobbyn?",
            listOf("Belmondo", "Oyche Doniz", "White 2115", "Young Igi"),
            0
        ),
        Question(
            "Jaki był tytuł pierwszego albumu Mobbyn?",
            listOf("Sos Mobbyn", "Guzik z pętelką", "Opowieści z Doliny Smoków", "Mobbyn SOS"),
            0
        ),
        Question(
            "Który z członków Mobbyn zasłynął kontrowersyjnymi tekstami?",
            listOf("Belmondo", "Oyche Doniz", "Żaden", "Obaj"),
            3
        ),
        Question(
            "Który z członków Mobbyn był odpowiedzialny za produkcję muzyczną?",
            listOf("Belmondo", "Oyche Doniz", "Young Leosia", "DJ Johny"),
            1
        ),
        Question(
            "W którym roku grupa Mobbyn oficjalnie zakończyła działalność?",
            listOf("2016", "2018", "2020", "2019"),
            1
        ),
        Question(
            "Jakie kontrowersje doprowadziły do zakończenia działalności Mobbyn?",
            listOf("Konflikt między członkami", "Problemy prawne", "Rozpad zespołu", "Problemy finansowe"),
            0
        ),
        Question(
            "Jak nazywał się wspólny projekt Belmondo i Oyche Doniza?",
            listOf("Mobbyn", "Belmobb", "Mobb2", "Mobbyn 2.0"),
            0
        ),
        Question(
            "Jaki styl muzyczny reprezentuje Mobbyn?",
            listOf("Trap", "Pop", "Jazz", "Rock"),
            0
        ),
        Question(
            "Która z tych piosenek jest autorstwa Mobbyn?",
            listOf("Siekiera", "Pięć gwiazdek", "Ja chcę mieć G-Klase", "Baryła"),
            2
        ),
        Question(
            "Który z członków Mobbyn wrócił na scenę muzyczną po rozpadzie grupy?",
            listOf("Belmondo", "Oyche Doniz", "Young Multi", "Żaden"),
            0
        )
    )
}