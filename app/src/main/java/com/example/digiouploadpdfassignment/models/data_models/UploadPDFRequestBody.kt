package com.example.digiouploadpdfassignment.models.data_models

data class UploadPDFRequestBody(
    val signers: List<SignersItem>,
    val fileName: String,
    val fileData: String,
    val expire_in_days: Int,
    val display_on_page: String,
)

data class SignersItem(
    val identifier: String,
    val name: String,
    val reason: String,
)