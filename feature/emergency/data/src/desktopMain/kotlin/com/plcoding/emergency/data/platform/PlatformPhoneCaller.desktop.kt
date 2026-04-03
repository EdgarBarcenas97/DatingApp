package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.PhoneCaller

actual class PlatformPhoneCaller : PhoneCaller {
    override fun callEmergencyNumber(number: String) {
        // Desktop doesn't support phone calls
    }
}
