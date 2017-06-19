package app.popularmoviestage.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidmvp.bondol.project1.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.popularmoviestage.project.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView mListMovieRecyclerView;
    private TextView mErrorLoadDataTextView;
    private ProgressBar mLoadingProgressBar;

    PopularMovieStageAdapter mPopularMovieStageAdapter;
    public static List<Result> sMovieList = new ArrayList<>();

    public static final String API_KEY = "YOUR_API_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListMovieRecyclerView = (RecyclerView) findViewById(R.id.rv_list_movie);
        mErrorLoadDataTextView = (TextView) findViewById(R.id.tv_error_load_data);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading_data);

        setUpRecyclerView();

        new FetchMovieData("https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular: {
                mPopularMovieStageAdapter.setData(null);
                new FetchMovieData("https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY).execute();
                return true;
            }
            case R.id.action_top_rate: {
                mPopularMovieStageAdapter.setData(null);
                new FetchMovieData("https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY).execute();
                return true;
            }
            case R.id.action_favorite: {
                for (int i = sMovieList.size() - 1; i > -1; i--) {
                    if (!sMovieList.get(i).isMovieFavoriteStatus()) {
                        sMovieList.remove(i);
                    }
                }
                mPopularMovieStageAdapter.setData(sMovieList);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void showErrorMessage() {
        mListMovieRecyclerView.setVisibility(View.INVISIBLE);
        mErrorLoadDataTextView.setVisibility(View.VISIBLE);
    }

    private void showMovieList() {
        mListMovieRecyclerView.setVisibility(View.VISIBLE);
        mErrorLoadDataTextView.setVisibility(View.INVISIBLE);
    }

    private class FetchMovieData extends AsyncTask<String, Void, String> {

        String requestUrl = "";

        FetchMovieData(String url) {
            requestUrl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgressBar.setVisibility(View.VISIBLE);
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
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieList();
                Gson gson = new GsonBuilder().create();
                MovieModel movieModel = gson.fromJson(movieData, MovieModel.class);
                sMovieList = movieModel.getResults();
                mPopularMovieStageAdapter.setData(sMovieList);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public void onItemClickCallback(Object data) {
        int index = (int) data;
        Result mResult = sMovieList.get(index);

        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(Constant.INTENT_OBJECT, mResult);
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mListMovieRecyclerView.setLayoutManager(gridLayoutManager);
        mListMovieRecyclerView.setHasFixedSize(true);
        mPopularMovieStageAdapter = new PopularMovieStageAdapter(this, this);
        mListMovieRecyclerView.setAdapter(mPopularMovieStageAdapter);
    }
}
