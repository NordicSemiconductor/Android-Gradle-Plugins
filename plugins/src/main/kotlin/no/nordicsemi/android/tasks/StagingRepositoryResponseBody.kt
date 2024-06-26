package no.nordicsemi.android.tasks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StagingRepositoryResponseBody(
    @field:Json(name = "data") val data: List<StagingRepositoryResponse>
)

@JsonClass(generateAdapter = true)
data class StagingRepositoryResponse(
    @field:Json(name = "repositoryId") val id: String
)
