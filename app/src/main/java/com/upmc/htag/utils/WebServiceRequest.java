package com.upmc.htag.utils;

/**
 * Created by cb_mac on 27/03/2018.
 */


import android.util.Log;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * upload image to api & handle server response
 * the only java code
 */
public class WebServiceRequest {
    private static final String headerKey = "ocp-apim-subscription-key";
    private HttpClient client = new DefaultHttpClient();
    private String subscriptionKey ;

    public WebServiceRequest(String key) {
        this.subscriptionKey = key;
    }

    public String callAzureApi (String url ,String contentType, InputStream stream) throws Exception{
        byte[] data = IOUtils.toByteArray(stream);
        Map<String, Object> params = new HashMap<>();
        params.put("data", data);
        return webInvoke(url,params,contentType);
    }

    private String webInvoke(String url, Map<String, Object> data, String contentType) throws Exception {

        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", contentType);
        request.setHeader(headerKey, this.subscriptionKey);
        request.setEntity(new ByteArrayEntity((byte[]) data.get("data")));

        HttpResponse response = this.client.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        Log.e("OKKKKKK"," "+statusCode);
        if (statusCode == HttpURLConnection.HTTP_OK) {
            return readInput(response.getEntity().getContent());

        } else if (statusCode == HttpURLConnection.HTTP_ACCEPTED) {
            return response.getFirstHeader("Operation-Location").getValue();
        }
        else if (statusCode== HttpURLConnection.HTTP_UNSUPPORTED_TYPE){
            return "Unsupported media type";
        }
        else {
            throw new Exception("Error executing POST request! Received error code: " +
                    response.getStatusLine().getStatusCode());
        }
}

    private String readInput(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer json = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            json.append(line);
        }
        return json.toString();
    }
}