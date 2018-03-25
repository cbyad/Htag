package com.upmc.htag.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.upmc.htag.R
import com.upmc.htag.models.MediaContent

/**
 * Created by cb_mac on 22/02/2018.
 */

/**
 * Class to display content saved by user
 */
class GalleryAdapter(val medias: List<MediaContent>, val context: Context) :
        RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return medias.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMedia = medias[position]
        holder.display(currentMedia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_gallery_model, parent, false)
        return MyViewHolder(view)
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var currentMedia: MediaContent? = null

        init {

            /**
             * TODO
             */
        }

        fun display(media: MediaContent) {
            currentMedia=media
            /**
             * TODO
             */

        }

    }
}