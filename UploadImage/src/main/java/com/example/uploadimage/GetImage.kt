package com.example.uploadimage

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.example.uploadimage.databinding.ActivityGetImageBinding
import com.example.uploadimage.databinding.AlertDialogLayoutBinding
import java.io.*

class GetImage : AppCompatActivity() {

    private lateinit var binding: ActivityGetImageBinding
    var intent1 = Intent()
    lateinit var finalFile: File
    private val result: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                if (it.data == null) {
                    binding.preview.setImageURI(finalFile.toUri())
                    var bitmap = BitmapFactory.decodeFile(finalFile.absolutePath)
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        FileOutputStream(finalFile)
                    )
                    println(finalFile.length() / 1024)
                    val com = 1024 * 1024
                    intent1.data = finalFile.absolutePath.toUri()
                    var size = finalFile.length() / com
                    if (size > 2) {
                        bitmap = BitmapFactory.decodeStream(FileInputStream(finalFile))
                        var actuleSize = (2.0 / size.toFloat())
                        size = (actuleSize * 100).toLong()
                        println(actuleSize.toString() + " Com")
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            size.toInt(),
                            FileOutputStream(finalFile)
                        )
                    }
                    binding.preview.setImageURI(finalFile.toUri())
                } else {
                    val uri = it.data?.data as Uri
                    binding.preview.visibility = View.VISIBLE
                    val path = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                    try {
                        val file = File(path, "/Image")
                        file.mkdirs()
                        val image = File(file.absolutePath, "image1.jpeg")
                        intent1.data = image.absolutePath.toUri()
                        var bitmap =
                            BitmapFactory.decodeStream(this.contentResolver.openInputStream(uri))
                        val out = FileOutputStream(image)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        println(image.length() / 1024)
                        val com = 1024 * 1024
                        var size = image.length() / com
                        if (size > 2) {
                            bitmap = BitmapFactory.decodeStream(FileInputStream(image))
                            var actuleSize = (2.0 / size.toFloat())
                            size = (actuleSize * 100).toLong()
                            println(actuleSize.toString() + " Com")
                            bitmap.compress(
                                Bitmap.CompressFormat.JPEG,
                                size.toInt(),
                                FileOutputStream(image)
                            )
                        }

                        println(image.length())
                        out.flush()
                        out.close()
                        finalFile = image
                        binding.preview.setImageURI(image.toUri())
                        println(image.absolutePath)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            } else
                Toast.makeText(this, "Invalid data", Toast.LENGTH_LONG).show()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityGetImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.select.setOnClickListener {
            uploadImage()
        }

        binding.done.setOnClickListener {

            val bundle = Bundle().also {
                it.putString("path", finalFile.absolutePath)
            }
            intent1.putExtra("path", bundle)
            setResult(RESULT_OK, intent1)
            finish()

        }
    }

    private fun uploadImage() {

        val dialog = AlertDialog.Builder(this).create()
        val dialogView = AlertDialogLayoutBinding.inflate(layoutInflater)
        dialog.setView(dialogView.root)
        dialog.show()

        dialogView.camera.setOnClickListener {
            binding.preview.setImageDrawable(getDrawable(R.drawable.ic_image_svgrepo_com))
            dialog.dismiss()
            val path = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(path, "/Image")
            file.mkdirs()
            val image = File(file.absolutePath, "image1.jpeg")
            finalFile = image
            result.launch(
                Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE_SECURE).putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(this, application.packageName+".provider", image)
                )
            )
        }

        dialogView.gallery.setOnClickListener {
            binding.preview.setImageDrawable(getDrawable(R.drawable.ic_image_svgrepo_com))
            dialog.dismiss()
            result.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
        dialogView.files.setOnClickListener {
            binding.preview.setImageDrawable(getDrawable(R.drawable.ic_image_svgrepo_com))
            dialog.dismiss()
            result.launch(
                Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
                    .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            )
        }

    }
}