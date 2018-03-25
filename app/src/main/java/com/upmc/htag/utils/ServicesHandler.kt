package com.upmc.htag.utils

import android.os.AsyncTask
import android.util.Log
import com.upmc.htag.activities.HomeFragment
import com.upmc.htag.models.Tag
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder


/**
 * Created by cb_mac on 21/03/2018.
 */

/*
Supported image formats: JPEG, PNG, GIF, BMP.
Image file size must be less than 4MB.
Image dimensions should be greater than 50 x 50
*/
class SercvicesHandler {

    companion object {

        /**
         * collect (tag,confidence) from json response and put them in a MutableList
         */
        fun parseJsonResponseAPI(jsonContent: String): MutableList<Tag> {
            val assocList: MutableList<Tag> = mutableListOf()

            try {

                val reader = JSONObject(jsonContent)
                val tagsArray: JSONArray = reader.getJSONArray("tags")

                // iterate throught the content
                for (i in 1..tagsArray.length()) {
                    val currentTagObject: JSONObject = tagsArray.getJSONObject(i)

                    val name: String = currentTagObject.getString("name")
                    val confidence: Double = currentTagObject.getDouble("confidence")
                    //then add them to our list
                    val elt = Tag(name,confidence)
                    assocList.add(elt)
                }

            } catch (e : JSONException){
                Log.e("JSON error","Error while parsing JSON"+ e.message)
            }

            return assocList
        }
    }
}