package no.nordicsemi.android.tasks

import com.squareup.moshi.Json

data class StagingRepositoryResponseBody(
    @field:Json(name = "data") val data: List<StagingRepositoryResponse>
)

data class StagingRepositoryResponse(
    @field:Json(name = "repositoryId") val id: String
)
