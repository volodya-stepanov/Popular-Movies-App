package com.example.android.PopularMoviesApp.app;

/**
 * Created by Володя on 10.10.2016.
 */
public class MovieEntry {
    String originalTitle;
    String posterPath;
    String plotSynopsis;
    double userRating;
    String releaseDate;

    public MovieEntry(String vTitle){
        this.originalTitle = vTitle;
    }

    public MovieEntry(String vTitle, String vPlot, String posterPath){
        this.originalTitle = vTitle;
        this.plotSynopsis = vPlot;
        this.posterPath = posterPath;
    }

    public MovieEntry(String vTitle, String posterPath, String vPlot, double vUserRating, String vReleaseDate){
        this.originalTitle = vTitle;
        this.posterPath = posterPath;
        this.plotSynopsis = vPlot;
        this.userRating = vUserRating;
        this.releaseDate = vReleaseDate;
    }
}
