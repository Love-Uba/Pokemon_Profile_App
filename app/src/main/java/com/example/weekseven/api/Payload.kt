package com.example.weekseven.api

data class Payload(
        val fileId: String,
        val fileType: String,
        val fileName: String,
        val downloadUri: String,
        val uploadStatus: Boolean
)