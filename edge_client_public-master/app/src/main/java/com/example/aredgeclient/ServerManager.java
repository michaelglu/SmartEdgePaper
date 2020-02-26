package com.example.aredgeclient;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
/*
* Contains methods for performing GET requests to the server
*/
public class ServerManager {
    public static final String SERVER_ENDPOINT = "YOUR_SERVER_ENDPOINT";
    public static final String GET_ALL_ROUTE="/getAllImage";
    public static final String GET_MODEL_PATH="/getRenderable";
    private JsonHttpResponseHandler httpResponseHandler;

    public ServerManager(JsonHttpResponseHandler handler){
        httpResponseHandler=handler;
    }

    public void getImages(){
        AsyncHttpClient client= new AsyncHttpClient();
        client.addHeader("clientId","YOUR_CLIENT_ID");
        Log.d("DATA DOWNLOAD STARTED","Time: "+System.nanoTime());
        client.get(SERVER_ENDPOINT+GET_ALL_ROUTE,httpResponseHandler);
    }
    public void getModel(String id){
        AsyncHttpClient client= new AsyncHttpClient();
        client.addHeader("renderableId",id);
        client.get(SERVER_ENDPOINT+GET_MODEL_PATH,httpResponseHandler);

    }
}
