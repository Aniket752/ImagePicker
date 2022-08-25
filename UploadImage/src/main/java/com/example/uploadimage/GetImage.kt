package com.example.uploadimage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class GetImage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_image)

        Toast.makeText(this,"Hay",Toast.LENGTH_LONG).show()
    }
}