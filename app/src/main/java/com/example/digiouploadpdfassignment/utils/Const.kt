package com.example.digiouploadpdfassignment.utils

import android.net.Uri
import android.util.Base64
import com.example.digiouploadpdfassignment.models.data_models.SignersItem
import com.example.digiouploadpdfassignment.models.data_models.UploadPDFRequestBody
import java.net.URI

object Const {
    const val BASE_URL = "https://ext.digio.in:444/"

    const val CLIENT_ID = "AIZ67DUSNZ8TGWJV4DZ7DI3T5Z2LN2W2"
    const val CLIENT_SECRET = "ASN9AVKHU6HF41KTU71G3KNXLG1ET7BC"

    fun getRequestModel(pdfUri: Uri, fileName: String): UploadPDFRequestBody {

        val singer = SignersItem(
            identifier = "kumarishwar623@gmail.com",
            name = "Ishwar Kumar",
            reason = "Property Signature"
        )

        val singersList: MutableList<SignersItem> = mutableListOf()
        singersList.add(singer)

        return UploadPDFRequestBody(
            signers = singersList,
            fileName,
            fileData = convertStringToBase64(
                pdfUri.toString()),
            10,
            "All"
        )
    }

    fun convertStringToBase64(text: String): String {
        var finalString = ""
        try {
            finalString = Base64.encodeToString(text.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return finalString
    }

    fun getAuth(): String {
        return convertStringToBase64("$CLIENT_ID:$CLIENT_SECRET")
    }
}