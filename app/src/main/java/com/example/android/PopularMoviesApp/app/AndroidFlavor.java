package com.example.android.PopularMoviesApp.app;

/**
 * Created by Володя on 10.10.2016.
 */
public class AndroidFlavor {
    String versionName;
    String versionNumber;
    int image;  // drawable reference id

    public AndroidFlavor(String vName, String vNumber, int image){
        this.versionName = vName;
        this.versionNumber = vNumber;
        this.image = image;
    }
}
