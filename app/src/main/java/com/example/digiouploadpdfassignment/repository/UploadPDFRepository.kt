package com.example.digiouploadpdfassignment.repository

import com.example.digiouploadpdfassignment.models.data_models.UploadPDFRequestBody
import com.example.digiouploadpdfassignment.models.data_models.UploadPDFResponse
import com.example.digiouploadpdfassignment.models.remote.PDFUploadApiService
import retrofit2.Response
import javax.inject.Inject

class UploadPDFRepository @Inject constructor(
    private val pdfUploadApiService: PDFUploadApiService) {

    suspend fun uploadPdfFileToServer(uploadPDFRequestBody: UploadPDFRequestBody): Response<UploadPDFResponse> {
        return pdfUploadApiService.uploadPdfToServer(uploadPDFRequestBody)
    }

}