package com.example.digiouploadpdfassignment.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiouploadpdfassignment.models.data_models.UploadPDFResponse
import com.example.digiouploadpdfassignment.repository.UploadPDFRepository
import com.example.digiouploadpdfassignment.utils.Const
import com.example.digiouploadpdfassignment.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadPDFViewModel @Inject constructor(
    private val repository: UploadPDFRepository
) : ViewModel() {

    private val _uploadPdfResponse: MutableLiveData<NetworkResult<UploadPDFResponse>> = MutableLiveData()
    val uploadPdfResponse: LiveData<NetworkResult<UploadPDFResponse>> = _uploadPdfResponse

    fun uploadPdfFile(fileName: String, pdfUri: Uri) {
        viewModelScope.launch {
            _uploadPdfResponse.postValue(NetworkResult.Loading())
            val response = repository.uploadPdfFileToServer(Const.getRequestModel(pdfUri, fileName))
            when (response.isSuccessful) {
                true -> {
                    _uploadPdfResponse.postValue(NetworkResult.Success(response.body()))
                }
                else -> {
                    _uploadPdfResponse.postValue(NetworkResult.Error(response.message()))
                }
            }
        }
    }
}