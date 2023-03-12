package it.unipi.gardenfit.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import it.unipi.gardenfit.GardenFitActivity
import it.unipi.gardenfit.R

/**
 * This class manages the registration/login of the user. It uses a default view.
 */
class RegisterActivity: ComponentActivity() {

    companion object {
        var username: String? = null
    }
    // Tag used while interacting with the LOG
    private val TAG = "RegisterActivity"
    // SharedPreferences file where are stored username and password of the user
    private var preferences: SharedPreferences? = null
    // The sign-in launcher
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_ui)
        preferences = applicationContext
            .getSharedPreferences("file_key", Context.MODE_PRIVATE)
        try {
            createSignInIntent()
        } catch (e: Exception) {
            Log.e(TAG, "onCreate:failure")
        }
    }

    /**
     * Create and launch sign-in intent
     */
    private fun createSignInIntent() {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    /**
     * Check the authentication result and write it down
     * on the SharedPreferences' file. It also gives the user some feedbacks about it.
     */
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if(response == null) Log.e(TAG, "onSignInResult:failure")

        // Successfully signed in
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                preferences?.edit()?.putString("USERNAME", user.email)?.apply()
                username = user.email
            }

            val intent = Intent(this, GardenFitActivity::class.java)
            startActivity(intent)
        }
        // Cannot sign in
        else {
            Log.e(TAG, "onSignInResult:failure");
            Toast.makeText(this, "Registration failed.",
                Toast.LENGTH_SHORT).show();
        }
    }
}