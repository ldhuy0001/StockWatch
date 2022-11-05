package com.example.stockwatch_assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel(){


    private var username = MutableLiveData("Empty!")
    fun observeUserName(): LiveData<String>{
        return username
    }

    fun updateTest(){
        username.postValue(FirebaseAuth.getInstance().currentUser?.displayName)
    }
}