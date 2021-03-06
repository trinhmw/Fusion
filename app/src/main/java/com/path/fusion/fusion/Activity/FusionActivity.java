package com.path.fusion.fusion.Activity;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.path.fusion.fusion.Controller.FileManager;
import com.path.fusion.fusion.R;

import java.util.ArrayList;


public class FusionActivity extends ActionBarActivity {
    Spinner mCategory1Spinner;
    Spinner mCategory2Spinner;
    TextView mTextView;
    Button mFusionButton;
    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fusion);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher);
        }
        fileManager = FileManager.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, fileManager.getUnique());
        mCategory1Spinner = (Spinner) findViewById(R.id.category1Spinner);
        mCategory2Spinner = (Spinner) findViewById(R.id.category2Spinner);
        mCategory1Spinner.setAdapter(adapter);
        mCategory2Spinner.setAdapter(adapter);

        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        mFusionButton = (Button) findViewById(R.id.fusionButton);
        View.OnClickListener resultOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validSpinner(fileManager.getUnique())) {
                    String result = fileManager.getResult(mCategory1Spinner.getSelectedItem().toString(), mCategory2Spinner.getSelectedItem().toString());
                    if (result == null) {
                        mTextView.setText("No information available.");
                    } else {
                        mTextView.setText(result);
                    }
                }
            }
        };
        mFusionButton.setOnClickListener(resultOnClickListener);


    }

    /**
     * validSpinner - Checks content of the Arraylist for the adapter to make sure it's not null or empty
     *
     * @param content
     * @return
     */
    public boolean validSpinner(ArrayList<String> content) {
        boolean status = false;
        if (content == null || content.isEmpty()) {
            errorDialog("Please load CSV file before finding fusion results.");
        } else {
            status = true;
        }
        return status;
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
}
