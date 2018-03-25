package com.upmc.htag.activities

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import com.upmc.htag.R
import kotlinx.android.synthetic.main.fragment_home_main.view.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection

/**
 * Created by cb_mac on 06/03/2018.
 */

/**
 * A placeholder fragment containing a simple view.
 */


class HomeFragment : Fragment() {
    private val TAG : String = HomeFragment.javaClass.simpleName
    /**
     * API Request parameters
     */
    lateinit var azureVisionUrl: String
    lateinit var azureVisionKey: String
    var backCode: Int = 0
    var response: String = ""
    val CONNECTION_TIMEOUT_MILLISECONDS: Int = 8000
    val LINE_FEED: String = "\r\n"
    var outputStream: OutputStream? = null
    var writer: PrintWriter? = null
    val charset: String = "UTF-8"
    val BUFFER_SIZE: Int = 4096


    /**
     *
     */
    lateinit var apiButton: Button
    lateinit var imageChoosed: ImageView
    lateinit var progressBar: ProgressBar

    companion object {
        var CURRENT_IMAGE_CHOOSEN_URI: String = ""
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_main, container, false)
        //rootView.section_label.text = getString(R.string.section_format)
        Log.e("hjkjhj", rootView.info_text_home.text.toString())

        /*component view init*/
        apiButton = rootView.findViewById(R.id.api_button_caller)
        imageChoosed = rootView.findViewById(R.id.image_chosed)
        progressBar = rootView.findViewById(R.id.progress_bar)

        azureVisionUrl=getString(R.string.azure_api_endpoint)
        azureVisionKey=getString(R.string.azure_vision_api_key)


        apiButton.setOnClickListener {
            //TODO handle api request
            ApiRequestHandler().execute()
        }

        return rootView

    }

    inner class ApiRequestHandler : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE

        }

        /**
         * @param url
         * @param imagePath
         */
        override fun doInBackground(vararg params: String?): String {

            try {
                val boundary = "===" + System.currentTimeMillis() + "==="
                val url = URL(azureVisionUrl)
                val connection = url.openConnection() as HttpsURLConnection

                connection.readTimeout = CONNECTION_TIMEOUT_MILLISECONDS
                connection.connectTimeout = CONNECTION_TIMEOUT_MILLISECONDS
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.doInput = true
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary)
                connection.setRequestProperty("Ocp-Apim-Subscription-Key", azureVisionKey)
                connection.setRequestProperty("Content-Encoding", "identity")
                connection.setRequestProperty("Connection", "Keep-Alive")
                connection.setUseCaches(false)


                outputStream = connection.outputStream
                writer = PrintWriter(OutputStreamWriter(outputStream, charset), true)
                /**************/
                val file = File(CURRENT_IMAGE_CHOOSEN_URI)
                addFilePart(file)
                Log.e("URL",  connection.content.toString())
                /**************/
                backCode = connection.responseCode
                connection.disconnect()

                if (backCode == HttpURLConnection.HTTP_OK) {
                    val tempStream: InputStream = connection.inputStream

                    if (tempStream != null) {
                        response = convertToString(tempStream)
                        Log.e("OKKKKKK",  response)

                    }
                }
                Log.e("ERROR", "Error in network " + backCode)


            } catch (e: Exception) {
                Log.e("", "Error in doInBackGroung" + e.message)
            }
            return response
        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            progressBar.visibility=View.GONE
            //Log.e("Serveur response", result)
        }


        private fun convertToString(inputStream: InputStream): String {
            var result= ""
            val isReader = InputStreamReader(inputStream)
            var bReader = BufferedReader(isReader)
            var tempStr: String

            try {

                do  {
                    tempStr = bReader.readLine()
                    if (tempStr == null) {
                        break
                    }
                    result += tempStr
                } while (true)
                isReader.close()
            } catch (Ex: Exception) {
                Log.e("BAD!!!", "Error in ConvertToString " + Ex.printStackTrace())
            }
            isReader.close()
            return result
        }


        /**
         * adds image file to the request
         */
        @Throws(IOException::class)
        private fun addFilePart(uploadFile: File) {
            val fileName: String = uploadFile.name
        Log.e("filename : ",URLConnection.guessContentTypeFromName(fileName))

            writer?.append(
                    "Content-Disposition: form-data ; filename=\"" + fileName + "\"")
                    ?.append(LINE_FEED)


            writer?.append(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
                    ?.append(LINE_FEED)


            writer?.append("Content-Transfer-Encoding: binary")?.append(LINE_FEED)
            writer?.append(LINE_FEED)
            writer?.flush()

            val inputStream = FileInputStream(uploadFile)
            val buffer = ByteArray(BUFFER_SIZE)
            var bytesRead = -1

            do {
                bytesRead = inputStream.read(buffer)
                if (bytesRead == -1)
                    break
                outputStream?.write(buffer, 0, bytesRead)
            } while (true)

            outputStream?.flush()
            inputStream?.close()

            writer?.append(LINE_FEED)
            writer?.flush()

        }


    }

}