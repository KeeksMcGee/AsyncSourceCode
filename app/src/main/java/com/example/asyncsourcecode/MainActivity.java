package com.example.asyncsourcecode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, AdapterView.OnItemSelectedListener {

    private EditText urlInput;
    private String spinnerChoice;
    private TextView mSourceCodeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.urlEditText);
        mSourceCodeTextView = findViewById(R.id.source_code_viewer);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.https_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);
        if(spinner !=null){
            spinner.setOnItemSelectedListener(this);
            spinner.setAdapter(adapter);
        }

        if(getSupportLoaderManager().getLoader(0) !=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    public void getSourceCode(View view) {

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if(connMgr !=null){
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager !=null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }

        String queryString = urlInput.getText().toString();


        if(networkInfo !=null && networkInfo.isConnected() && (queryString.length() !=0)) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            queryBundle.putString("httpSwitch", spinnerChoice);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            mSourceCodeTextView.setText(R.string.loading);
        }
        else{
            if(queryString.length() ==0){
                Toast.makeText(this,R.string.no_url, Toast.LENGTH_LONG).show();
            }
            else if(!URLUtil.isValidUrl(queryString)){
                Toast.makeText(this,R.string.invalid_url,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this,R.string.no_connection,Toast.LENGTH_LONG).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle bundle) {
        String queryString ="";
        String httpSwitch = "";

        if(bundle !=null){
            queryString = bundle.getString("queryString");
            httpSwitch = bundle.getString("httpSwitch");
        }
        return new SourceCodeLoader(this,queryString, httpSwitch);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try{
            mSourceCodeTextView.setText(data);
         } catch(Exception e){
            e.printStackTrace();
            mSourceCodeTextView.setText(R.string.no_response);
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerChoice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        String[] values = getResources().getStringArray(R.array.https_array);
        spinnerChoice = values[0];
    }
}
