package no.nordicsemi.android.tasks

import java.nio.charset.Charset
import kotlin.text.Charsets.ISO_8859_1
import okio.ByteString.Companion.encode

/** Factory for HTTP authorization credentials. */
object Credentials {
  /** Returns an auth credential for the Basic scheme. */
  @JvmStatic @JvmOverloads
  fun bearer(
    username: String,
    password: String,
    charset: Charset = ISO_8859_1,
  ): String {
    val usernameAndPassword = "$username:$password"
    val encoded = usernameAndPassword.encode(charset).base64()
    return "Bearer $encoded"
  }
}