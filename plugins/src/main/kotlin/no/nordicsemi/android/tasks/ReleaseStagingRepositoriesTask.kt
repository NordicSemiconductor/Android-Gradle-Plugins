package no.nordicsemi.android.tasks

import com.squareup.okhttp.Credentials
import okhttp3.OkHttpClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

open class ReleaseStagingRepositoriesTask : DefaultTask() {

    @TaskAction
    fun releaseStagingRepositories() {
        val username = System.getenv("OSSR_USERNAME")
        val password = System.getenv("OSSR_PASSWORD")

        val credentials = Credentials.basic(username, password)

        val client = OkHttpClient.Builder()
                //only for testing
//            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header("Authorization", credentials)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .build()
                )
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://oss.sonatype.org")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()

        val service = retrofit.create(SonatypeService::class.java)

        releaseStagingRepositories(service)
    }

    private fun releaseStagingRepositories(service: SonatypeService) {
        val response = service.getStagingRepositories().execute()

        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        val ids = response.body()?.data?.map { it.id } ?: throw RuntimeException("No staging id.")

        val requestBody = StagingRepositoriesRequestBody(StagingRepositoriesRequest(stagedRepositoryIds = ids))

        val closeResponse = service.closeStagingRepositories(requestBody).execute()

        if (!closeResponse.isSuccessful) {
            throw HttpException(closeResponse)
        }

        Thread.sleep(120000) //Wait for the repository to close. Been too lazy to write retry mechanism.

        val releaseResponse = service.releaseStagingRepositories(requestBody).execute()

        if (!releaseResponse.isSuccessful) {
            throw HttpException(releaseResponse)
        }
    }
}
