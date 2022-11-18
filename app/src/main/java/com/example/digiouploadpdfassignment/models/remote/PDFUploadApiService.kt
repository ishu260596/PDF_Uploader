package com.example.digiouploadpdfassignment.models.remote

import com.example.digiouploadpdfassignment.models.data_models.UploadPDFRequestBody
import com.example.digiouploadpdfassignment.models.data_models.UploadPDFResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PDFUploadApiService {

    @POST("v2/client/document/upload")
    suspend fun uploadPdfToServer(
        @Body uploadPDFRequestBody: UploadPDFRequestBody
    ): Response<UploadPDFResponse>
}