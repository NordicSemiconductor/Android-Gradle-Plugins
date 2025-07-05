package no.nordicsemi.android.tasks

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OssrhStagingApi {

    @POST("/manual/upload/defaultRepository/{requested_namespace}")
    fun uploadDefaultRepository(
        @Path("requested_namespace") namespace: String,
        @Query("publishing_type") publishingType: String
    ): Call<Unit>
}
