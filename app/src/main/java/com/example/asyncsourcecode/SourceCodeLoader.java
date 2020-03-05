package com.example.asyncsourcecode;

import android.content.Context;
import android.net.Network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class SourceCodeLoader extends AsyncTaskLoader {

    private String urlQueryString;
    Context mContext;
    private String mhttpSwitch;

    public SourceCodeLoader(@NonNull Context context, String queryString, String httpSwitch) {
        super(context);
        urlQueryString = queryString;
        mContext = context;
        mhttpSwitch = httpSwitch;
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        return NetworkUtils.getSourceCodeInfo(mContext, urlQueryString, mhttpSwitch);
    }

    @Override
    protected void onStartLoading(){
        super.onStartLoading();
        forceLoad();
    }
}
