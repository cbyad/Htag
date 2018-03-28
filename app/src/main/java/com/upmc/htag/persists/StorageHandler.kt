package com.upmc.htag.persists

import android.content.Context
import java.io.File
import java.io.InputStream

object StorageHandler {
    val filename = "htag.json"
    var allTagsStored: ArrayList<Data> = arrayListOf()

    fun writeInternalFileConfig(fileContents: String, ctx: Context) {
        ctx.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }

    fun readInternalFileConfig(ctx: Context): String {

        val eof = System.getProperty("lines.separator")
        val inputStream: InputStream = File(ctx.getFilesDir(), filename).inputStream()
        return inputStream.bufferedReader().use { it.readText() }
    }

}