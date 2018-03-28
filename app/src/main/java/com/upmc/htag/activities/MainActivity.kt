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
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.util.Log
import kotlinx.android.synthetic.main.fragment_home_main.*
import java.io.ByteArrayOutputStream
import java.io.IOException

import android.widget.LinearLayout
import android.widget.Toast
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
        gallery_fab.setOnClickListener { /*view ->
            //TODO add function to pick content from file system
            Snackbar.make(view, "Cafard vert", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show() */
            launchImagePicker()
        }

        /*
        // photo_fab to capture an image
        photo_fab.setOnClickListener { launchCameraPicker() }
        */

        /*
            Open config file and save in memory in place
         */
        val fileContents = StorageHandler.readInternalFileConfig(applicationContext)
        if (fileContents!=""){
            val g : Gson = Gson()
           var list = g.fromJson(fileContents, arrayListOf<Data>().javaClass)
            StorageHandler.allTagsStored.addAll(list)
        }
    }

    fun launchCameraPicker(){
        //TODO camera handler
    }

    fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = INTENT_TYPE
        startActivityForResult(Intent.createChooser(intent,"...Select an image"), PICK_PHOTO)
    }

    /**
     * compress image before and display it --->
     */
    fun showImage(data: Intent){
        info_text_home.visibility=View.INVISIBLE // hide text
        //resetTextParams()
        val contentUri = data.data
            if (contentUri==null)
                Log.e("erro","toto")
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, contentUri)
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
            image_chosed.visibility=View.VISIBLE // show image view
            image_chosed.setImageBitmap(bitmap)
            //TODO  image compression

            HomeFragment.CURRENT_IMAGE_CHOOSEN_URI=ImageUtils.getSmartFilePath(this,contentUri)

            HtagSnackbar.make(this,container,HomeFragment.CURRENT_IMAGE_CHOOSEN_URI,Snackbar.LENGTH_LONG).show()
        }catch (e: IOException){
            e.stackTrace
        }
        //
        api_button_caller.visibility= View.VISIBLE // show button_check too
        HomeFragment.tagList.clear()
        tag_list_view.adapter.notifyDataSetChanged()
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK && data!=null) {
             showImage(data)
        }
         else {
            //Display error and exit
            return
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when(id) {
            R.id.action_settings -> {return true}

            R.id.action_exit ->{return true}
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {

            if (position==0)
                return HomeFragment()
            return GalleryFragment()
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }
}
