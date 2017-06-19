package app.popularmoviestage.project;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.androidmvp.bondol.project1.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import app.popularmoviestage.project.utilities.NetworkUtils;

public class MovieReviewActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView mUserReviewRecyclerView;
    MovieReview mMovieReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserReviewRecyclerView = (RecyclerView) findViewById(R.id.rvUserReview);
        new FetchMovieReview("https://api.themoviedb.org/3/movie/" + MovieDetailActivity.movieId + "/reviews?api_key=7382ed1b27affd4ad1b147d00bdbcde0").execute();
    }

    private void setUpRecyclerView(List<MovieReview.Result> movieReview) {
        mUserReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserReviewRecyclerView.setNestedScrollingEnabled(false);

        MovieReviewRecyclerViewAdapter mMovieReviewRecyclerViewAdapter = new MovieReviewRecyclerViewAdapter(this, movieReview);
        mUserReviewRecyclerView.setAdapter(mMovieReviewRecyclerViewAdapter);
    }

    @Override
    public void onItemClickCallback(Object data) {
        int index = (int) data;
        openWebPage(mMovieReview.getResults().get(index).getUrl());
    }

    private class FetchMovieReview extends AsyncTask<String, Void, String> {

        String requestUrl = "";

        FetchMovieReview(String url) {
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
                mMovieReview = gson.fromJson(movieData, MovieReview.class);
                setUpRecyclerView(mMovieReview.getResults());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void openWebPage(String url) {
        /*
         * We wanted to demonstrate the Uri.parse method because its usage occurs frequently. You
         * could have just as easily passed in a Uri as the parameter of this method.
         */
        Uri webpage = Uri.parse(url);

        /*
         * Here, we create the Intent with the action of ACTION_VIEW. This action allows the user
         * to view particular content. In this case, our webpage URL.
         */
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        /*
         * This is a check we perform with every implicit Intent that we launch. In some cases,
         * the device where this code is running might not have an Activity to perform the action
         * with the data we've specified. Without this check, in those cases your app would crash.
         */
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
