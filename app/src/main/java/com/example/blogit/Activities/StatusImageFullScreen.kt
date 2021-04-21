package com.example.blogit.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.blogit.R
import kotlinx.android.synthetic.main.activity_statusimagefullscreen.*

class StatusImageFullScreen : AppCompatActivity()  {

    private lateinit var pimageString : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statusimagefullscreen)

        val intent: Intent = getIntent()
        pimageString = intent.getStringExtra("pimage").toString()

        Glide.with(statusFullScreenImage).load(pimageString).into(statusFullScreenImage)
    }
}