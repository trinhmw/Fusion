package com.path.fusion.fusion;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends ActionBarActivity {
    private static final int REQUEST_CODE = 4841;
    private static final String TAG = "MainActivity";
    private static String csvPath = null;
    Button mOpenFileButton;
    Button mLoadFileButton;
    Button mClearDataButton;
    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fileManager = FileManager.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOpenFileButton = (Button) findViewById(R.id.openFileButton);


        View.OnClickListener openFileOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showChooser();
            }
        };
        mOpenFileButton.setOnClickListener(openFileOnClickListener);


        mLoadFileButton = (Button) findViewById(R.id.loadFileButton);
        View.OnClickListener loadFileOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    CSVReader reader = new CSVReader(new FileReader(csvPath), ',');
                    String [] nextLine;
                    while ((nextLine = reader.readNext()) != null){
                        fileManager.addToMap(nextLine[0],nextLine[1],nextLine[2]);
                        Log.d("Output",fileManager.getValue(nextLine[0],nextLine[1]));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        mLoadFileButton.setOnClickListener(loadFileOnClickListener);

        mClearDataButton = (Button) findViewById(R.id.clearDataButton);
        View.OnClickListener clearDataOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fileManager.deleteFile();
            }
        };



    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.app_name));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mOpenFileButton = (Button) findViewById(R.id.openFileButton);
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
//                            Toast.makeText(MainActivity.this,
//                                    "File Selected: " + path, Toast.LENGTH_LONG).show();
                            mOpenFileButton.setText(path);
                            csvPath = path;
                        } catch (Exception e) {
                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        try {
//            startActivityForResult(
//                    Intent.createChooser(intent, "Select a File to Upload"),
//                    FILE_SELECT_CODE);
//
//        } catch (android.content.ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(this, "Please install a File Manager.",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case FILE_SELECT_CODE:
//                if (resultCode == RESULT_OK) {
//                    // Get the Uri of the selected file
//                    Uri uri = data.getData();
//                    // Get the path
//                    try {
//                        String path = getPath(this, uri);
//                        File file = new File(path);
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                    // Get the file instance
////                    File file = new File(path);
//                    // Initiate the upload
//                }
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public static String getPath(Context context, Uri uri) throws URISyntaxException {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = { "_data" };
//            Cursor cursor = null;
//
//            try {
//                cursor = context.getContentResolver().query(uri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow("_data");
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//                // Eat it
//            }
//        }
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
