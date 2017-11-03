package com.okdevtv.fileupload;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * original code from
 * http://androidexample.com/Upload_File_To_Server_-_Android_Example/index.php?view=article_discription&aid=83&aaid=106
 * <p>
 * modified to support Android API level 23 or more.
 */
public class UploadActivity extends AppCompatActivity {

    /**
     * *******  File Path ************
     */
    final String uploadFilePath = "/sdcard/Download/";
    final String uploadFileName = "external.txt";

    final int REQUEST_READ_FROM_EXTERNAL_STORAGE = 1;
    TextView messageText;
    Button uploadButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadButton = (Button) findViewById(R.id.uploadButton);
        messageText = (TextView) findViewById(R.id.messageText);

        messageText.setText("Uploading file path :- '/sdcard/Download/" + uploadFileName + "'");


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isExternalStorageReadable())
                    return;     // 외부메모리를 사용하지 못하면 끝냄

                String[] PERMISSIONS_STORAGE = {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };

                if (ContextCompat.checkSelfPermission(UploadActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            UploadActivity.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_READ_FROM_EXTERNAL_STORAGE
                    );
                } else {
                    uploadFile();
                }


            }
        });
    }

    private void uploadFile() {
        String sourceFileUri = uploadFilePath + "" + uploadFileName;

        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            TextView messageText = (TextView) findViewById(R.id.messageText);
            messageText.setText("Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

        } else {
            new UploadFile(UploadActivity.this).execute(uploadFilePath + "" + uploadFileName);
        }
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_READ_FROM_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadFile();
            }
        } else {
            Toast.makeText(getApplicationContext(), "접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
        }
    }

}
