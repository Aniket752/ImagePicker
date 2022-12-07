package com.example.imagepicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.uploadimage.GetImage

class MainActivity : AppCompatActivity() {
    lateinit var image : ImageView
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.image)
        button = findViewById(R.id.start)
        button.setOnClickListener {
            val intent = Intent(this,GetImage::class.java)
            intent.putExtra("size",2)
            intent.putExtra("name","Aniket")
            startActivityForResult(intent,100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        image.setImageDrawable(getDrawable(com.example.uploadimage.R.drawable.ic_image_svgrepo_com))
        if(resultCode == RESULT_OK){
            data?.data?.let {
                image.setImageURI(it)
            }
        }
    }
}