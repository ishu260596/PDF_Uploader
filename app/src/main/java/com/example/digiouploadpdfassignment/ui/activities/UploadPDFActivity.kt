package com.example.digiouploadpdfassignment.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.assignment.uploadpdf.R
import com.assignment.uploadpdf.databinding.ActivityMainBinding
import com.example.digiouploadpdfassignment.utils.NetworkResult
import com.example.digiouploadpdfassignment.viewmodel.UploadPDFViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadPDFActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var uploadPDFViewModel: UploadPDFViewModel
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private var pdfUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
    }

    private fun initListeners() {
        uploadPDFViewModel = ViewModelProvider(this)[UploadPDFViewModel::class.java]
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val data: Intent? = result.data
            data.let { it ->
                pdfUri = it?.data
                pdfUri.let { uri ->
                    val fileName: String = uri?.lastPathSegment.toString()
                    binding.btnUploadPdf.isEnabled = true
                    binding.btnUploadPdf.alpha = 1.0f
                    if (!TextUtils.isEmpty(fileName))
                        binding.tvPdfName.text = fileName
                    else
                        binding.tvPdfName.text = "My Document.pdf"
                }
            }
        }

        uploadPDFViewModel.uploadPdfResponse.observe(this) { response ->
            when (response) {

                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Success -> {
                    response.data?.let { result ->
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Document Uploaded successfully!", Toast.LENGTH_LONG).show()
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(this, response.message.toString(), Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }

                else -> {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
            }

        }

        binding.tvTextChoose.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                )
            } else {
                selectPDF()
            }
        }

        binding.btnUploadPdf.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            uploadPDFViewModel.uploadPdfFile(binding.tvPdfName.text.toString(), pdfUri!!)
        }

    }

    private fun selectPDF() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        resultLauncher?.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty()
            && grantResults[0]
            == PackageManager.PERMISSION_GRANTED
        ) {
            selectPDF();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}