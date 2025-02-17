package com.example.weatherforecast.exception

sealed class EncryptionException(code: Int, tag: String, message: String) : SecurityException(code, tag,
    message
){
    companion object {
        val ENCRYPTION_CODE = 550;
    }
    data class EncryptionError(val log: String) : EncryptionException(ENCRYPTION_CODE+1, "EncryptionException",log)
    object SecretKeyRetrievalError : EncryptionException(ENCRYPTION_CODE+2, "SecretKeyRetrievalError", "Couldn't retrieve secret key")
}