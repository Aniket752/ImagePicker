package com.example.uploadimage

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.uploadimage.databinding.ActivityGetImageBinding
import com.example.uploadimage.databinding.AlertDialogLayoutBinding
import java.io.*

class GetImage : AppCompatActivity() {

    private lateinit var binding: ActivityGetImageBinding
    var intent1 = Intent()
    var size: Int? = null
    var fileName : String? = null
    lateinit var finalFile: File
    private val result: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                if (it.data?.data == null) {
                    binding.preview.setImageURI(finalFile.toUri())
                    var bitmap = BitmapFactory.decodeFile(finalFile.absolutePath)
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        FileOutputStream(finalFile)
                    )
                    val com = 1024 * 1024
                    intent1.data = finalFile.absolutePath.toUri()
                    var actualSize = finalFile.length() / com
                    size?.let {
                        if(it > 0){
                            if (actualSize > it) {
                                bitmap = BitmapFactory.decodeStream(FileInputStream(finalFile))
                                val actuleSize = (it.toFloat()/ actualSize.toFloat())
                                actualSize = (actuleSize * 100).toLong()
                                bitmap.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    actualSize.toInt(),
                                    FileOutputStream(finalFile)
                                )
                            }
                        }
                    }
                    binding.preview.setImageURI(finalFile.toUri())
                    binding.done.isEnabled = true
                } else {
                    val uri = it.data?.data as Uri
                    binding.preview.visibility = View.VISIBLE
                    val path = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                    try {
                        val file = File(path, "/Image")
                        file.mkdirs()
                        val image =  if (fileName == null) File(file.absolutePath, "image1.jpeg")
                        else
                            File(file.absolutePath, "$fileName.jpeg")
                        intent1.data = image.absolutePath.toUri()
                        var bitmap =
                            BitmapFactory.decodeStream(this.contentResolver.openInputStream(uri))
                        val out = FileOutputStream(image)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        println(image.length() / 1024)
                        val com = 1024 * 1024
                        var actualSize = image.length() / com
                        size?.let {
                            if(it >0){
                                if (actualSize > it) {
                                    bitmap = BitmapFactory.decodeStream(FileInputStream(image))
                                    val actuleSize = (it.toFloat() / actualSize.toFloat())
                                    actualSize = (actuleSize * 100).toLong()
                                    bitmap.compress(
                                        Bitmap.CompressFormat.JPEG,
                                        actualSize.toInt(),
                                        FileOutputStream(image)
                                    )
                                }
                            }
                        }
                        out.flush()
                        out.close()
                        finalFile = image
                        binding.preview.setImageURI(image.toUri())
                        binding.done.isEnabled = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            } else
                Toast.makeText(this, "Invalid data", Toast.LENGTH_LONG).show()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityGetImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            size = it.getIntExtra("size", 0)
            fileName = it.getStringExtra("name")
        }

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
            binding.preview.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_image_svgrepo_com))
            dialog.dismiss()
            val path = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(path, "/Image")
            file.mkdirs()
            val image = if(fileName == null) File(file.absolutePath, "image1.jpeg")
            else File(file.absolutePath, "$fileName.jpeg")
            println(image.absolutePath +" Path")
            finalFile = image
            result.launch(
                Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE).putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(this, application.packageName + ".provider", image)
                )
            )
        }

        dialogView.gallery.setOnClickListener {
            binding.preview.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_image_svgrepo_com))
            dialog.dismiss()
            result.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
        dialogView.files.setOnClickListener {
            binding.preview.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_image_svgrepo_com))
            dialog.dismiss()
            result.launch(
                Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
                    .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            )
        }

    }
}