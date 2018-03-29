package com.upmc.htag.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.upmc.htag.R
import com.upmc.htag.adapters.GalleryAdapter
import com.upmc.htag.models.MediaContent
import com.upmc.htag.models.Tag
import com.upmc.htag.persists.StorageHandler
import java.io.OutputStream
import java.io.PrintWriter

/**
 * Created by cb_mac on 06/03/2018.
 */

/**
 * A placeholder fragment containing a simple view.
 */
class GalleryFragment : Fragment() {
    private val TAG: String = GalleryFragment.javaClass.simpleName

    /**
     * components views
     */
    lateinit var galleryListView: RecyclerView

    /**
     * others
     */
    val NUMBER_OF_COLUMNS = 2

    companion object {
        var imageList: ArrayList<MediaContent> = arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_gallery_model, container, false)

        galleryListView = rootView.findViewById(R.id.gallery_list_view)
        galleryListView.layoutManager = GridLayoutManager(rootView.context, NUMBER_OF_COLUMNS)

        galleryListView.adapter = GalleryAdapter(imageList, rootView.context)

        imageList.clear()
        for (i in 0 until StorageHandler.allTagsStored.size) {
            val name = StorageHandler.allTagsStored[i].name
            val src = StorageHandler.allTagsStored[i].path
            imageList.add(MediaContent(src, name))
        }
        galleryListView.adapter.notifyDataSetChanged()
        return rootView
    }
}