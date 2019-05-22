package edu.washington.alkwan.arewethereyet

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.buttonStart)
        val editMessage = findViewById<EditText>(R.id.editMessage)
        val editPhoneNumber = findViewById<EditText>(R.id.editNumber)
        val editMinutes = findViewById<EditText>(R.id.editMinutes)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, BroadcastReceiver::class.java)
        val regex = """(\([0-9]{3}\)\ [0-9]{3}\-[0-9]{4})""".toRegex()

        startButton.setOnClickListener {
            if (startButton.text == "Start") {
                val duration = LENGTH_SHORT

                if (editMessage.text.isEmpty()) {
                    val text = "Please include a message to send."
                    val toast = Toast.makeText(this, text, duration)
                    toast.show()
                } else if (!regex.matches(editPhoneNumber.text.toString())) {
                    val text = "Please make sure the phone number is in the (123) 456-7891 format."
                    val toast = Toast.makeText(this, text, Toast.LENGTH_LONG)
                    toast.show()
                } else if (editMinutes.text.isEmpty()) {
                    val text = "Please input a number."
                    val toast = Toast.makeText(this, text, duration)
                    toast.show()
                } else if (editMinutes.text.toString().toInt() <= 0) {
                    val text = "Please input 1 minute or more."
                    val toast = Toast.makeText(this, text, duration)
                    toast.show()
                } else {
                    startButton.text = "Stop"
                    val messageText = editMessage.text.toString()
                    val phoneText = editPhoneNumber.text.toString()
                    intent.putExtra("message", messageText)
                    intent.putExtra("number", phoneText)

                    val alarmIntent = PendingIntent.getBroadcast(this, 0, intent,0)
                    val time = editMinutes.text.toString().toLong() * 60000

                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        time,
                        alarmIntent
                    )

                    Log.v("**Main**",
                        "Alarm started! Message is '$messageText' and number is $phoneText.")
                }
            } else {
                startButton.text = "Start"
                val alarmIntent = PendingIntent.getBroadcast(this, 0, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT)
                alarmManager.cancel(alarmIntent)
                Log.v("**Main**", "Alarm ended!")
            }
        }
    }
}
