package com.path.fusion.fusion.Activity;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.path.fusion.fusion.Controller.FileManager;
import com.path.fusion.fusion.Controller.PathAlgorithm;
import com.path.fusion.fusion.R;
import com.path.fusion.fusion.Object.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;

public class PathActivity extends ActionBarActivity {
    FileManager fileManager;
    Button mPathButton;
    Button mAltPathButton;
    Button mClearPathButton;
    Button mClearAltPathButton;
    Spinner mCategorySpinner;
    Spinner mResultSpinner;
    Spinner mCategorySpinner2;
    Spinner mResultSpinner2;
    TextView mTextView;
    TextView mAltTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher);
        }
        fileManager = FileManager.getInstance();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, fileManager.getUnique());
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, fileManager.getUnique());
        mCategorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        mResultSpinner = (Spinner) findViewById(R.id.resultSpinner);
        mCategorySpinner2 = (Spinner) findViewById(R.id.categorySpinner2);
        mResultSpinner2 = (Spinner) findViewById(R.id.resultSpinner2);
        mCategorySpinner.setAdapter(adapter);
        mResultSpinner.setAdapter(adapter);
        mCategorySpinner2.setAdapter(adapter);
        mResultSpinner2.setAdapter(adapter);

        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        mPathButton = (Button) findViewById(R.id.pathButton);
        View.OnClickListener pathOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAltTextView.setText("");
                findPath(mTextView, false, mCategorySpinner,mResultSpinner);
            }
        };
        mPathButton.setOnClickListener(pathOnClickListener);

        mAltTextView = (TextView) findViewById(R.id.altTextView);
        mAltTextView.setMovementMethod(new ScrollingMovementMethod());

        mAltPathButton = (Button) findViewById(R.id.altPathButton);
        View.OnClickListener altPathOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                findPath(mAltTextView, true, mCategorySpinner2,mResultSpinner2);
            }
        };
        mAltPathButton.setOnClickListener(altPathOnClickListener);


        mClearPathButton = (Button) findViewById(R.id.clearPathButton);
        View.OnClickListener clearPathOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTextView.setText("");
            }
        };
        mClearPathButton.setOnClickListener(clearPathOnClickListener);

        mClearAltPathButton = (Button) findViewById(R.id.clearAltPathButton);
        View.OnClickListener clearAltPathOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAltTextView.setText("");
            }
        };
        mClearAltPathButton.setOnClickListener(clearAltPathOnClickListener);
    }

    /**
     * findPath - Find the path using the PathAlgorithm and sets the path in the specified textview.
     * It may or may not append to the text view depending on the boolean.
     * @param textView
     * @param append
     */
    public void findPath(TextView textView, boolean append, Spinner spinnerCat, Spinner spinnerRes){
        if (validSpinner(fileManager.getUnique())) {
            PathAlgorithm pathAlgorithm = new PathAlgorithm();
            ArrayList<Vertex> ingredient = new ArrayList<>();
            ArrayList<Vertex> result = new ArrayList<>();
            String selection1 = spinnerCat.getSelectedItem().toString();
            String selection2 = spinnerRes.getSelectedItem().toString();
            Pair<String, String> pair;
            pathAlgorithm.execute(fileManager.getUniqueHash().get(selection1));
            LinkedList<Vertex> path = pathAlgorithm.getPath(fileManager.getUniqueHash().get(selection2));
            LinkedList<Vertex> ingredientPath = null;
            if(append == false) {
                textView.setText("");
            }
            int i = 0;
            if (path == null) {
                textView.append("There is no existing path.");
            } else {
                for (Vertex vertex : path) {
                    if (i != 0) {
                        textView.append(" + " + fileManager.getMaterial(result.get(i - 1).getName(), vertex.getName()) + " = " + vertex.getName() + "\n");
                    }
                    if (i == 0) {
                        textView.append(vertex.getName() + "\n");
                    }

                    result.add(vertex);
                    textView.append(vertex.getName());
//                    Log.d("PathActivity", vertex.getName());
                    i++;
                }
            }
            textView.append("\n-------\n");
        }
    }



    /**
     * validSpinner - Checks content of the Arraylist for the adapter to make sure it's not null or empty
     * @param content
     * @return
     */
    public boolean validSpinner(ArrayList<String> content){
        boolean status = false;
        if(content == null || content.isEmpty())
        {
            errorDialog("Please load CSV file before finding fusion results.");
        }
        else
        {
            status = true;
        }
        return status;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_path, menu);
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
