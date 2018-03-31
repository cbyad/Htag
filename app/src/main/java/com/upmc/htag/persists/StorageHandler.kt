package com.upmc.htag.persists

import android.content.Context
import android.util.Log
import com.upmc.htag.models.Tag
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.InputStream

object StorageHandler {
    val filename = "htag.json"
    val begin = "{\n" +
            "  \"tags\":"

    val end = "}"

    var allTagsStored: ArrayList<Data> = arrayListOf()

    fun writeInternalFileConfig(fileContents: String, ctx: Context) {
        ctx.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())

        }
    }

    fun isfileExist(ctx: Context): Boolean{
        val file = ctx.getFileStreamPath(filename)
        file.delete()
        return file.exists()
    }




    fun readInternalFileConfig(ctx: Context): String {
            //val eof = System.getProperty("lines.separator")
            val inputStream: InputStream = File(ctx.getFilesDir(), filename).inputStream()
            return inputStream.bufferedReader().use { it.readText() }

    }


    fun parseJSONConfigFile(jsonContent: String): java.util.ArrayList<Data> {
        val assocList: java.util.ArrayList<Data> = arrayListOf()
        try {
            val reader = JSONObject(jsonContent)
            val tagsArray: JSONArray = reader.getJSONArray("tags")
            //Log.e(TAG," tableau size"+ tasArray.length())
            // iterate throught the content
            /* equivalent to i in 0 */
            for (i in 0 until tagsArray.length()) {
                val currentDataObject: JSONObject = tagsArray.getJSONObject(i)

                val name: String = currentDataObject.optString("name")
                val confidence: Double = currentDataObject.optDouble("confidence")
                val src: String = currentDataObject.optString("path")
                //then add them to our list
                val elt = Data(name, confidence, src)
                assocList.add(elt)
            }
        } catch (e: JSONException) {
            Log.e("JSON error", "Error while parsing JSON" + e.message)
        }

        return assocList
    }

    fun getImagesPathsWithTags(tag: String): ArrayList<String> {
        var paths = arrayListOf<String>()
        StorageHandler.allTagsStored.forEach { elt ->
            if (elt.name == tag) {
                paths.add(elt.path)
            }
        }
        return paths
    }

}