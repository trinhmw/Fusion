package com.path.fusion.fusion.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.opencsv.CSVReader;
import com.path.fusion.fusion.Controller.FileManager;
import com.path.fusion.fusion.R;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends ActionBarActivity {
    private static final int REQUEST_CODE = 4841;
    private static final String TAG = "MainActivity";
    private static String csvPath = null;
    private final String NO_FILE = "There is no CSV file loaded.";
    Button mOpenFileButton;
    Button mLoadFileButton;
    Button mFusionButton;
    Button mPathButton;
    Button mListButton;

    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fileManager = FileManager.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher);
        }
        mOpenFileButton = (Button) findViewById(R.id.openFileButton);


        View.OnClickListener openFileOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showChooser();
            }
        };
        mOpenFileButton.setOnClickListener(openFileOnClickListener);


        mLoadFileButton = (Button) findViewById(R.id.loadFileButton);
        mLoadFileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    loadFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        mListButton = (Button) findViewById(R.id.listButton);
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(listIntent);
            }
        });

        mFusionButton = (Button) findViewById(R.id.fusionButton);
        mFusionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent fusionIntent = new Intent(getApplicationContext(), FusionActivity.class);
                startActivity(fusionIntent);
            }
        });

        mPathButton = (Button) findViewById(R.id.pathButton);
        mPathButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent fusionIntent = new Intent(getApplicationContext(), PathActivity.class);
                startActivity(fusionIntent);
            }
        });

    }

    /**
     * loadFile - Reads the path to the CSV file and uses the File Manager to save the data onto the Android External Storage.
     * This stores data, and then generates the links/relations between the data.
     * @throws IOException
     */
    private void loadFile() throws IOException {
        if(validFile(csvPath)) {
            CSVReader reader = new CSVReader(new FileReader(csvPath), ',', ';', 1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                fileManager.addToMap(nextLine[0], nextLine[1], nextLine[2]);
                Log.d("OutputMainActivity", fileManager.getResult(nextLine[0], nextLine[1]));
            }
            fileManager.uniqueStringToVertexSet();
            fileManager.generateAllEdges();
            dialog("Load file complete.");
        }
    }

    /**
     * validFile - Checks whether the csvPath is an actual CSV or has even been loaded.
     * @param csvPath
     * @return
     */
    private boolean validFile(String csvPath){
        boolean status = false;
        if(csvPath == null){
            errorDialog("There is no file loaded");
        }
        else if((csvPath.equals("")) || csvPath.isEmpty()){
            errorDialog("There is no file loaded");
        }
        else if(!(((csvPath.substring(csvPath.lastIndexOf('.'))).equals(".csv")) ||
        (csvPath.substring(csvPath.lastIndexOf('.'))).equals(".CSV"))){
            errorDialog("Wrong file type loaded.");
        }
        else
        {
            status = true;
        }

        return status;
    }

    /**
     * showChooser - Shows the file chooser.
     */
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
                            Log.e("MainAcitvity", "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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

    public void errorDialog(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.setTitle("Error");
        TextView textView = (TextView) dialog.findViewById(R.id.dialogText);
        textView.setText(message);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void dialog(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.setTitle("Status");
        TextView textView = (TextView) dialog.findViewById(R.id.dialogText);
        textView.setText(message);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
