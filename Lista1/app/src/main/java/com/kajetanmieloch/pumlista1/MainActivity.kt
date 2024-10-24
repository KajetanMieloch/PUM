package com.kajetanmieloch.pumlista1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ProgressBar
import android.view.View

class MainActivity : AppCompatActivity() {
    private var currentQuestionIndex = 0
    private val totalQuestions = 10
    private var points = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val questionCounterTextView = findViewById<TextView>(R.id.textView4)
        val questionTextView = findViewById<TextView>(R.id.textView9)
        val aswear1 = findViewById<RadioButton>(R.id.radioButton1)
        val aswear2 = findViewById<RadioButton>(R.id.radioButton2)
        val aswear3 = findViewById<RadioButton>(R.id.radioButton3)
        val aswear4 = findViewById<RadioButton>(R.id.radioButton4)

        questionCounterTextView.text = currentQuestionIndex.toString() + "/10"
        questionTextView.text = getMobbynQuestions()[0].question
        aswear1.text = getMobbynQuestions()[0].aswears[0]
        aswear2.text = getMobbynQuestions()[0].aswears[1]
        aswear3.text = getMobbynQuestions()[0].aswears[2]
        aswear4.text = getMobbynQuestions()[0].aswears[3]

        val nextButton = findViewById<Button>(R.id.button)

        // Obsługa kliknięcia przycisku
        nextButton.setOnClickListener {
            nextQuestion()
        }
    }

    fun nextQuestion() {
        val questionCounterTextView = findViewById<TextView>(R.id.textView4)
        val questionTextView = findViewById<TextView>(R.id.textView9)
        val aswear1 = findViewById<RadioButton>(R.id.radioButton1)
        val aswear2 = findViewById<RadioButton>(R.id.radioButton2)
        val aswear3 = findViewById<RadioButton>(R.id.radioButton3)
        val aswear4 = findViewById<RadioButton>(R.id.radioButton4)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val selectedAnswerIndex = when {
            aswear1.isChecked -> 0
            aswear2.isChecked -> 1
            aswear3.isChecked -> 2
            aswear4.isChecked -> 3
            else -> -1 // No selection
        }

        if (selectedAnswerIndex == getMobbynQuestions()[currentQuestionIndex].correctAnswer) {
            points += 1
        }

        if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex += 1

            radioGroup.clearCheck()

            questionCounterTextView.text = (currentQuestionIndex + 1).toString() + "/10"
            questionTextView.text = getMobbynQuestions()[currentQuestionIndex].question
            aswear1.text = getMobbynQuestions()[currentQuestionIndex].aswears[0]
            aswear2.text = getMobbynQuestions()[currentQuestionIndex].aswears[1]
            aswear3.text = getMobbynQuestions()[currentQuestionIndex].aswears[2]
            aswear4.text = getMobbynQuestions()[currentQuestionIndex].aswears[3]
        } else {
            showFinalScore()
        }
    }

    fun showFinalScore() {
        findViewById<TextView>(R.id.textView4).visibility = View.GONE
        findViewById<TextView>(R.id.textView3).visibility = View.GONE
        findViewById<TextView>(R.id.textView9).visibility = View.GONE
        findViewById<RadioGroup>(R.id.radioGroup).visibility = View.GONE
        findViewById<Button>(R.id.button).visibility = View.GONE
        findViewById<ProgressBar>(R.id.progressBar3).visibility = View.GONE

        val finalScoreTextView = findViewById<TextView>(R.id.finalScoreTextView)
        finalScoreTextView.text = "Twój wynik to: $points z $totalQuestions!"
        finalScoreTextView.visibility = View.VISIBLE
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