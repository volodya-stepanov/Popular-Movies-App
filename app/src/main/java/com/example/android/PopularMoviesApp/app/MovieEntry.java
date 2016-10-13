package com.example.android.PopularMoviesApp.app;

/**
 * Created by Володя on 10.10.2016.
 */
public class MovieEntry {
    String originalTitle;
    int image;  // drawable reference id
    String plotSynopsis;
    double userRating;
    String releaseDate;

    public MovieEntry(String vTitle){
        this.originalTitle = vTitle;
    }

    public MovieEntry(String vTitle, String vPlot, int image){
        this.originalTitle = vTitle;
        this.plotSynopsis = vPlot;
        this.image = image;
    }

    public MovieEntry(String vTitle, int image, String vPlot, double vUserRating, String vReleaseDate){
        this.originalTitle = vTitle;
        this.image = image;
        this.plotSynopsis = vPlot;
        this.userRating = vUserRating;
        this.releaseDate = vReleaseDate;
    }
}
