package com.example.maxwell

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.maxwell.databinding.ActivityMainBinding
import com.google.firebase.appdistribution.FirebaseAppDistribution
import com.google.firebase.appdistribution.FirebaseAppDistributionException
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "FirebaseAppDistribution"
        //private const val REQUEST_CODE = 123 // Unique request code for permission
    }

    private lateinit var firebaseAppDistribution: FirebaseAppDistribution
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, you can post the notification
                checkForUpdate()
            } else {
                // Permission denied, handle this case (e.g., show a message to the user)
                Log.d(TAG, "Permission denied for posting notifications.")
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAppDistribution = Firebase.appDistribution
        binding.updatebutton.setOnClickListener { _ ->
            // Check for permission before updating
            requestNotificationPermission()
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it from the user
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Permission is already granted, you can post the notification
            checkForUpdate()
        }
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
                    Log.d(TAG, "checkForUpdate: " + e.message)
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

    private fun isTesterSignedIn(): Boolean {
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