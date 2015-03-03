package com.path.fusion.fusion;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class FusionActivity extends ActionBarActivity {
    Spinner mCategory1Spinner;
    Spinner mCategory2Spinner;
    Button mFusionButton;
    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fusion);
        fileManager = FileManager.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, fileManager.getUnique());
        mCategory1Spinner = (Spinner) findViewById(R.id.category1Spinner);
        mCategory2Spinner = (Spinner) findViewById(R.id.category2Spinner);
        mCategory1Spinner.setAdapter(adapter);
        mCategory2Spinner.setAdapter(adapter);

        mFusionButton = (Button) findViewById(R.id.fusionButton);
        View.OnClickListener resultOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String result = fileManager.getValue(mCategory1Spinner.getSelectedItem().toString(), mCategory2Spinner.getSelectedItem().toString());
                mFusionButton.setText(result);
            }
        };
        mFusionButton.setOnClickListener(resultOnClickListener);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fusion, menu);
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
