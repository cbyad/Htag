package com.upmc.htag.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.upmc.htag.R
import com.upmc.htag.adapters.GalleryAdapter

/**
 * Created by cb_mac on 06/03/2018.
 */

/**
 * A placeholder fragment containing a simple view.
 */
class GalleryFragment : Fragment() {
    lateinit var listView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_gallery_model, container, false)

        listView = rootView.findViewById(R.id.gallery_list_view)
        listView.setLayoutManager( LinearLayoutManager(rootView.context));
        //listView.setAdapter(GalleryAdapter(taskLists,this))


        return rootView
    }

    /*
    companion object {

        /**
         * Returns a new instance
         */
        fun newInstance(): GalleryFragment {
            val fragment = GalleryFragment()
            //val args = Bundle()
            //args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            //fragment.arguments = args
            return fragment
        }
    }
    */
}