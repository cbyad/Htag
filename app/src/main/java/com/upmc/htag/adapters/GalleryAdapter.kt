package com.upmc.htag.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.upmc.htag.R
import com.upmc.htag.models.MediaContent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.upmc.htag.activities.ImageDetailActivity

/**
 * Class to display image checked by user
 */
class GalleryAdapter(val medias: ArrayList<MediaContent>, val context: Context) :
        RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return medias.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gallery_model, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMedia = medias[position]
        holder.display(currentMedia, context, medias)

    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var currentMedia: MediaContent? = null
        var mPhotoImageView: ImageView
        var mTag: TextView

        init {
            mPhotoImageView = view.findViewById(R.id.iv_photo)
            mTag = view.findViewById(R.id.iv_photo_tags)
        }


        fun display(media: MediaContent, ctx: Context, medias: ArrayList<MediaContent>) {
            currentMedia = media
            mTag.text = media.title
            Glide.with(ctx)
                    .load(media.src)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mPhotoImageView)

            mPhotoImageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val m = medias[position]
                    var intent = Intent(ctx, ImageDetailActivity::class.java)
                    intent.putExtra("src", m.src)
                    startActivity(ctx, intent, null)
                }
            }


        }
    }
}