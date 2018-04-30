package com.example.craig.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    private ImageView mImageView;
    static final int REQUEST_TAKE_PHOTO = 10;
    private Uri mUri;
    private Intent mIntent;

    private static final String API_KEY = "Fa3GonwiTw3RwyIWgNhwVQ";
    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        Uri aUri = null;
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
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StartService(mImageView);
    }

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
    public void StartService (View view){
        Intent intent= new Intent(Camera.this, backGroundProcesses.class);
        String cut = mUri.toString();
        int index = cut.indexOf("es/")+3;
        String name = cut.substring(index);
        //storage/emulated/0/Android/data/com.example.craig.camera/files/Pictures/
        String uri = ("/storage/emulated/0/Android/data/com.example.craig.camera/files/Pictures/"+name);
        intent.putExtra("uri",uri);
        startService(intent);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
