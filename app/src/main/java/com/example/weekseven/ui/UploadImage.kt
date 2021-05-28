package com.example.weekseven.ui

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.renderscript.ScriptGroup
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.example.weekseven.R
import com.example.weekseven.api.ImageAPI
import com.example.weekseven.api.ImageResponse
import com.example.weekseven.databinding.FragmentUploadImageBinding
import com.example.weekseven.util.UploadResponseBody
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_upload_image.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 *
 */
class UploadImage : Fragment(), UploadResponseBody.UploadCallback {

    var fragmentBinding: FragmentUploadImageBinding? = null
    lateinit var binding: FragmentUploadImageBinding
    private var pickedImage: Uri? = null
    private lateinit var selectImage: ImageButton


    companion object {
        //Button click
        private val IMAGE_PICK_CODE = 1000

        //Permission code
        private val PERMISSION_CODE = 1001
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val rootie = inflater.inflate(R.layout.fragment_upload_image, container, false)

        binding = FragmentUploadImageBinding.bind(rootie)

        selectImage = binding.selectImage

        fragmentBinding = binding

        binding.selectImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), UploadImage.PERMISSION_CODE)
                } else {
                    pickImage()
                }
            } else {
                pickImage()
            }
        }

        binding.sendButton.setOnClickListener {
            uploadImage()
        }

        return binding.root
    }

    fun pickImage() {

        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("images/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    fun uploadImage() {
        if (pickedImage == null) {
            Toast.makeText(requireContext(), "Select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val parcelFileDescriptor =
                requireActivity().contentResolver?.openFileDescriptor(pickedImage!!, "r", null)
                        ?: return
        val file = File(requireActivity().cacheDir, requireActivity().contentResolver.getFileName(pickedImage!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        binding.progressBar.visibility = VISIBLE
        binding.progressBar.progress = 0
        val body = UploadResponseBody(file, "image", this)
        ImageAPI().uploadImage(
                MultipartBody.Part.createFormData("file", file.name, body),
                RequestBody.create(MediaType.parse("multipart/form-data"), "This Image was uplaoded by Week7Task")
        ).enqueue(object : Callback<ImageResponse> {

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Snackbar.make(requireView(), " Failure ${t.message} ${t.localizedMessage}", Snackbar.LENGTH_SHORT).show()
                Log.d("FAILURE", "Check out ${t.localizedMessage}, ${t.message}")
            }

            override fun onResponse(
                    call: Call<ImageResponse>,
                    response: Response<ImageResponse>
            ) {
                if (response.isSuccessful) {
                    progressBar.progress = 100
                    Snackbar.make(requireView(), "SUCCESSFUL", Snackbar.LENGTH_SHORT).show()
                    Log.d("SUCCESSFUL", "${response.body().toString()}")
                    binding.sendButton.visibility = View.GONE
                } else {
                    Log.d("Response", "returns $response")
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            pickedImage = data?.data
            selectImage.setImageURI(pickedImage)
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        return name

    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage

    }

}
