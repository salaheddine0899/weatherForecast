package com.example.weatherforecast.helper

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProperties.BLOCK_MODE_GCM
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA256
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT
import android.security.keystore.KeyProperties.PURPOSE_SIGN
import android.security.keystore.KeyProperties.PURPOSE_VERIFY
import android.util.Base64
import com.example.weatherforecast.BuildConfig.BIOMETRIC_TOKEN_STRING
import com.example.weatherforecast.exception.DecryptionException
import com.example.weatherforecast.exception.EncryptionException
import com.example.weatherforecast.utils.GlobalConstants.AES_GCM_NO_PADDING
import com.example.weatherforecast.utils.GlobalConstants.HMAC_SHA256
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.UnrecoverableKeyException
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class AESCipherHelper
@Inject
constructor() {
    private val IV_LENGTH = 12
    private val T_LENGTH = 128
    private val provider = "AndroidKeyStore"
    private val charset by lazy {
        charset("UTF-8")
    }

    companion object {
        const val KEY_ALIAS = "ama-project-secret-key"
        const val BIOMETRIC_SECRET_KEY_ALIAS = "biometric_key"
    }

    private val keyStore by lazy {
        KeyStore.getInstance(provider).apply {
            load(null)
        }
    }

    private val keyGenerator by lazy {
        KeyGenerator.getInstance(KEY_ALGORITHM_AES, provider)
    }
    private val keyGeneratorHmac by lazy {
        KeyGenerator.getInstance(KEY_ALGORITHM_HMAC_SHA256, provider)
    }

    fun encryptData(jsonString: String): String {
        try {// Generate secret key for the alias
            val secretKey = getSecretKey(KEY_ALIAS)

            val cipher = Cipher.getInstance(AES_GCM_NO_PADDING)
            // Initialize the cipher in ENCRYPT_MODE
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            // Get the IV generated for this encryption
            val iv = cipher.iv

            // Encrypt the data
            val encryptedValue = cipher.doFinal(jsonString.toByteArray(charset))

            // Prepend the IV to the encrypted data (IV + CipherText)
            val ivAndCipherText = iv + encryptedValue

            // Return Base64-encoded IV + CipherText
            return Base64.encodeToString(ivAndCipherText, Base64.DEFAULT)
        } catch (ex: Exception) {
            throw EncryptionException.EncryptionError("Couldn't encrypt information for KEY_ALIAS: ${KEY_ALIAS} , jsonString : ${jsonString} , error : ${ex.message} ")
        }
    }

    fun decryptData(encryptedData: String): String {
        try {
            // Get the secret key from the keystore
            val secretKey = getSecretKey(KEY_ALIAS)

            // Decode the Base64-encoded data
            val decodedBytes = Base64.decode(encryptedData, Base64.DEFAULT)

            // Extract the IV (first 16 bytes for CBC)
            val iv = decodedBytes.copyOfRange(0, IV_LENGTH)

            // Extract the actual encrypted data (everything after the IV)
            val cipherText = decodedBytes.copyOfRange(IV_LENGTH, decodedBytes.size)

            val cipher = Cipher.getInstance(AES_GCM_NO_PADDING)

            // Initialize the cipher in DECRYPT_MODE with the extracted IV
            val ivParameterSpec = GCMParameterSpec(T_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)

            // Decrypt the data
            val decryptedValue = cipher.doFinal(cipherText)

            // Return the decrypted string
            return String(decryptedValue, charset)
        } catch (ex: AEADBadTagException) {
            val decodedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
            throw DecryptionException.BadTagException(
                "Couldn't fetch secret key for secretKey ${getSecretKey(KEY_ALIAS)} ,keyAlias: ${KEY_ALIAS} , encryptedData : ${encryptedData} , decodedBytes : ${decodedBytes} , iv ${
                    decodedBytes.copyOfRange(
                        0,
                        IV_LENGTH
                    )
                } ,  cipherText : ${
                    decodedBytes.copyOfRange(
                        IV_LENGTH,
                        decodedBytes.size
                    )
                } , error : ${ex.message} "
            )
        }
    }

    private fun generateSecretKey(
        alias: String,
        forHMac: Boolean = false,
        withAuth: Boolean = false
    ): SecretKey {
        var keyParams = KeyGenParameterSpec
            .Builder(
                alias,
                if (forHMac) PURPOSE_SIGN or PURPOSE_VERIFY else PURPOSE_ENCRYPT or PURPOSE_DECRYPT
            )
        if (!forHMac) {
            keyParams = keyParams
                .setBlockModes(BLOCK_MODE_GCM)
                .setEncryptionPaddings(ENCRYPTION_PADDING_NONE);
        }
        if (withAuth) {
            keyParams = keyParams.setUserAuthenticationRequired(true)
                .setInvalidatedByBiometricEnrollment(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                keyParams = keyParams
                    .setUserAuthenticationParameters(10, KeyProperties.AUTH_BIOMETRIC_STRONG)
            }
        }
        return (if (forHMac) keyGeneratorHmac else keyGenerator).apply {
            init(

                keyParams.build()
            )
        }.generateKey()
    }

    fun removeKey() {
        keyStore.deleteEntry(KEY_ALIAS)
    }

    fun generateBiometricToken(): String {
        val secretKey = getSecretKey(BIOMETRIC_SECRET_KEY_ALIAS, withHMac = true, withAuth = true)
        val mac = Mac.getInstance(HMAC_SHA256)
        mac.init(secretKey)
        val encryptedToken = mac.doFinal(BIOMETRIC_TOKEN_STRING.toByteArray(charset))
        return Base64.encodeToString(encryptedToken, Base64.DEFAULT)
    }


    private fun getSecretKey(
        alias: String,
        withHMac: Boolean = false,
        withAuth: Boolean = false
    ): SecretKey {
        try {
            return if (keyStore.isKeyEntry(alias)) {
                val entry = keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry
                entry.secretKey
            } else {
                generateSecretKey(alias, withHMac, withAuth)
            }
        } catch (ex: Exception) {
            return when (ex) {
                is UnrecoverableKeyException -> {
                    generateSecretKey(alias, withHMac, withAuth)
                }

                is KeyStoreException -> {
                    generateSecretKey(alias, withHMac, withAuth)
                }

                else -> {
                    throw ex
                }
            }
        }
    }

}