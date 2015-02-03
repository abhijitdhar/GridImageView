package com.adapp.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.adapp.gridimagesearch.R;
import com.adapp.gridimagesearch.adapters.ImageResultsAdapter;
import com.adapp.gridimagesearch.listeners.EndlessScrollListener;
import com.adapp.gridimagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {

    private static final int REQUEST_RESULT_CODE = 50;

    private String urlEndpoint = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&&rsz=8&q=";

    private ArrayList<ImageResult> imageResults;
    private EditText etQuery;
    private GridView gvResults;

    String imgsz="";
    String imgcolor="";
    String imgtype="";
    String as_sitesearch="";

    EndlessScrollListener endlessScrollListener;

    private ImageResultsAdapter imageResultsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();

        imageResults = new ArrayList<>();


        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);

        gvResults.setAdapter(imageResultsAdapter);
        endlessScrollListener = new EndlessScrollListener() {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                String query = etQuery.getText().toString();
                String searchUrl = urlEndpoint +
                        query +
                        "&start=" + totalItemsCount +
                        "&imgsz=" + imgsz +
                        "&imgcolor=" + imgcolor +
                        "&imgtype=" + imgtype +
                        "&as_sitesearch=" + as_sitesearch;
                Log.i("INFO", "start = " + totalItemsCount);
                fireGoogleSearchAPI(searchUrl, true);
            }
        };
        gvResults.setOnScrollListener(endlessScrollListener);


    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // launch the image display activity
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult image = imageResults.get(position);
                //i.putExtra("url", image.fullUrl);
                i.putExtra("result", image);
                startActivity(i);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
            //Toast.makeText(this, "Settings here", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, SettingsActivity.class);
            i.putExtra("imgsz", imgsz);
            i.putExtra("imgcolor", imgcolor);
            i.putExtra("imgtype", imgtype);
            i.putExtra("as_sitesearch", as_sitesearch);

            startActivityForResult(i, REQUEST_RESULT_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_RESULT_CODE) {
            if(resultCode == RESULT_OK) {
                imgsz = data.getStringExtra("imgsz");
                imgcolor = data.getStringExtra("imgcolor");
                imgtype = data.getStringExtra("imgtype");
                as_sitesearch = data.getStringExtra("as_sitesearch");
                //Toast.makeText(this, imgsz + ", " + imgcolor + ", " + imgtype + ", " + as_sitesearch, Toast.LENGTH_LONG).show();

                // fire a search again with updated query options from settings
                String query = etQuery.getText().toString();
                String searchUrl = urlEndpoint +
                        query +
                        "&imgsz=" + imgsz +
                        "&imgcolor=" + imgcolor +
                        "&imgtype=" + imgtype +
                        "&as_sitesearch=" + as_sitesearch;
                endlessScrollListener.reset();
                fireGoogleSearchAPI(searchUrl, false);
            }
        }
    }

    public void onImageSearch(View view) {

        String query = etQuery.getText().toString();
        //Toast.makeText(this, "Query is: " + query, Toast.LENGTH_SHORT).show();

        String searchUrl = urlEndpoint + query + "";
        endlessScrollListener.reset();
        fireGoogleSearchAPI(searchUrl, false);

    }

    private void fireGoogleSearchAPI(String searchUrl, final boolean pagination) {

        if(isNetworkAvailable() == false) {
            Toast.makeText(this, "Network connection unavailable!!", Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("DEBUG", response.toString());
                //response.getJSONArray("");

                try {
                    JSONArray arr = response.getJSONObject("responseData").getJSONArray("results");
                    //imageResults.clear();

                    if(pagination == false) {
                        imageResultsAdapter.clear();
                    }
                    imageResultsAdapter.addAll(ImageResult.fromJsonArray(arr));
                    //imageResults = ImageResult.fromJsonArray(arr);
                    //imageResultsAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.i("INFO", imageResults.toString());
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
