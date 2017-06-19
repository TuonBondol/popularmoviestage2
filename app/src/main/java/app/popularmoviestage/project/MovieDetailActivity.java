package app.popularmoviestage.project;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmvp.bondol.project1.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.popularmoviestage.project.utilities.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener {

    private RecyclerView mTrailerRecyclerView;
    private MovieTrailerRecyclerViewAdapter mMovieTrailerRecyclerViewAdapter;
    public static String movieId;

    public static String movieKey = "";
    ImageView mMarkAsFavorite;

    Result mResultModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView mMovieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        TextView mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        ImageView mMovieImageView = (ImageView) findViewById(R.id.iv_movie_image);
        TextView mUserRateTextView = (TextView) findViewById(R.id.tv_user_rate);
        TextView mOverViewTextView = (TextView) findViewById(R.id.tv_overview);
        mMarkAsFavorite = (ImageView) findViewById(R.id.mark_as_favorite);

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailer);

        Intent intent = getIntent();
        if (intent.hasExtra(Constant.INTENT_OBJECT)) {
            mResultModel = (Result) intent.getSerializableExtra(Constant.INTENT_OBJECT);
            if (mResultModel.isMovieFavoriteStatus()) {
                mMarkAsFavorite.setImageResource(R.drawable.star_full);
            }
            mMovieTitleTextView.setText(mResultModel.getOriginalTitle());
            mReleaseDateTextView.setText(mResultModel.getReleaseDate());
            Picasso.with(this)
                    .load(PopularMovieStageAdapter.BASE_HOST_IMAGE + PopularMovieStageAdapter.IMAGE_WIDTH + mResultModel.getPosterPath())
                    .into(mMovieImageView);
            mUserRateTextView.setText(mResultModel.getVoteAverage() + getResources().getString(R.string.rate_ten));
            mOverViewTextView.setText(mResultModel.getOverview());
            movieId = String.valueOf(mResultModel.getId());
            new FetchMovieTrailer("https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=7382ed1b27affd4ad1b147d00bdbcde0").execute();
        }

        mMarkAsFavorite.setOnClickListener(this);
    }

    private void setUpRecyclerView(List<MovieTrailer.Result> movieTrailer) {
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailerRecyclerView.setNestedScrollingEnabled(false);

        mMovieTrailerRecyclerViewAdapter = new MovieTrailerRecyclerViewAdapter(movieTrailer);
        mMovieTrailerRecyclerViewAdapter.setmItemClickListener(this);
        mTrailerRecyclerView.setAdapter(mMovieTrailerRecyclerViewAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mark_as_favorite: {
                List<Result> mResultList = new ArrayList<>();
                for (int i = 0; i < MainActivity.sMovieList.size(); i++) {
                    if (MainActivity.sMovieList.get(i).getId().equals(mResultModel.getId())) {
                        boolean favoriteStatus = MainActivity.sMovieList.get(i).isMovieFavoriteStatus();
                        if (favoriteStatus) {
                            favoriteStatus = false;
                            mMarkAsFavorite.setImageResource(R.drawable.star_empty);

                        } else {
                            favoriteStatus = true;
                            mMarkAsFavorite.setImageResource(R.drawable.star_full);
                        }
                        Result result = new Result();
                        result.setId(MainActivity.sMovieList.get(i).getId());
                        result.setMovieFavoriteStatus(favoriteStatus);
                        result.setAdult(MainActivity.sMovieList.get(i).getAdult());
                        result.setBackdropPath(MainActivity.sMovieList.get(i).getBackdropPath());
                        result.setGenreIds(MainActivity.sMovieList.get(i).getGenreIds());
                        result.setOriginalLanguage(MainActivity.sMovieList.get(i).getOriginalLanguage());
                        result.setOriginalTitle(MainActivity.sMovieList.get(i).getOriginalTitle());
                        result.setOverview(MainActivity.sMovieList.get(i).getOverview());
                        result.setPopularity(MainActivity.sMovieList.get(i).getPopularity());
                        result.setPosterPath(MainActivity.sMovieList.get(i).getPosterPath());
                        result.setReleaseDate(MainActivity.sMovieList.get(i).getReleaseDate());
                        result.setTitle(MainActivity.sMovieList.get(i).getTitle());
                        result.setVideo(MainActivity.sMovieList.get(i).getVideo());
                        result.setVoteAverage(MainActivity.sMovieList.get(i).getVoteAverage());
                        result.setVoteCount(MainActivity.sMovieList.get(i).getVoteCount());
                        mResultList.add(result);
                    } else {
                        mResultList.add(MainActivity.sMovieList.get(i));
                    }
                }
                MainActivity.sMovieList = mResultList;
                break;
            }
        }
    }

    @Override
    public void onItemClickCallback(Object data) {
        Intent mIntent = new Intent(this, PlayTrailerActivity.class);
        startActivity(mIntent);
    }

    private class FetchMovieTrailer extends AsyncTask<String, Void, String> {

        String requestUrl = "";

        FetchMovieTrailer(String url) {
            requestUrl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(requestUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                return NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String movieData) {
            if (movieData != null) {
                Gson gson = new GsonBuilder().create();
                MovieTrailer mMovieTrailer = gson.fromJson(movieData, MovieTrailer.class);
                setUpRecyclerView(mMovieTrailer.getResults());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.movie_review: {
                startActivity(new Intent(this, MovieReviewActivity.class));
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
