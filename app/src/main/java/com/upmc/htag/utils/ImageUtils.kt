package com.upmc.htag.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log

/**
 * Created by cb_mac on 21/03/2018.
 */
class ImageUtils {

    companion object {
        val DEBUG :String = "ImageUtils"
        private  fun getPathDeprecated(ctx: Context, uri: Uri?): String {
            if (uri == null) {
                return ""
            }
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = ctx.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                val column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                return cursor.getString(column_index)
            }
            return uri.path
        }

        fun getSmartFilePath(ctx: Context, uri: Uri): String {

            if (Build.VERSION.SDK_INT < 19) {
                return getPathDeprecated(ctx, uri)
            }
            return getPath(ctx, uri)
        }

        @SuppressLint("NewApi")
       private  fun getPath(context: Context, uri: Uri): String {
            val isLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            // DocumentProvider
            if (isLollipop && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                    }
                    /*
                    else if ("video/audio/..." == type) {
                        contentUri = MediaStore.{Video/Audio/..}.Media.EXTERNAL_CONTENT_URI
                    }
                    */

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return contentUri.let { getDataColumn(context, it, selection, selectionArgs) }
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }// File

            return ""
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         * @param context The context.
         * *
         * @param uri The Uri to query.
         * *
         * @param selection (Optional) Filter used in the query.
         * *
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * *
         * @return The value of the _data column, which is typically a file path.
         */
        private fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                          selectionArgs: Array<String>?): String {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                if (cursor != null)
                    cursor.close()
            }
            return ""
        }


        /**
         * @param uri The Uri to check.
         * *
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * *
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * *
         * @return Whether the Uri authority is MediaProvider.
         */
        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

    }

}