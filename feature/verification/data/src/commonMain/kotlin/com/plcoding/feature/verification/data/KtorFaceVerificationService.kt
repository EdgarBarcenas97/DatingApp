package com.plcoding.feature.verification.data

import com.plcoding.core.data.networking.post
import com.plcoding.core.data.networking.put
import com.plcoding.core.data.networking.safeCall
import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.EmptyResult
import com.plcoding.core.domain.util.Result
import com.plcoding.core.domain.util.map
import com.plcoding.feature.verification.data.dto.ConfirmFaceVerificationRequest
import com.plcoding.feature.verification.data.dto.FaceVerificationResultResponse
import com.plcoding.feature.verification.data.dto.FaceVerificationUrlsResponse
import com.plcoding.feature.verification.data.mappers.toDomain
import com.plcoding.feature.verification.domain.FaceVerificationService
import com.plcoding.feature.verification.domain.models.FaceVerificationResult
import com.plcoding.feature.verification.domain.models.FaceVerificationUrls
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import kotlin.collections.component1
import kotlin.collections.component2

class KtorFaceVerificationService(
    private val httpClient: HttpClient
) : FaceVerificationService {

    override suspend fun getFaceVerificationUploadUrl(
        mimeType: String
    ): Result<FaceVerificationUrls, DataError.Remote> {
        return httpClient.post<Unit, FaceVerificationUrlsResponse>(
            route = "/verification/face-upload-url",
            queryParams = mapOf(
                "mimeType" to mimeType
            ),
            body = Unit
        ).map { it.toDomain() }
    }

    override suspend fun uploadFaceVerificationImage(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote> {
        return safeCall {
            httpClient.put {
                url(uploadUrl)
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                setBody(imageBytes)
            }
        }
    }

    override suspend fun confirmFaceVerification(
        imageUrl: String
    ): Result<FaceVerificationResult, DataError.Remote> {
        return httpClient.post<ConfirmFaceVerificationRequest, FaceVerificationResultResponse>(
            route = "/verification/confirm-face",
            body = ConfirmFaceVerificationRequest(imageUrl)
        ).map { it.toDomain() }
    }
}
