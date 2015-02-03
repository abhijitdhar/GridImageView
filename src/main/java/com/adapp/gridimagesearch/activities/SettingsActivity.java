package com.adapp.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.adapp.gridimagesearch.R;

public class SettingsActivity extends ActionBarActivity {

    String imgsz;
    String imgcolor;
    String imgtype;
    String as_sitesearch;

    Spinner spImageSz;
    Spinner spImageType;
    Spinner spColorFilter;
    EditText etSiteFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // sets the settings from previous save

        imgsz = getIntent().getStringExtra("imgsz");
        imgtype = getIntent().getStringExtra("imgtype");
        imgcolor = getIntent().getStringExtra("imgcolor");
        as_sitesearch = getIntent().getStringExtra("as_sitesearch");

        spImageSz = (Spinner) findViewById(R.id.spImageSz);
        setSpinnerAdapter(new String[] {"small", "medium", "large", "xlarge"}, spImageSz);

        spImageType = (Spinner) findViewById(R.id.spImageType);
        setSpinnerAdapter(new String[] {"face", "photo", "clipart", "lineart"}, spImageType);

        spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
        setSpinnerAdapter(new String[] {"black", "blue", "brown", "gray", "green"}, spColorFilter);

        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);

        setSpinnerToValue(spImageSz, imgsz);
        setSpinnerToValue(spImageType, imgtype);
        setSpinnerToValue(spColorFilter, imgcolor);
        etSiteFilter.setText(as_sitesearch);

    }

    private void setSpinnerAdapter(String [] list, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
        spinner.setAdapter(adapter);
    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    public void onSave(View view) {
        imgsz = spImageSz.getSelectedItem().toString();
        imgcolor = spColorFilter.getSelectedItem().toString();
        imgtype = spImageType.getSelectedItem().toString();

        as_sitesearch = etSiteFilter.getText().toString();

        Intent i = new Intent();
        i.putExtra("imgsz", imgsz);
        i.putExtra("imgcolor", imgcolor);
        i.putExtra("imgtype", imgtype);
        i.putExtra("as_sitesearch", as_sitesearch);

        setResult(RESULT_OK, i);

        this.finish();
    }

    public void onCancel(View view) {
        setResult(RESULT_CANCELED, null);
        this.finish();
    }
}
