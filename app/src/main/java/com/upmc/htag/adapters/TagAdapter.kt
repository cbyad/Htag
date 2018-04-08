package com.upmc.htag.adapters

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.upmc.htag.R
import com.upmc.htag.models.Tag
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import java.time.Duration
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.ScaleAnimation

/**
 * Created by cb_mac on 25/03/2018.
 */

/**
 * Class to display tag from api response
 */
class TagAdapter(val tags: MutableList<Tag>, val context: Context?) :
        RecyclerView.Adapter<TagAdapter.MyViewHolder>() {



    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTag = tags[position]
        holder.display(currentTag)
        holder.setFadeAnimation(holder.tagName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tag_model, parent, false)
        return MyViewHolder(view)
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val FADE_DURATION : Long = 1000 //FADE_DURATION in milliseconds
        private var currentTag: Tag? = null
         var isClicked = false
        var tagName : TextView

        init {
            tagName= view.findViewById(R.id.tag_name)
        }

        fun display(tag: Tag) {
            currentTag=tag
            tagName.text= tag.name

        }


         fun setFadeAnimation(view: View) {
             val anim = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
             anim.duration = FADE_DURATION
             view.startAnimation(anim)
        }
    }
}