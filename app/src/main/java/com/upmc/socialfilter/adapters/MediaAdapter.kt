package com.upmc.socialfilter.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.upmc.socialfilter.R
import com.upmc.socialfilter.models.Media

/**
 * Created by cb_mac on 22/02/2018.
 */

/**
 * Class to display content like image,video in listview
 */
class MediaAdapter(private val medias: List<Media>, private val context: Context) :
        RecyclerView.Adapter<MediaAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return medias.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMedia = medias[position]
        holder.display(currentMedia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.media_model, parent, false)
        return MyViewHolder(view)
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var currentMedia: Media? = null

        init {

            /**
             * TODO
             */
        }

        fun display(media: Media) {
            currentMedia=media
            /**
             * TODO
             */

        }

    }
}