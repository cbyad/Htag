package com.upmc.htag.activities

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import com.upmc.htag.R
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.util.Log
import kotlinx.android.synthetic.main.fragment_home_main.*
import java.io.ByteArrayOutputStream
import java.io.IOException

import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.upmc.htag.persists.Data
import com.upmc.htag.persists.StorageHandler
import com.upmc.htag.utils.ImageUtils
import com.upmc.htag.views.HtagSnackbar

class MainActivity : AppCompatActivity() {
    /**
     *
     */
    val PICK_PHOTO = 1
    val INTENT_TYPE = "image/*"


    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter


        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        // gallery_thumb to pick content from file system
        gallery_fab.setOnClickListener { launchImagePicker() }

        /*
        // photo_fab to capture an image
        photo_fab.setOnClickListener { launchCameraPicker() }
        */

        // put in a async task after

        /**
         * Open config file (htag.json) and load into memory (allTagsStored)
         * if this app is launched the first time then create config file
         */

        if (StorageHandler.isfileExist(applicationContext)) {
            HtagSnackbar.make(this, container, StorageHandler.filename + " is loaded", Snackbar.LENGTH_SHORT)
                    .show()
            val fileContents = StorageHandler.readInternalFileConfig(applicationContext)
            if (!fileContents.isEmpty()) {
                var list = StorageHandler.parseJSONConfigFile(fileContents)
                StorageHandler.allTagsStored.addAll(list)

                Log.e("HTAG", " " + fileContents)
            }
        } else {
            val updateData = StorageHandler.begin + Gson().toJson(StorageHandler.allTagsStored) + StorageHandler.end
            StorageHandler.writeInternalFileConfig(updateData, this)
            HtagSnackbar.make(this, container, StorageHandler.filename + " is created",
                    Snackbar.LENGTH_SHORT)
                    .show()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        HomeFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        /**before exit program save all new data into htag.json*/
        val removeDuplicate = StorageHandler.allTagsStored.distinctBy { listOf(it.confidence, it.name, it.path) }
        val updateData = StorageHandler.begin + Gson().toJson(removeDuplicate) + StorageHandler.end
        StorageHandler.writeInternalFileConfig(updateData, this)
    }

    fun launchCameraPicker() {
        //TODO camera handler
    }

    fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = INTENT_TYPE
        startActivityForResult(Intent.createChooser(intent, "...Select an image"), PICK_PHOTO)
    }

    /**
     * compress image before and display it --->
     */
    fun showImage(data: Intent) {
        info_text_home.visibility = View.INVISIBLE // hide text

        val contentUri = data.data
        if (contentUri == null)
            Log.e("erro", "toto")
        try {

            HomeFragment.CURRENT_IMAGE_CHOOSEN_URI = ImageUtils.getSmartFilePath(this, contentUri)

            Glide.with(this)
                    .load(HomeFragment.CURRENT_IMAGE_CHOOSEN_URI)
                    .into(image_chosed)

            image_chosed.visibility = View.VISIBLE // show image view
            HtagSnackbar.make(this, container, HomeFragment.CURRENT_IMAGE_CHOOSEN_URI, Snackbar.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.stackTrace
        }
        //
        api_button_caller.visibility = View.VISIBLE // show button_check too
        HomeFragment.tagList.clear()
        tag_list_view.adapter.notifyDataSetChanged()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            showImage(data)
        } else {
            //Display error and exit
            return
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {

            if (position == 0)
                return HomeFragment()
            return GalleryFragment()
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }

}
