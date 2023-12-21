package no.nordicsemi.android.tasks

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SonatypeService {

    @GET("service/local/staging/profile_repositories")
    fun getStagingRepositories(): Call<StagingRepositoryResponseBody>

    @POST("service/local/staging/bulk/close")
    fun closeStagingRepositories(@Body body: StagingRepositoriesRequestBody): Call<Unit>

    @POST("service/local/staging/bulk/promote")
    fun releaseStagingRepositories(@Body body: StagingRepositoriesRequestBody): Call<Unit>

    @POST("service/local/staging/bulk/drop")
    fun dropStagingRepositories(@Body body: StagingRepositoriesRequestBody): Call<Unit>
}
