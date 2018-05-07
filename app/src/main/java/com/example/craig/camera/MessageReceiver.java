package com.example.craig.camera;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

//required class for sending messages between threads
@SuppressLint("ParcelCreator")
public class MessageReceiver extends ResultReceiver{
    private Camera.Message message;
    public MessageReceiver(Camera.Message message){
        super(new Handler());
        this.message = message;
    }
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData){
        message.displayMessage(resultCode,resultData);
    }
}