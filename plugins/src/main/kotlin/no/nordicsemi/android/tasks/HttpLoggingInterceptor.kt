package no.nordicsemi.android.tasks

import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.Protocol
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

/**
 * Copy paste square HttpLoggingInterceptor and migrate to use okhttp3.
 */
class HttpLoggingInterceptor @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT) :
    Interceptor {
    enum class Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    interface Logger {
        fun log(message: String?)

        companion object {
            /** A [Logger] defaults output appropriate for the current platform.  */
            val DEFAULT: Logger = object : Logger {
                override fun log(message: String?) {
                    println(message)
                }
            }
        }
    }

    @Volatile
    private var level = Level.BODY

    /** Change the level at which this interceptor logs.  */
    fun setLevel(level: Level) {
        this.level = level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = level
        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }
        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS
        val requestBody = request.body()
        val hasRequestBody = requestBody != null
        if (logHeaders) {
            val headers = request.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                logger.log(headers.name(i) + ": " + headers.value(i))
                i++
            }
            var endMessage: String = "--> END " + request.method()
            if (logBody && hasRequestBody) {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)
                val charset = UTF8
                val contentType = requestBody.contentType()
                contentType?.charset(UTF8)
                logger.log("")
                logger.log(buffer.readString(charset))
                endMessage += " (" + requestBody.contentLength() + "-byte body)"
            }
            logger.log(endMessage)
        }
        val response = chain.proceed(request)
        val responseBody = response.body()
        if (logHeaders) {
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                logger.log(headers.name(i) + ": " + headers.value(i))
                i++
            }
            var endMessage: String = "<-- END HTTP"
            if (logBody) {
                val source = responseBody?.source()
                source?.request(Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source?.buffer
                var charset: Charset? = UTF8
                val contentType = responseBody?.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }
                if (responseBody?.contentLength() != 0L) {
                    logger.log("")
                    logger.log(buffer?.clone()?.readString((charset)!!))
                }
                endMessage += " (" + buffer?.size + "-byte body)"
            }
            logger.log(endMessage)
        }
        return response
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
        private fun protocol(protocol: Protocol): String {
            return if (protocol == Protocol.HTTP_1_0) "HTTP/1.0" else "HTTP/1.1"
        }

        private fun requestPath(url: HttpUrl): String {
            val path = url.encodedPath()
            val query = url.encodedQuery()
            return if (query != null) ("$path?$query") else path
        }
    }
}
