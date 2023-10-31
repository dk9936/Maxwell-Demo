package com.example.maxwell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.appdistribution.FirebaseAppDistribution
import com.google.firebase.appdistribution.FirebaseAppDistributionException

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: Testing application v2")

    }

    override fun onResume() {
        super.onResume()

        // Copy and paste this into any part of your app - for example, in your main
// activity's onResume method.
        val firebaseAppDistribution = FirebaseAppDistribution.getInstance()
        firebaseAppDistribution.updateIfNewReleaseAvailable()
            .addOnProgressListener { updateProgress ->
                // (Optional) Implement custom progress updates in addition to
                // automatic NotificationManager updates.
                Log.d(TAG, "onResume: $updateProgress")
            }
            .addOnFailureListener { e ->
                // (Optional) Handle errors.
                if (e is FirebaseAppDistributionException) {
                    when (e.errorCode) {
                        FirebaseAppDistributionException.Status.NOT_IMPLEMENTED -> {
                            // SDK did nothing. This is expected when building for Play.
                            Log.d(TAG, "onResume: nothing")
                        }
                        else -> {
                            // Handle other errors.
                            Log.d(TAG, "onResume: other errors")
                        }
                    }
                }
            }
    }

}