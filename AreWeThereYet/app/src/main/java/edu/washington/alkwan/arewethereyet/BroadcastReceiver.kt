package edu.washington.alkwan.arewethereyet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message")
        val number = intent.getStringExtra("number")
        Log.v("**BroadcastReceiver**", "Message: $message. Number: $number.")
        val text = "Texting $number: $message"
        val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        toast.show()
    }
}
