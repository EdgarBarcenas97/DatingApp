package com.plcoding.emergency.domain

interface PhoneCaller {
    fun callEmergencyNumber(number: String = "911")
}
