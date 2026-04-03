package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.PhoneCaller

expect class PlatformPhoneCaller : PhoneCaller {
    override fun callEmergencyNumber(number: String)
}
