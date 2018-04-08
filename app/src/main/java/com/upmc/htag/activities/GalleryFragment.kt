package com.upmc.htag.activities

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
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

class GalleryFragment : Fragment(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private val TAG: String = GalleryFragment.javaClass.simpleName
    private val grids = arrayOf<String>("2", "4", "6")

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
            StorageHandler.allTagsStored.distinctBy { listOf(it.confidence, it.name, it.path) }
                    .forEach { elt -> imageList.add(MediaContent(elt.path, elt.name)); tagList.add(elt.name) }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        when (id) {
            R.id.action_settings -> {
                var builder: AlertDialog.Builder = AlertDialog.Builder(context)

                builder.setTitle("Number of grids")
                        .setCancelable(true)
                        .setItems(grids) { dialog, which ->
                            // the user clicked on grids[which]
                            val number: Int = (grids[which]).toInt()


                            galleryListView.layoutManager = GridLayoutManager(context, number)
                        }.show()
                return true
            }

            R.id.action_delete -> {
                if (StorageHandler.allTagsStored.isEmpty()) // nothing to delete don't show alert !
                    return true

                var builder: AlertDialog.Builder = AlertDialog.Builder(context)

                builder.setMessage("Are you sure ?").setCancelable(true)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete") { dialog, which ->
                            StorageHandler.reset(context)
                            StorageHandler.allTagsStored.clear()
                            imageList.clear()
                            galleryListView.adapter.notifyDataSetChanged()
                        }.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
        tagList.distinctBy { it }.forEach { it -> filteredValues.add(it) } // remove duplicate and copy
        tagList.forEach { it ->
            if (!it.toLowerCase().startsWith(newText.toLowerCase())) {
                filteredValues.remove(it) // remove don't match pattern
            }
        }

        if (filteredValues.isNotEmpty()) {
            var newImageList = arrayListOf<MediaContent>()

            filteredValues.forEach { it ->
                imageList.filter { t -> it == t.title }.forEach { elt ->
                    newImageList.add(MediaContent(elt.src, elt.title)) }
            }

            imageList.clear()
            imageList.addAll(newImageList)
            galleryListView.adapter.notifyDataSetChanged()
        } else {
            imageList.clear()
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