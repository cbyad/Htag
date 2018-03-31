package com.upmc.htag.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*


import com.upmc.htag.R
import com.upmc.htag.adapters.GalleryAdapter
import com.upmc.htag.models.MediaContent
import com.upmc.htag.persists.StorageHandler


/**
 * Created by cb_mac on 06/03/2018.
 */

/**
 * A placeholder fragment containing a simple view.
 */
class GalleryFragment : Fragment(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private val TAG: String = GalleryFragment.javaClass.simpleName

    /**
     * components views
     */

    /**
     * others
     */
    val NUMBER_OF_COLUMNS = 2

    companion object {
        var imageList: ArrayList<MediaContent> = arrayListOf()
        var tagList: ArrayList<String> = arrayListOf()
        lateinit var galleryListView: RecyclerView

        fun populateImageAndTagList() {
            imageList.clear()
            StorageHandler.allTagsStored.
                    distinctBy { listOf(it.confidence,it.name,it.path) }
                    .forEach { elt-> imageList.add(MediaContent(elt.path,elt.name)) }
            StorageHandler.allTagsStored.forEach { it -> tagList.add(it.name) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        populateImageAndTagList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_gallery_model, container, false)

        galleryListView = rootView.findViewById(R.id.gallery_list_view)
        galleryListView.layoutManager = GridLayoutManager(rootView.context, NUMBER_OF_COLUMNS)

        galleryListView.adapter = GalleryAdapter(imageList, rootView.context)
        return rootView
    }

    fun resetSearch() {
        populateImageAndTagList()
        galleryListView.adapter.notifyDataSetChanged()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = getString(R.string.auto_complete_text)

        super.onCreateOptionsMenu(menu, inflater)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.e(TAG, "Query text change !")
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch()
            return false
        }

        var filteredValues = arrayListOf<String>()
        tagList.forEach { it -> filteredValues.add(it) } // copy
        tagList.forEach { it ->
            if (!it.toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(it) // remove don't match pattern
            }
        }

        if (filteredValues.isNotEmpty()) { // contain just 1 element
            var newImageList = arrayListOf<MediaContent>()

            imageList.filter { it-> filteredValues[0] == it.title }
                    .forEach { elt-> newImageList.add(MediaContent(elt.src,elt.title)) }
            imageList.clear()
            imageList.addAll(newImageList)
            galleryListView.adapter.notifyDataSetChanged()
        }

        return false
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        return true
    }
}