package com.plcoding.feature.verification.presentation.di

import com.plcoding.feature.verification.presentation.face_verification.FaceVerificationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val faceVerificationPresentationModule = module {
    viewModelOf(::FaceVerificationViewModel)
}
