package com.kajetanmieloch.pumlista1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private var currentQuestionIndex = 1
    private val totalQuestions = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val questionCounterTextView = findViewById<TextView>(R.id.textView4)
        questionCounterTextView.text = currentQuestionIndex.toString() + "/10"

        val nextButton = findViewById<Button>(R.id.button)

        // Obsługa kliknięcia przycisku
        nextButton.setOnClickListener {
            nextQuestion()
        }
    }

    fun nextQuestion(){
        val questionCounterTextView = findViewById<TextView>(R.id.textView4)
        if(currentQuestionIndex < totalQuestions) {
            currentQuestionIndex += 1;
            questionCounterTextView.text = currentQuestionIndex.toString() + "/10"
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
}