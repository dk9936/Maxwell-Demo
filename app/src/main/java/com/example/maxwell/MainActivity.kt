package com.example.maxwell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.maxwell.databinding.ActivityMainBinding
import com.google.firebase.appdistribution.FirebaseAppDistribution
import com.google.firebase.appdistribution.FirebaseAppDistributionException
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "FirebaseAppDistribution"
    }

    private lateinit var firebaseAppDistribution: FirebaseAppDistribution
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAppDistribution = Firebase.appDistribution
        binding.updatebutton.setOnClickListener { _ ->
            checkForUpdate()
        }
        binding.signinButton.setOnClickListener { _ ->
            signIn()
        }
    }



    override fun onResume() {
        super.onResume()
        configureUpdateButton()
        configureSigninButton()
    }

    private fun checkForUpdate() {
        firebaseAppDistribution.updateIfNewReleaseAvailable()
            .addOnProgressListener { updateProgress ->
                // (Optional) Implement custom progress updates in addition to
                // automatic NotificationManager updates.
                Log.d(TAG, "checkForUpdate: $updateProgress")
            }
            .addOnFailureListener { e ->
                if (e is FirebaseAppDistributionException) {
                    // Handle exception.
                    Log.d(TAG, "checkForUpdate: "+e.message)
                }
            }
    }

    private fun signIn() {
        if (isTesterSignedIn()) {
            firebaseAppDistribution.signOutTester()
            configureUpdateButton()
            configureSigninButton()
        } else {
            firebaseAppDistribution.signInTester()
        }
    }

    private fun isTesterSignedIn() : Boolean {
        return firebaseAppDistribution.isTesterSignedIn
    }

    private fun configureUpdateButton() {
        binding.updatebutton.visibility = if (isTesterSignedIn()) View.VISIBLE else View.GONE
    }

    private fun configureSigninButton() {
        binding.signinButton.text = if (isTesterSignedIn()) "Sign Out" else "Sign In"
        binding.signinButton.visibility = View.VISIBLE
    }



}