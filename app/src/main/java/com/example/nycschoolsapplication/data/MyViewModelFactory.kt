package com.example.nycschoolsapplication.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SchoolViewModel::class.java)) {
            return SchoolViewModel(SchoolRepository(RetrofitClient.instance)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}