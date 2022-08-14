package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // BACKING PROPERTY
    // making variable dynamic such that it can't be updated outside the class but its value can be used
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    // lateinit type variable should be initialized before using it
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    // initializer block which first get executed after start of activity
    init {
        getNextWord()
    }

    // updates currentWord and currentScrambleWord wih the next word
    private fun getNextWord() {
        currentWord = allWordsList.random()         // fetch random word from allWordList
        val tempWord = currentWord.toCharArray()    // to shuffle the letters of currentWord it needs to convert in charArray
        tempWord.shuffle()                          // shuffles the letters of the word

        while(String(tempWord).equals(currentWord,false)) {
            tempWord.shuffle()
        }

        if(wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    // Increases the game score if the player’s word is correct.
    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    // Checks if playerWord is correct and accordingly increases score
    fun isUserWordCorrect(playerWord : String) : Boolean {
        if(playerWord.equals(currentWord,true)) {
            increaseScore()
            return true
        }
        return false
    }

    // Re-initializes the data to restart the game
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    // check if accessing nextWord is possible or not
    fun nextWord(): Boolean {
        return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

}