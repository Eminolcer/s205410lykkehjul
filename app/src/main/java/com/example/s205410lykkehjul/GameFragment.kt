package com.example.s205410lykkehjul

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import java.lang.Exception

class GameFragment : Fragment() {
    private var showWord = ""
    private var secretWord = ""
    private var lives = 5
    private var point = 0
    private var playerGuess = ""
    private var winGame: Boolean = false
    private var loseGame: Boolean = false
    private var guessCorrect: Boolean = false
    private var spinTextResult = ""
    private var spinOutcome = 0
    private var extraLives = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val start = view.findViewById<Button>(R.id.startButton)
        val greeting = view.findViewById<TextView>(R.id.welcomeText)
        val letter = view.findViewById<EditText>(R.id.letterText)
        val guessButton = view.findViewById<Button>(R.id.guessButton)
        val wordText = view.findViewById<TextView>(R.id.wordText)
        val cardView2 = view.findViewById<CardView>(R.id.cardView2)
        val cardView1 = view.findViewById<CardView>(R.id.cardView)
        val spinText = view.findViewById<TextView>(R.id.Spintext)
        val spinButton = view.findViewById<Button>(R.id.spinButton)
        val categoryText = view.findViewById<TextView>(R.id.categoryText)
        val livesText = view.findViewById<TextView>(R.id.livesText)
        val pointText = view.findViewById<TextView>(R.id.pointText)
        val categoryView = view.findViewById<ImageView>(R.id.categoryView)
        val livesView = view.findViewById<ImageView>(R.id.livesView)
        val pointView = view.findViewById<ImageView>(R.id.pointView)



        start.setOnClickListener {
            start.setVisibility(View.GONE)
            greeting.setVisibility(View.GONE)
            wordText.setVisibility(View.VISIBLE)
            cardView2.setVisibility(View.VISIBLE)
            cardView1.setVisibility(View.VISIBLE)
            spinText.setVisibility(View.VISIBLE)
            spinButton.setVisibility(View.VISIBLE)
            categoryText.setVisibility(View.VISIBLE)
            livesText.setVisibility(View.VISIBLE)
            pointText.setVisibility(View.VISIBLE)
            categoryView.setVisibility(View.VISIBLE)
            livesView.setVisibility(View.VISIBLE)
            pointView.setVisibility(View.VISIBLE)
            gameBegin()
            livesText.text = lives.toString()
            pointText.text = point.toString()
            Toast.makeText(activity, secretWord, Toast.LENGTH_SHORT).show()
            wordText.text = showWord
        }

        guessButton.setOnClickListener {
            guessButton.setVisibility(View.INVISIBLE)
            letter.setVisibility(View.INVISIBLE)
            spinButton.setVisibility(View.VISIBLE)
            playerGuess = letter.text.toString()
            guess()
            livesText.text = lives.toString()
            pointText.text = point.toString()
            wordText.text = showWord
            if (winGame) {
                Navigation.findNavController(view).navigate(R.id.goToWinGame)
            }
            if (loseGame) {
                Navigation.findNavController(view).navigate(R.id.goToLost)
            }
        }

        spinButton.setOnClickListener {
            guessButton.setVisibility(View.VISIBLE)
            letter.setVisibility(View.VISIBLE)
            spinButton.setVisibility(View.INVISIBLE)
            spin()
            livesText.text = lives.toString()
            pointText.text = point.toString()
            spinText.text = spinTextResult
            if (loseGame) {
                Navigation.findNavController(view).navigate(R.id.goToLost)
            }
        }


    }

    private fun gameBegin() {
        val sportArray: Array<String> = resources.getStringArray(R.array.Sport)
        secretWord = sportArray.random()

        repeat(secretWord.length) {
            showWord = showWord + "_"

        }

    }

    private fun guess() {
        if (playerGuess in secretWord && playerGuess.length == 1) {
            guessCorrect = true
            point += spinOutcome
            spinOutcome = 0
            lives += extraLives
            extraLives = 0

            val indexOfLetter = secretWord.indexesOf(playerGuess, true)
            if (indexOfLetter.size == 1) {
                showWord =
                    showWord.replaceRange(indexOfLetter[0], indexOfLetter[0] + 1, playerGuess)
            } else if (indexOfLetter.size == 2) {
                showWord =
                    showWord.replaceRange(indexOfLetter[0], indexOfLetter[0] + 1, playerGuess)
                showWord =
                    showWord.replaceRange(indexOfLetter[1], indexOfLetter[1] + 1, playerGuess)
            }
        } else if (playerGuess !in secretWord && playerGuess.length == 1) {
            lives = lives - 1
            spinOutcome = 0
            extraLives = 0
            Toast.makeText(activity, "Du gættede forkert, og mister et liv", Toast.LENGTH_SHORT)
                .show()
        } else if (playerGuess.length != 1) {
            Toast.makeText(activity, "Skriv kun et bogstav", Toast.LENGTH_SHORT).show()
        }
        win()
        lost()

        return

    }


    /**
     * funktionen finder indexet af et bogstav i et string.
     * Denne funktion er fundet på stack overflow
     */
    fun String?.indexesOf(substr: String, ignoreCase: Boolean = false): List<Int> {
        return this?.let {
            val indexes = mutableListOf<Int>()
            var startIndex = 0
            while (startIndex in 0 until length) {
                val index = this.indexOf(substr, startIndex, ignoreCase)
                startIndex = if (index != -1) {
                    indexes.add(index)
                    index + substr.length
                } else {
                    index
                }
            }
            return indexes
        } ?: emptyList()
    }

    fun win() {
        if (showWord == secretWord) {
            winGame = true
        }
    }

    fun lost() {
        if (lives == 0) {
            loseGame = true
        }
    }

    fun spin() {
        val spinArray: Array<String> = resources.getStringArray(R.array.points)
        spinTextResult = spinArray.random()

        val splitString = spinTextResult.split(" ")

        if (splitString[4][1] == '0') {
            spinOutcome = splitString[4].toInt()
        }else{
            when (splitString[4]) {
            "modtage" -> extraLives = 1
            "liv" -> lives -= 1
            "bankerot" -> point = 0
        }}


    }


}
