package com.example.nycschoolsapplication.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SchoolViewModel(private val repository: SchoolRepository) : ViewModel() {

    private val _schools = MutableStateFlow(Result.Loading as Result)
    val schools: StateFlow<Result> = _schools

    private val _selectedSchool = MutableStateFlow(SchoolInfo())
    val selectedSchool: StateFlow<SchoolInfo> = _selectedSchool

    fun fetchSchools() {
        viewModelScope.launch {
            try{
                _schools.value = Result.Success(repository.fetchSchools())
            } catch (e: Exception) {
                _schools.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun selectSchool(school: SchoolInfo) {
        _selectedSchool.value = school
    }

    sealed class Result {
        data object Loading : Result()
        data class Success(val schools: List<SchoolInfo>) : Result()
        data class Error(val message: String) : Result()
    }
}