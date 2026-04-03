package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.PhoneCaller
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class PlatformPhoneCaller : PhoneCaller {
    override fun callEmergencyNumber(number: String) {
        val url = NSURL(string = "tel:$number")
        UIApplication.sharedApplication.openURL(url)
    }
}
