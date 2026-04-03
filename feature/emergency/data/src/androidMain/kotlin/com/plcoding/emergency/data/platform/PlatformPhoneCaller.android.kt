package com.plcoding.emergency.data.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.plcoding.emergency.domain.PhoneCaller

actual class PlatformPhoneCaller(
    private val context: Context
) : PhoneCaller {
    override fun callEmergencyNumber(number: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$number")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
