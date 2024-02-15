package com.example.nycschoolsapplication.data

import com.google.gson.annotations.SerializedName

data class School (
    @SerializedName("dbn")
    val dbn: String,
    @SerializedName("school_name")
    val school_name: String,
    @SerializedName("overview_paragraph")
    val overview_paragraph: String)
