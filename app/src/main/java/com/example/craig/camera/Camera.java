package com.example.craig.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    private ImageView mImageView;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int CAMERA_REQUEST = 1888;
    private Intent TPI;
    //cloudsight declarations
    private static final String API_KEY = "Fa3GonwiTw3RwyIWgNhwVQ";
    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
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
        TPI=takePictureIntent;
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
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.craig.camera",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
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
                //Creates the base class

                CSApi CS = new CSApi(
                        HTTP_TRANSPORT,
                        JSON_FACTORY,
                        API_KEY
                );

                //The Format to be used if the image being posted has a link that the API can reference

                CSPostConfig imageToPost = CSPostConfig.newBuilder()
                        .withRemoteImageUrl("http://www.greycloaktech.com/wp-content/uploads/2015/07/url-small.jpg")
                        .build();

                //finds the newest image in the folder to send to Cloudsight

                File fl = new File("Android/data/com.example.craig.camera/files/Pictures");
                File[] files = fl.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isFile();
                    }
                });
                long lastMod = Long.MIN_VALUE;
                File savedImage = null;
                for (File file : files) {
                    if (file.lastModified() > lastMod) {
                        savedImage = file;
                        lastMod = file.lastModified();
                    }
                }
                //The format used to build the Object that uploads a saved image to CloudS

                CSPostConfig savedImageToPost = CSPostConfig.newBuilder()
                        .withImage(savedImage)
                        .build();

                //Receives the response of the API that says if the image was received
                //in this case we will see what the api sees in this saved image

                try {
                    CSPostResult portResult = CS.postImage(savedImageToPost);
                }catch(IOException ex){}

                //System.out.println("Post result: " + portResult);
                //used to print the response from the API^

            }
        });
    }
    private void galleryAddPic () {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            data = TPI;
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageView.setImageBitmap(imageBitmap);
            }
        }

}
