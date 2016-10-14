package com.example.android.PopularMoviesApp.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    public MovieFragment() {
    }

    private MovieEntryAdapter movieEntryAdapter;

//    MovieEntry[] movieEntries = {
//            new MovieEntry("Cupcake", "1.5", R.drawable.cupcake),
//            new MovieEntry("Donut", "1.6", R.drawable.donut),
//            new MovieEntry("Eclair", "2.0-2.1", R.drawable.eclair),
//            new MovieEntry("Froyo", "2.2-2.3", R.drawable.froyo),
//            new MovieEntry("Gingerbread", "2.3-2.3.7", R.drawable.gingerbread),
//            new MovieEntry("Honeycomb", "3.0-3.2.6", R.drawable.honeycomb),
//            new MovieEntry("Ice Cream Sandwich", "4.0-4.0.4", R.drawable.icecream),
//            new MovieEntry("Jelly Bean", "4.1-4.3.1", R.drawable.jellybean),
//            new MovieEntry("KitKat", "4.4-4.4.4, 4.4W-4.4W.2", R.drawable.kitkat),
//            new MovieEntry("Lollipop", "5.0-5.1.1", R.drawable.lollipop)
//    };

    MovieEntry[] movieEntries = {
            new MovieEntry("Cupcake"),
            new MovieEntry("Donut"),
            new MovieEntry("Eclair"),
            new MovieEntry("Froyo"),
            new MovieEntry("Gingerbread"),
            new MovieEntry("Honeycomb"),
            new MovieEntry("Ice Cream Sandwich"),
            new MovieEntry("Jelly Bean"),
            new MovieEntry("KitKat"),
            new MovieEntry("Lollipop")
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        List<MovieEntry> movieEntriesList = new ArrayList<MovieEntry>(Arrays.asList(movieEntries));

        // Создаём новый экземпляр класса MovieEntryAdapter. В качестве контекста передаём getActivity(), в качестве массива - flavorList.
        movieEntryAdapter = new MovieEntryAdapter(getActivity(), movieEntriesList);

        // Получаем ссылку на ListView и прикрепляем к нему адаптер
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(movieEntryAdapter);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute("top_rated");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, MovieEntry[]> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        //TODO: Доделать выборку остальных данных
        private MovieEntry[] getMoviesDataFromJson(String jsonStr) throws JSONException {
            // Создаём новый JSONObject
            JSONObject jsonObject = new JSONObject(jsonStr);

            // Получаем массив JSONArray по имени. В данном случае нас интересует массив results
            JSONArray list = jsonObject.getJSONArray("results");

            //TODO: Какое число здесь ставить? Можно поставить list.length()
            MovieEntry[] resultMovies = new MovieEntry[list.length()];
            for (int i = 0; i < list.length(); i++){

                JSONObject movie = list.getJSONObject(i);

                String title = movie.getString("original_title");
                String posterPath = movie.getString("poster_path");
                String plotSynopsys = movie.getString("overview");
                double userRating = movie.getDouble("vote_average");
                String releaseDate = movie.getString("release_date");

                resultMovies[i] = new MovieEntry(title, posterPath, plotSynopsys, userRating, releaseDate);
                //Log.v(LOG_TAG, "Result #" + i + ": " + title);
            }

            return resultMovies;
        }

        @Override
        protected MovieEntry[] doInBackground(String... params) {

            // Проверка размера массива (если нет параметра, нечего проверять)
            if (params.length == 0){
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

                // Создаём новый объект класса Uri.Builder
                Uri.Builder builder = new Uri.Builder();

                // Схема
                builder.scheme("http");

                // Авторитетный источник
                builder.authority("api.themoviedb.org");

                // Путь
                builder.path("3");

                // Метод appendPath кодирует данный сегмент и добавляет его к пути
                builder.appendPath("movie");

                //Этот сегмент зависит от параметра, переданного извне - популярные или с наивысшим рейтингом
                String sortOrder = params[0];
                builder.appendPath(sortOrder);

                // Метод appendQueryParameter кодирует ключ и значение и добавляет параметр к строке запросов
                builder.appendQueryParameter("language", "en-US");
                builder.appendQueryParameter("api_key", BuildConfig.MOVIE_DATABASE_API_KEY);

                // Создаём URI с заданными атрибутами
                Uri baseUri = builder.build();

                // Создаём URL на основе URI
                URL url = new URL(baseUri.toString());

                Log.v(LOG_TAG, "Built URI " + baseUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MovieEntry[] result) {
            if (result != null) {
                movieEntryAdapter.clear();

                //Если мы ориентируемся на HoneyComb и выше, можно использовать метод addAll, чтобы не добавлять записи одну за другой
                for (MovieEntry movieEntry : result){
                    movieEntryAdapter.add(movieEntry);
                }
            }
        }
    }
}



























