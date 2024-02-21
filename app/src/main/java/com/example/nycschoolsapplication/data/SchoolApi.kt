package com.example.nycschoolsapplication.data

import retrofit2.http.GET

interface SchoolApi {

    @GET("s3k6-pzi2.json")
    suspend fun fetchSchools(): List<SchoolInfo>

}
