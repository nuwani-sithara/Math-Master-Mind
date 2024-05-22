package com.example.mathmastermind

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Random
import android.app.AlertDialog
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var answerButtons: Array<Button>
    private lateinit var timerTextView: TextView

    private var correctAnswer: Int = 0
    private var score: Int = 0
    private var remainingTime: Long = 60000 // 60 seconds

    private lateinit var countDownTimer: CountDownTimer

    // Properties to hold num1 and num2 values
    private var num1: Int = 0
    private var num2: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.questionTextView)
        timerTextView = findViewById(R.id.timerTextView)

        answerButtons = arrayOf(
            findViewById(R.id.answerButton1),
            findViewById(R.id.answerButton2),
            findViewById(R.id.answerButton3),
            findViewById(R.id.answerButton4)
        )

        generateQuestion()
        startTimer()

        answerButtons.forEach { button ->
            button.setOnClickListener { checkAnswer(button) }
        }
    }

    private fun generateQuestion() {
        val random = Random()
        num1 = random.nextInt(100)
        num2 = random.nextInt(100)
        correctAnswer = num1 + num2

        questionTextView.text = "$num1 + $num2 = ?"

        val answers = mutableListOf(correctAnswer)
        while (answers.size < 4) {
            val wrongAnswer = random.nextInt(200)
            if (wrongAnswer != correctAnswer) {
                answers.add(wrongAnswer)
            }
        }
        answers.shuffle()

        answerButtons.forEachIndexed { index, button ->
            button.text = answers[index].toString()
        }
    }

    private fun checkAnswer(button: Button) {
        val selectedAnswer = button.text.toString().toInt()
        if (selectedAnswer == correctAnswer) {
            score++
            generateQuestion()
        } else {
            gameOver()
        }

        if (score >= 10) { // Win condition, change 10 to the desired score to win
            gameWon()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                val seconds = remainingTime / 1000
                timerTextView.text = "Time: $seconds seconds"
            }

            override fun onFinish() {
                gameOver()
            }
        }.start()
    }

    private fun gameWon() {
        // Implement win logic, e.g., show a congratulatory message
        timerTextView.text = "You Win!"
        countDownTimer.cancel()
    }

    private fun gameOver() {
        countDownTimer.cancel() // Stop the timer

        // Show the correct answers
        showAnswers()

        // Show a dialog box with game over message and options
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Game Over")
        dialogBuilder.setMessage("Your final score: $score\n\nDo you want to play again?")
        dialogBuilder.setPositiveButton("Restart") { dialog, which ->
            // Reset the game state
            generateQuestion()
            score = 0
            remainingTime = 60000 // Reset the timer to 60 seconds
            startTimer()

            // Update the UI to make buttons available again
            answerButtons.forEach { button ->
                button.isEnabled = true
            }
        }
        dialogBuilder.setNegativeButton("Main Menu") { dialog, which ->
            // Navigate back to the main menu activity
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish() // Close the current activity
        }
        dialogBuilder.setCancelable(false) // Prevent the dialog from being dismissed by pressing outside
        dialogBuilder.show()
    }

    private fun showAnswers() {
        val correctAnswersText = "Correct Answers:\n$num1 + $num2 = $correctAnswer"

        // Show a dialog box with correct answers
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Correct answer of the previous one")
        dialogBuilder.setMessage("Your final score: $score\n\n$correctAnswersText")
        dialogBuilder.setPositiveButton("OK") { dialog, which ->
            // Handle the OK button click, you can add any specific action here if needed
        }
        dialogBuilder.setCancelable(false) // Prevent the dialog from being dismissed by pressing outside

        // Set the correct answers on the buttons
        answerButtons.forEachIndexed { index, button ->
            button.text = if (index == 0) correctAnswer.toString() else ""
            button.isEnabled = false // Disable the buttons so the user cannot interact with them
        }

        dialogBuilder.show()
    }
}
