package com.example.nycschoolsapplication.data

class SchoolRepository(private val api: SchoolApi) {

        suspend fun fetchSchools(): List<School> {
            return api.fetchSchools()
        }
}