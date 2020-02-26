package com.example.aredgeclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;
/*
* First Activity, responsible for loading the image-model map
*/
public class MainActivity extends AppCompatActivity {

    private AssetManager assetManager;
    private ServerManager serverManager;
    private JsonHttpResponseHandler responseHandler;
    private DownloadListener downloadListener,fileLoaderListener;
    private int downloadSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent= new Intent(this, ArActivity.class);
        downloadSize=0;
        downloadListener=new DownloadListener() {
            @Override
            public void onDownloadCompleted() {
                //LAUNCH A NEW ACTIVITY
                Log.d("Download Listener","IMAGE LOADED");
                try{Thread.sleep(1000);} catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(assetManager.getImageMapSize()==downloadSize&&assetManager.getRenderableMapSize()==downloadSize){
                    Log.d("DATA DOWNLOAD ENDED","Time: "+System.nanoTime());
                    Log.d("Download Listener","ALL ASSETS LOADED");

                    ImageStorage.setImageMap(assetManager.getImageMap());
                    intent.putExtra("renderableIdMap",assetManager.getRenderbleIdMap());
                    intent.putExtra("renderablePathMap",assetManager.getRenderablePathMap());
                    startActivity(intent);


                }
            }
        };
        fileLoaderListener=new DownloadListener() {
            @Override
            public void onDownloadCompleted() {
               // IF you want to record model load times comment out the line below
               serverManager.getImages();
            }
        };


        assetManager = new AssetManager(downloadListener);
        responseHandler=setUpResponseHandler(this);
        serverManager= new ServerManager(responseHandler);
        Button button= findViewById(R.id.downloadButton);
          button.setOnClickListener((View view)->{
          //comment out the line below if don't want to load model to file system
          new FileLoader(MainActivity.this,fileLoaderListener).execute("YOUR_RENDERABLE_URL");
          //uncomment line below if don't want to load load to file system
          // serverManager.getImages();
    });
    }


//  Sets up response handlers needed by ServerManager
    public JsonHttpResponseHandler setUpResponseHandler(Context context){
        JsonHttpResponseHandler handler= new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d("SERVER","Success:"+response.toString());
                try{
                    assetManager.addRenderable(response.getString("id"),response.getString("filePath"));
                } catch (JSONException e) {
                    Log.e("JSON","ERROR PARSING JSON BODY: "+e.getMessage());
                }

            }
            @Override
            public void onSuccess(int statusCode,Header[]headers,JSONArray array){
                Log.d("SERVER","Success:"+array.toString());
                try{
                    downloadSize=array.length();
                    for(int i=0;i<array.length();i+=1){
                        JSONObject image= array.getJSONObject(i);
                        assetManager.addImage(image.getString("_id"),image.getString("filePath"),image.getString("renderableId"),context);
                        serverManager.getModel(image.getString("renderableId"));
                    }

                }catch (JSONException e){}


            }
            @Override
            public void onFailure(int code, Header []headers,String message,Throwable throwable){
                Log.d("SERVER","ERRPR: "+message+"\n"+throwable.getMessage());
            }
        };
        return  handler;
    }

    public interface DownloadListener{
        public void onDownloadCompleted();
    }
}
