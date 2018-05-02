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

    @Override
    protected void onHandleIntent(Intent intent){
        int code = intent.getIntExtra("code",1);
        if (code==1) {
            csPost(intent);
        }/*else if(code==2){
            wmdPost(intent);
        }*/
    }
    public void csPost(Intent intent){
        ResultReceiver receiver = intent.getParcelableExtra("receiver1");
        String address = intent.getStringExtra("uri");
        CSPostResult postResult;
        CSGetResult scoredResult = null;
        CSApi api = new CSApi(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                API_KEY
        );
        File savedImage = new File(address);

        if (!savedImage.exists()) {
            Log.e("file", "no file at that path");
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
        Bundle bundle = new Bundle();
        bundle.putString("message", scoredResult.getName());
        receiver.send(1, bundle);
    }


    /*public void wmdPost(Intent intent){
        Bundle bundle = new Bundle();
        bundle.putString("placeholder","placeholder");
        List<Integer> numberPlace = new ArrayList<Integer>();
        ResultReceiver receiver = intent.getParcelableExtra("receiver2");
        String foundLetters = intent.getStringExtra("string");
        if (foundLetters == null){
            receiver.send(3,bundle);
        }
        char[] spaceParse = foundLetters.toCharArray();
        for(int x = 0; x<9; x++){
            numberPlace.add(foundLetters.indexOf(x));
        }
        for(int y = 0; y < numberPlace.size(); y++){
            if (numberPlace.get(y)>numberPlace.get(0)){
                numberPlace.set(0,numberPlace.get(y));
            }
        }
        int ctr = numberPlace.get(0);
        while(spaceParse[ctr]!=' '){
            ctr--;
        }
        ctr++;
        foundLetters = foundLetters.substring(ctr,foundLetters.indexOf(" ",ctr));
        String url = "https://www.drugs.com/imprints.php?imprint="+foundLetters+"&color=&shape=0";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements image = doc.select("img[src]");
        String HTML = image.toString();
        int nameHeader = HTML.indexOf("/images/pills/");
        int startIndex = HTML.indexOf("alt=", nameHeader);
        startIndex = startIndex + 5;
        int endIndex = HTML.indexOf(" ", startIndex);
        String pillName = HTML.substring(startIndex, endIndex);

        bundle.putString("message",pillName);
        receiver.send(2,bundle);
    }*/
}
