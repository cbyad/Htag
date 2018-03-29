package com.upmc.htag.utils

import android.util.Log
import com.upmc.htag.models.Tag
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by cb_mac on 25/03/2018.
 */

/*
Supported image formats: JPEG, PNG, GIF, BMP.
Image file size must be less than 4MB.
Image dimensions should be greater than 50 x 50
*/
object JSONHandler {
    val TAG = this.javaClass.simpleName

    /**
     * collect (tag,confidence) from json response and put them in a MutableList
     */
    fun parseJSONResponseAPI(jsonContent: String): ArrayList<Tag> {
        val assocList: ArrayList<Tag> = arrayListOf()
        try {
            val reader = JSONObject(jsonContent)
            val tagsArray: JSONArray = reader.getJSONArray("tags")
            //Log.e(TAG," tableau size"+ tasArray.length())
            // iterate throught the content
            /* equivalent to i in 0 */
            for (i in 0 until tagsArray.length()) {
                val currentTagObject: JSONObject = tagsArray.getJSONObject(i)

                val name: String = currentTagObject.optString("name")
                val confidence: Double = currentTagObject.optDouble("confidence")
                val hint : String =currentTagObject.optString("hint")
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