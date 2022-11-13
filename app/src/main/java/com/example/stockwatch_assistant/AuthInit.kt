package com.example.stockwatch_assistant

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class AuthInit(viewModel: MainViewModel, signInLauncher: ActivityResultLauncher<Intent>) {
    init {
        val user = FirebaseAuth.getInstance().currentUser
        Log.d("MMM","User from authinit $user")
        if (user == null){
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
            signInLauncher.launch(signInIntent)

            Log.d("MMM"," authinit null called")

        } else{

            Log.d("MMM"," authinit not null called")

            Log.d("ck", "XXX user ${user.displayName} email ${user.email}")
            viewModel.updateUser()
        }
    }
}