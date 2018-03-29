package com.upmc.htag.activities

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.ProgressBar
import com.google.gson.Gson
import com.upmc.htag.R
import com.upmc.htag.adapters.TagAdapter
import com.upmc.htag.models.Tag
import com.upmc.htag.persists.Data
import com.upmc.htag.persists.SecretValues
import com.upmc.htag.persists.StorageHandler
import com.upmc.htag.utils.JSONHandler
import kotlinx.android.synthetic.main.fragment_home_main.view.*
import java.io.*
import com.upmc.htag.utils.WebServiceRequest
import com.upmc.htag.views.HtagSnackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


/**
 * Created by cb_mac on 06/03/2018.
 */

/**
 * A placeholder fragment containing a simple view.
 */

class HomeFragment : Fragment() {
    private val TAG: String = HomeFragment.javaClass.simpleName

    /**
     * API Request parameters
     */
    var backCode: Int = 0
    var response: String = ""
    val CONNECTION_TIMEOUT_MILLISECONDS: Int = 8000
    val LINE_FEED: String = "\r\n"
    var outputStream: OutputStream? = null
    var writer: PrintWriter? = null
    val charset: String = "UTF-8"


    /**
     * components views
     */
    lateinit var apiButton: Button
    lateinit var imageChoosed: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var tagListView: RecyclerView

    /**
     * others
     */
    val NUMBER_OF_COLUMNS = 4

    companion object {
        var tagList: ArrayList<Tag> = arrayListOf()
        var CURRENT_IMAGE_CHOOSEN_URI: String = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_main, container, false)
        Log.e(TAG, rootView.info_text_home.text.toString())

        /*component view init*/
        apiButton = rootView.findViewById(R.id.api_button_caller)
        imageChoosed = rootView.findViewById(R.id.image_chosed)
        progressBar = rootView.findViewById(R.id.progress_bar)
        tagListView = rootView.findViewById(R.id.tag_list_view) as RecyclerView

        tagListView.adapter = TagAdapter(tagList, rootView.context)
        tagListView.layoutManager = GridLayoutManager(rootView.context, NUMBER_OF_COLUMNS)




        apiButton.setOnClickListener {
            tagListView.visibility = View.VISIBLE
            ApiRequestHandler().execute()
            it.visibility = View.GONE
        }
        return rootView
    }

    inner class ApiRequestHandler : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String): String {

            val inputStream = FileInputStream(CURRENT_IMAGE_CHOOSEN_URI)
            var webServiceInstance = WebServiceRequest(SecretValues.azure_vision_api_key)
            return webServiceInstance.callAzureApi(SecretValues.azure_api_endpoint,
                    "application/octet-stream", inputStream)
        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            tagListView.recycledViewPool.clear() // this is very useful
            tagList.clear()
            tagList.addAll(JSONHandler.parseJSONResponseAPI(result))
            tagListView.adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
            val tagsMsg = ""+ tagList.size + " tag(s) found"
            HtagSnackbar.make(context,tagListView.rootView ,tagsMsg, Snackbar.LENGTH_LONG).show()
            /*
            *  Save all in file
            */

            /*
            tagList.forEach{elt-> StorageHandler.allTagsStored.add(Data(elt.name,elt.confidence, CURRENT_IMAGE_CHOOSEN_URI))
            }

            val g = Gson()
            val str = StorageHandler.begin+g.toJson(StorageHandler.allTagsStored)+StorageHandler.end
            StorageHandler.writeInternalFileConfig(str,context)
        */

        }

    }

}