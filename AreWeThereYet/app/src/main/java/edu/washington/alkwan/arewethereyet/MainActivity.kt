package edu.washington.alkwan.arewethereyet

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText

class MainActivity : AppCompatActivity() {

    companion object {
        val REQUEST_SMS_SEND_PERMISSION = 1234
    }

    private lateinit var alarmIntent: Intent
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.buttonStart)
        val editMessage = findViewById<EditText>(R.id.editMessage)
        val editPhoneNumber = findViewById<EditText>(R.id.editNumber)
        val editMinutes = findViewById<EditText>(R.id.editMinutes)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, BroadcastReceiver::class.java)
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
                    alarmIntent.putExtra("message", editMessage.text.toString())
                    val areaCode = editPhoneNumber.text.toString().substring(1,4)
                    val num1 = editPhoneNumber.text.toString().substring(6,9)
                    val num2 = editPhoneNumber.text.toString().substring(10)
                    val numTotal = areaCode + num1 + num2
                    alarmIntent.putExtra("number", numTotal)
                    pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
                    startAlarm(editMinutes.text.toString(), alarmManager)
                    startButton.text = "Stop"
                    Log.v("**Main**", "Pressed start!")
                }
            } else {
                startButton.text = "Start"
                alarmManager.cancel(pendingIntent)
                Log.v("**Main**", "Texts ended!")
            }
        }
    }

    private fun startAlarm(minutes: String, alarmManager: AlarmManager) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {

            // Need to request SEND_SMS permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.SEND_SMS),
                REQUEST_SMS_SEND_PERMISSION)
            Log.v("**Main**", "Send SMS permissions")
        } else {
            // Has Permissions, Send away!
            val time = minutes.toLong() * 60000
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                time,
                pendingIntent
            )
            Log.v("**Main**", "Texts started!")
        }
    }
}
