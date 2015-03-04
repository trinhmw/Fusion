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
    Button mFusionButton;
    Button mPathButton;
    Button mGenerateEdgesButton;

    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fileManager = FileManager.getInstance();
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
                    loadFile();
//                    CSVReader reader = new CSVReader(new FileReader(csvPath), ',', ';', 1);
//                    String [] nextLine;
//                    while ((nextLine = reader.readNext()) != null){
//                        fileManager.addToMap(nextLine[0],nextLine[1],nextLine[2]);
//                        Log.d("OutputMainActivity",fileManager.getValue(nextLine[0],nextLine[1]));
//                    }
//                    fileManager.uniqueStringToVertexSet();
//                    Toast.makeText(getApplicationContext(), "Data Storage complete.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), "Generating Edges.", Toast.LENGTH_SHORT).show();
//                    fileManager.generateAllEdges();
//                    Toast.makeText(getApplicationContext(), "Load complete.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Data deleted.", Toast.LENGTH_SHORT).show();
            }
        };
        mClearDataButton.setOnClickListener(clearDataOnClickListener);

        mFusionButton = (Button) findViewById(R.id.fusionButton);
        View.OnClickListener fusionOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent fusionIntent = new Intent(getApplicationContext(), FusionActivity.class);
                startActivity(fusionIntent);
            }
        };
        mFusionButton.setOnClickListener(fusionOnClickListener);

        mPathButton = (Button) findViewById(R.id.pathButton);
        View.OnClickListener pathOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent fusionIntent = new Intent(getApplicationContext(), PathActivity.class);
                startActivity(fusionIntent);
            }
        };
        mPathButton.setOnClickListener(pathOnClickListener);


        mGenerateEdgesButton = (Button) findViewById(R.id.generateEdgesButton);
        View.OnClickListener generateOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fileManager.generateAllEdges();
//                fileManager.generateEdge(fileManager.getUniqueHash().get("Aquans"));
            }
        };
        mGenerateEdgesButton.setOnClickListener(generateOnClickListener);

    }

    private void loadFile() throws IOException {
        CSVReader reader = new CSVReader(new FileReader(csvPath), ',', ';', 1);
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null){
            fileManager.addToMap(nextLine[0],nextLine[1],nextLine[2]);
            Log.d("OutputMainActivity",fileManager.getValue(nextLine[0],nextLine[1]));
        }
        fileManager.uniqueStringToVertexSet();
        Toast.makeText(getApplicationContext(), "Data Storage complete.", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Generating Edges.", Toast.LENGTH_SHORT).show();
        fileManager.generateAllEdges();
        Toast.makeText(getApplicationContext(), "Load complete.", Toast.LENGTH_SHORT).show();
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
