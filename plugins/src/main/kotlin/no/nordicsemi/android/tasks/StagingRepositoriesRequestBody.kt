package no.nordicsemi.android.tasks

import com.squareup.moshi.Json

data class StagingRepositoriesRequestBody(
    @field:Json(name = "data") val data: StagingRepositoriesRequest
)

data class StagingRepositoriesRequest(
    @field:Json(name = "description")
    val description: String = "",
    @field:Json(name = "stagedRepositoryIds")
    val stagedRepositoryIds: List<String>
)
