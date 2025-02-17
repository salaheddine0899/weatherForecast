package com.example.weatherforecast.exception

sealed class DecryptionException(code: Int, tag: String, message: String) : SecurityException(code, tag,
    message
){
    companion object{
        val DECRYPTION_CODE = 500;
    }
    data class BadTagException(val log: String) : DecryptionException(DECRYPTION_CODE+1, "BadTagException",log)
    object SecretKeyRetrievalError : DecryptionException(DECRYPTION_CODE+2, "SecretKeyRetrievalError", "Couldn't retrieve secret key")

}