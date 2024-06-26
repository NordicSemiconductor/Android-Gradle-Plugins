package no.nordicsemi.android.tasks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StagingRepositoriesRequestBody(
    @field:Json(name = "data") val data: StagingRepositoriesRequest
)

@JsonClass(generateAdapter = true)
data class StagingRepositoriesRequest(
    @field:Json(name = "description")
    val description: String = "",
    @field:Json(name = "stagedRepositoryIds")
    val stagedRepositoryIds: List<String>
)
