package com.example.digiouploadpdfassignment.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiouploadpdfassignment.models.data_models.UploadPDFResponse
import com.example.digiouploadpdfassignment.repository.UploadPDFRepository
import com.example.digiouploadpdfassignment.utils.Const
import com.example.digiouploadpdfassignment.utils.NetworkResult
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadPDFViewModel @Inject constructor(
    private val repository: UploadPDFRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _uploadPdfResponse: MutableLiveData<NetworkResult<UploadPDFResponse>> =
        MutableLiveData()
    val uploadPdfResponse: LiveData<NetworkResult<UploadPDFResponse>> = _uploadPdfResponse

    @RequiresApi(Build.VERSION_CODES.M)
    fun uploadPdfFile(fileName: String, pdfUri: Uri) {
        viewModelScope.launch {
            _uploadPdfResponse.postValue(NetworkResult.Loading())
            if (hasInternetConnection()) {
                val response =
                    repository.uploadPdfFileToServer(Const.getRequestModel(pdfUri, fileName))
                when (response.isSuccessful) {
                    true -> {
                        _uploadPdfResponse.postValue(NetworkResult.Success(response.body()))
                    }
                    else -> {
                        _uploadPdfResponse.postValue(NetworkResult.Error(response.message()))
                    }
                }
            } else {
                _uploadPdfResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false;
        }
    }
}