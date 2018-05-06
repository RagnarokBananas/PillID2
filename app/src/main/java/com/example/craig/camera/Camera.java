package com.example.craig.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.File;
import java.io.IOException;

public class Camera extends AppCompatActivity{
    String mCurrentPhotoPath;
    private ImageView mImageView;
    static final int REQUEST_TAKE_PHOTO = 10;
    private Uri mUri;

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    /**
    *Creates a file with a specific file name at a specific place
    */
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_FILE";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    /**
    *Opens the camera for use and then saves the picture at the designated file
    */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        Uri aUri;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                aUri = FileProvider.getUriForFile(this,
                        "com.example.craig.camera",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, aUri);
                setResult(Activity.RESULT_OK,takePictureIntent);
                finish();
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                mUri = aUri;
            }
        }
        //pauses for the user to take a picture
        //was creating errors when starting the side thread to early without the image being saved
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        csService(mImageView);
    }
    /**
    *First method called
    *calls the layout from the layouts folder and the button
    * sets the buttons to an on click listener that directs to the dispatchTakePictureIntent
    */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        this.mImageView = (ImageView) this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }
    /**
    *called when the picture is done saving
    * opens a new thread because android requires it with API requests
    */
    public void csService (View view){
        Intent csIntent= new Intent(Camera.this, backGroundProcesses.class);
        MessageReceiver csReceiver = new MessageReceiver(new Message());
        String cut = mUri.toString();
        int index = cut.indexOf("es/") + 3;
        String name = cut.substring(index);
        String uri = ("/storage/emulated/0/Android/data/com.example.craig.camera/files/Pictures/" + name);
        csIntent.putExtra("code",1);
        csIntent.putExtra("uri", uri);
        csIntent.putExtra("receiver1", csReceiver);
        startService(csIntent);
    }
    /**
    *Receives the message and then displays it as a toast
    */
    public class Message{
        public void displayMessage(int resultCode, Bundle resultData){
            if (resultCode ==1) {
                String message = resultData.getString("message");
                Toast.makeText(Camera.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}