package com.example.craig.camera;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.craig.camera.Camera.HTTP_TRANSPORT;
import static com.example.craig.camera.Camera.JSON_FACTORY;

public class backGroundProcesses extends IntentService {
    private static final String API_KEY = "Fa3GonwiTw3RwyIWgNhwVQ";
    public backGroundProcesses() {
        super("MyThread");
    }
    //the method that get called at the start of the new thread
    @Override
    protected void onHandleIntent(Intent intent){
        //checks which code has been sent
        int code = intent.getIntExtra("code",1);
        if (code==1) {
            csPost(intent);
        }
    }
    //sends the picture to CloudSight and waits for a response
    public void csPost(Intent intent){
        //creates a receiver that allows information to be sent to the main thread
        ResultReceiver receiver = intent.getParcelableExtra("receiver1");
        //gets the file address from the input intent
        String address = intent.getStringExtra("uri");
        //create a few things needed for the image post
        //they needed to be called somewhere outside of the try/catch
        CSPostResult postResult;
        CSGetResult scoredResult = null;
        //the thing that creates the post request
        CSApi api = new CSApi(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                API_KEY
        );
        //looks for the file at the input address
        File savedImage = new File(address);
        //logs whether or no the file is there
        if (!savedImage.exists()) {
            Log.e("file", "no file at that path");
        }
        //configures the post
        CSPostConfig savedImageToPost = CSPostConfig.newBuilder()
                .withImage(savedImage)
                .build();
        //trys to post and get the result from the API
        try {
            postResult = api.postImage(savedImageToPost);
            Thread.sleep(30000);
            scoredResult = api.getImage(postResult);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //the receiver that we created earlier requires a bundle to be passed back
        //so we send the response from the API back in a bundle
        Bundle bundle = new Bundle();
        bundle.putString("message", scoredResult.getName());
        receiver.send(1, bundle);
    }
}
