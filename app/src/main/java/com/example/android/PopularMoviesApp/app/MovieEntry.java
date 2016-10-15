package com.example.android.PopularMoviesApp.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Володя on 10.10.2016.
 */
public class MovieEntry implements Parcelable{
    String originalTitle;
    String posterPath;
    String plotSynopsis;
    String userRating;
    String releaseDate;

    public MovieEntry(String vTitle){
        this.originalTitle = vTitle;
    }

    public MovieEntry(String vTitle, String vPlot, String posterPath){
        this.originalTitle = vTitle;
        this.plotSynopsis = vPlot;
        this.posterPath = posterPath;
    }

    public MovieEntry(String vTitle, String posterPath, String vPlot, String vUserRating, String vReleaseDate){
        this.originalTitle = vTitle;
        this.posterPath = posterPath;
        this.plotSynopsis = vPlot;
        this.userRating = vUserRating;
        this.releaseDate = vReleaseDate;
    }

    /**
     * Конструктор класса на основе пакета parcel
     * @param in Пакет parcel
     */
    public MovieEntry(Parcel in){
        originalTitle = in.readString();
        posterPath = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }

    /**
     * Статическое поле Creator генерирует объект класса-передатчика
     * Как видим, здесь генерируется объект класса AndroidFlavor,
     * из этого можно сделать вывод, что именно он является классом-передатчиком.
     * Как сказали девчонки, эта функция предназначена для того, чтобы получить этот пакет и декодировать то, что в нём находится, с помощью конструктора класса на основе пакета.
     * Эта функция генерирует экземпляр parcelable-класса на основе parcel
     * Пурнима также сказала, что эта функция также будет использоваться при сохранении InstanceState
     */
    public static final Creator<MovieEntry> CREATOR = new Creator<MovieEntry>() {
        /**
         * Этот метод создает объект класса MovieEntry на основе пакета
         * @param in Пакет parcel
         * @return Объект класса MovieEntry, созданный на основе пакета
         */
        @Override
        public MovieEntry createFromParcel(Parcel in) {
            return new MovieEntry(in);
        }

        /**
         * Мы не используем этот метод, потому что мы используем ArrayList
         * @param size
         * @return
         */
        @Override
        public MovieEntry[] newArray(int size) {
            return new MovieEntry[size];
        }
    };

    /**
     * Описывает различного рода специальные объекты, описывающие интерфейс
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Упаковывает объект для передачи
     * @param dest Пакет, в который должен быть записан объект
     * @param flags Дополнительные флаги о том, как должен быть записан объект. Может быть 0 или PARCELABLE_WRITE_RETURN_VALUE. Если указан этот флаг, объект записывается как возвращаемое значение, которое является результатом функции, например, "Parcelable someFunction()", "void someFunction(out Parcelable)", или "void someFunction(inout Parcelable)". Некоторые реализации могут захотеть освободить ресурсы в этой точке.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(plotSynopsis);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }
}
