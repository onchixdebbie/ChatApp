package com.deborah.chatbotapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deborah.chatbotapp.R
import com.deborah.chatbotapp.data.Message
import com.deborah.chatbotapp.utils.BotResponse
import com.deborah.chatbotapp.utils.Constants.OPEN_GOOGLE
import com.deborah.chatbotapp.utils.Constants.OPEN_SEARCH
import com.deborah.chatbotapp.utils.Constants.RECEIVE_ID
import com.deborah.chatbotapp.utils.Constants.SEND_ID
import com.deborah.chatbotapp.utils.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var adapter: MessagingAdapter
    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private val botList = listOf("Fransisca", "Peter", "Pemela", "Kamau", "Njuguna")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // Initialize UI components
            setContentView(R.layout.activity_main)
            rvMessages = findViewById(R.id.rv_messages)
            etMessage = findViewById(R.id.et_message)
            btnSend = findViewById(R.id.btn_send)

            recyclerView()

            clickEvents()

            val random = (0..4).random()

            customMessage("Hello! Today you are speaking with ${botList[random]}, how may I help you?")
        }
    }

    private fun clickEvents() {
        btnSend.setOnClickListener {
            sendMessage()
        }

        etMessage.setOnClickListener {
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main) {
                    rvMessages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        rvMessages.adapter = adapter
        rvMessages.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun sendMessage() {
        val message = etMessage.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            etMessage.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rvMessages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            delay(1000)

            withContext(Dispatchers.Main) {
                val response = BotResponse.basicResponses(message)

                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
                rvMessages.scrollToPosition(adapter.itemCount - 1)

                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }

                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String = message.substringAfter("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun customMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))
                rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}
