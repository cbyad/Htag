package com.upmc.htag.activities

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.upmc.htag.R

class ImageDetailActivity : Activity() {

    lateinit var  image : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_detail)
        image = findViewById(R.id.detail_image)
        val src :String = this.intent.getStringExtra("src")

        Glide.with(this)
                .load(src)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image)
    }
}
