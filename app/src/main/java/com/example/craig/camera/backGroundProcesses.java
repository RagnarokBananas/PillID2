package com.example.craig.camera;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.example.craig.camera.Camera.HTTP_TRANSPORT;
import static com.example.craig.camera.Camera.JSON_FACTORY;

public class backGroundProcesses extends IntentService {
    private static final String API_KEY = "Fa3GonwiTw3RwyIWgNhwVQ";
    private static final String TAG = backGroundProcesses.class.getName();
    public backGroundProcesses() {
        super("MyThread");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        CSPostResult postResult;
        CSGetResult scoredResult = null;
        CSApi api = new CSApi(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                API_KEY
        );
        String address = intent.getStringExtra("uri");
        File savedImage = new File(address);

        if(!savedImage.exists()){
            Log.e("file","no file at that path");
        }

        CSPostConfig savedImageToPost = CSPostConfig.newBuilder()
                .withImage(savedImage)
                .build();

        try {
            postResult = api.postImage(savedImageToPost);
            Thread.sleep(30000);
            scoredResult = api.getImage(postResult);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String stringToParse = scoredResult.getName();
        String parseStart = "name\":\"";
        String parseEnd = "\"}";
        int imprintIndexStart = stringToParse.indexOf(parseStart);
        int imprintIndexEnd = stringToParse.indexOf(parseEnd);
        String parsedName = stringToParse.substring(imprintIndexStart, imprintIndexEnd);

        Toast.makeText(this, parsedName, Toast.LENGTH_SHORT).show();
    }
}
