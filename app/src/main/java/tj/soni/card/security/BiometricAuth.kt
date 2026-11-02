package tj.soni.card.security

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat

object BiometricAuth {
    fun isAvailable(activity: FragmentActivity): Boolean = BiometricManager.from(activity).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS
    fun prompt(activity: FragmentActivity, onResult: (Boolean) -> Unit) {
        val executor = ContextCompat.getMainExecutor(activity)
        val prompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) { onResult(true) }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) { onResult(false) }
            override fun onAuthenticationFailed() { onResult(false) }
        })
        val info = BiometricPrompt.PromptInfo.Builder().setTitle("SONI CARD").setSubtitle("Тасдиқи шахсият").setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL).build()
        prompt.authenticate(info)
    }
}
