package com.plcoding.feature.verification.data.di

import com.plcoding.feature.verification.data.KtorFaceVerificationService
import com.plcoding.feature.verification.data.OfflineFirstFaceVerificationRepository
import com.plcoding.feature.verification.domain.FaceVerificationRepository
import com.plcoding.feature.verification.domain.FaceVerificationService
import com.plcoding.feature.verification.domain.UpdateUserVerificationStatusUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val faceVerificationDataModule = module {
    singleOf(::KtorFaceVerificationService) bind FaceVerificationService::class
    singleOf(::OfflineFirstFaceVerificationRepository) bind FaceVerificationRepository::class
    singleOf(::UpdateUserVerificationStatusUseCase)
}
