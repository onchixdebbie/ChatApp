package com.deborah.chatbotapp.utils

import androidx.compose.ui.text.toLowerCase
import com.deborah.chatbotapp.utils.Constants.OPEN_GOOGLE
import com.deborah.chatbotapp.utils.Constants.OPEN_SEARCH

object BotResponse {

    fun basicResponses(_message: String): String{

        val random = (0..2).random()
        val message = _message.toLowerCase()

        return when {
            //Hello
            message.contains("Hello") -> {
                when (random) {
                    0 -> "Hello there!"
                    1 -> "Sup"
                    2 -> "Boojoonro"
                    else -> "error"
                }
            }


            //How are you
            message.contains("How are you") -> {
                when (random) {
                    0 -> "I am doing fine, thanks for asking!"
                    1 -> "I am hungry"
                    2 -> "Pretty good! How are you?"
                    else -> "error"
                }
            }

            message.contains("flip") && message.contains("coin") ->{
                var r = (0..1).random()
                val result = if(r == 0) "heads" else "tails"
                "I flipped a coin and it landed on $result"
            }

            //solve the Math
            message.contains("solve") ->{
                val equation: String? = message.substringAfter("solve")

                return try {

                    val answer = SolveMath.solveMath(equation?: "0")
                    answer.toString()

                }catch (e: Exception){
                    "Sorry I cannot solve that"
                }
            }
            //Get the current time
            message.contains("time")&& message.contains("?") ->{
                Time.timeStamp()
            }

            //Opens Google
            message.contains("open")&& message.contains("google") ->{
                OPEN_GOOGLE
            }

            message.contains("search") ->{
                OPEN_SEARCH
            }

            else -> {
                when (random) {
                    0 -> "I don't understand..."
                    1 -> "Idk"
                    2 -> "Try asking me something different"
                    else -> "error"
                }
            }
        }
    }
}