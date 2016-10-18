package com.example.android.PopularMoviesApp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.android.PopularMoviesApp.app.R.id.container;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private static final String MOVIES_SHARE_HASHTAG = " #PopularMoviesApp";
        private static final String RATING_LABEL = "User Rating: ";
        private static final String RELEASE_DATE_LABEL = "Release Date: ";
        private String mMovieTitleStr;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Наполняем меню; эта строка добавляет пункты на панель действий, если она присутствует
            inflater.inflate(R.menu.detailfragment, menu);

            // Находим пункт меню с объектом ShareActionProvider
            MenuItem item = menu.findItem(R.id.action_share);

            // Получаем и сохраняем ShareActionProvider
            ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareMoviesIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Получаем интент для приёма объекта от запустившей активности
            Intent intent = getActivity().getIntent();

            // Получаем корневой элемент
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // Если интент присутствует и содержит нужный нам ключ
            if (intent != null && intent.hasExtra("movieEntry")){
                // Получаем переданный объект из интента
                MovieEntry movieEntry = intent.getParcelableExtra("movieEntry");

                // Находим TextView для отображения заголовка
                TextView titleTextView = (TextView) rootView.findViewById(R.id.title_text);

                // Записываем название фильма в строку mMovieTitleStr
                mMovieTitleStr = movieEntry.getOriginalTitle();

                // Выводим эту строку в TextView
                titleTextView.setText(mMovieTitleStr);

                // Аналогичным образом поступаем с рейтингом, датой выхода и кратким описанием
                TextView ratingTextView = (TextView) rootView.findViewById(R.id.rating_text);
                ratingTextView.setText(RATING_LABEL + movieEntry.getUserRating());

                TextView releaseDateTextView = (TextView) rootView.findViewById(R.id.release_date_text);
                releaseDateTextView.setText(RELEASE_DATE_LABEL + movieEntry.getReleaseDate());

                TextView plotSynopsisTextView = (TextView) rootView.findViewById(R.id.plot_synopsis_text);
                plotSynopsisTextView.setText(movieEntry.getPlotSynopsis());

                ImageView posterView = (ImageView) rootView.findViewById(R.id.poster_view);
                Picasso.with(getActivity())
                        .load(movieEntry.getPosterPath())
                        //.resize(50, 50)
                        //.centerCrop()
                        .into(posterView);
            }
            return rootView;
        }

        //TODO: Как лучше поделиться информацией о фильме? Какие ещё бывают типы делёжки, кроме текста?
        private Intent createShareMoviesIntent(){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mMovieTitleStr + MOVIES_SHARE_HASHTAG);
            return shareIntent;
        }
    }
}