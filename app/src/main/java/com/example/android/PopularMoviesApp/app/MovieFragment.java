package com.example.android.PopularMoviesApp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    public MovieFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Обновить список фильмов
        updateMovies();
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

    private ArrayList<MovieEntry> movieEntriesList;

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

    /**
     * Восстановление состояния операции
     * @param savedInstanceState Объект Bundle, содержащий информацию о состоянии экземпляра
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Сначала всегда вызываем суперкласс
        super.onCreate(savedInstanceState);

        // Поскольку метод onCreate() вызывается, если система создает новый экземпляр операции или восстанавливает предыдущий экземпляр, перед попыткой чтения необходимо убедиться, что Bundle имеет состояние null. В этом случае система создает новый экземпляр операции вместо восстановления ранее уничтоженного экземпляра.
        // Здесь мы проверяем, что объект savedInstanceState равен null или не содержит необходимого нам ключа массива
        if (savedInstanceState==null||!savedInstanceState.containsKey("movieEntries")){
            // В этом случае мы заново создаём movieEntries
            movieEntriesList = new ArrayList<MovieEntry>(Arrays.asList(movieEntries));
        }
        // Иначе (если такой ключ присутствует в объекте savedInstanceState)
        else {
            // Восстанавливаем flavorList из этого объекта
            movieEntriesList = savedInstanceState.getParcelableArrayList("movieEntries");
        }

        // Add this line in order for this fragment to handle menu events
        setHasOptionsMenu(true);
    }

    /**
     * Сохранение состояния операции
     * @param outState Объект Bundle, в котором хранятся пары "ключ-значение", сохраняющие состояние операции.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Сохраняем список вкусов с помощью метода putParcelableArrayList.
        // Метод putParcelableArrayList вставляет список Parcelable значений в схему соответствия этого объекта Bundle, заменяя любое существующее значение для данного ключа. Как ключ, так и значение могут быть null.
        outState.putParcelableArrayList("movieEntries", movieEntriesList);

        // Необходимо всегда вызывать суперкласс, чтобы он мог сохранить состояние иерархии представлений.
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //movieEntriesList = new ArrayList<MovieEntry>(Arrays.asList(movieEntries));

        // Создаём новый экземпляр класса MovieEntryAdapter. В качестве контекста передаём getActivity(), в качестве массива - flavorList.
        movieEntryAdapter = new MovieEntryAdapter(getActivity(), movieEntriesList);

        // Получаем ссылку на ListView и прикрепляем к нему адаптер
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(movieEntryAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieEntry clickedMovieEntry = movieEntryAdapter.getItem(position);

                Context context = getActivity();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movieEntry", clickedMovieEntry);
                startActivity(intent);
            }
        });

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
            // Обновить список фильмов
            updateMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Обновить список фильмов
     */
    private void updateMovies(){
        // Получае объект SharedPreferences для считывани настроек
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Получаем из SharedPreferences настройку порядка сортировки
        String sortOrder = sharedPreferences.getString(getString(R.string.pref_order_key), getString(R.string.pref_order_popular));

        // Создаём асинхронное задание для получения списка фильмов из интернета
        FetchMoviesTask moviesTask = new FetchMoviesTask();

        // Запускаем это задание, передавая в качестве параметра настройку порядка сортировки
        moviesTask.execute(sortOrder);
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
                String plotSynopsys = movie.getString("overview");
                String userRating = movie.getString("vote_average");
                String releaseDate = movie.getString("release_date");

                // Путь к изображению обрабатываем дополнительно, так как изначально в базе хранится относительный путь, а нам нужно присобачить к нему адрес и размер
                String relativePosterPath = movie.getString("poster_path");

                // Константы с адресом и размером
                final String POSTER_PATH_BASE_URL = "http://image.tmdb.org/t/p/";
                final String SIZE_PATH = "w342";

                // Сцепляем строки с помощью объекта StringBuilder
                StringBuilder builder = new StringBuilder(POSTER_PATH_BASE_URL);
                builder.append(SIZE_PATH);
                builder.append(relativePosterPath);
                String posterPath = builder.toString();

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



























