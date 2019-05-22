package edu.washington.alkwan.arewethereyet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast

class BroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message")
        val number = intent.getStringExtra("number")
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(
            number,
            null,
            message,
            null,
            null
        )
        Log.v("**BroadcastReceiver**", "Message: $message. Number: $number.")
    }
}
