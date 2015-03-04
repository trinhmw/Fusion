package com.path.fusion.fusion;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

public class PathActivity extends ActionBarActivity {
    FileManager fileManager;
    Button mPathButton;
    Spinner mCategorySpinner;
    Spinner mResultSpinner;
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        fileManager = FileManager.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, fileManager.getUnique());
        mCategorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        mResultSpinner = (Spinner) findViewById(R.id.resultSpinner);
        mCategorySpinner.setAdapter(adapter);
        mResultSpinner.setAdapter(adapter);

        mTextView = (TextView) findViewById(R.id.textView);

        mPathButton = (Button) findViewById(R.id.pathButton);
        View.OnClickListener pathOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PathAlgorithm pathAlgorithm = new PathAlgorithm();
                ArrayList<Vertex> ingredient = new ArrayList<>();
                ArrayList<Vertex> result = new ArrayList<>();
                String selection1 = mCategorySpinner.getSelectedItem().toString();
                String selection2 = mResultSpinner.getSelectedItem().toString();
                Pair<String,String> pair;
                pathAlgorithm.execute(fileManager.getUniqueHash().get(selection1));
                LinkedList<Vertex> path = pathAlgorithm.getPath(fileManager.getUniqueHash().get(selection2));
                LinkedList<Vertex> ingredientPath = null;
                mTextView.setText("");
                int i = 0;
                if (path == null){
                    mTextView.append("There is no existing path.");
                }
                else {
                    for (Vertex vertex : path) {
                        if (i != 0) {
                            mTextView.append(" + " + fileManager.getMaterial(result.get(i - 1).getName(), vertex.getName()) + "=" + vertex.getName() + "\n");
                        }
                        if (i == 0) {
                            mTextView.append(vertex.getName() + "\n");
                        }

                        result.add(vertex);
                        mTextView.append(vertex.getName());
                        Log.d("PathActivity", vertex.getName());
                        i++;
                    }
                }
            }
        };
        mPathButton.setOnClickListener(pathOnClickListener);



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
