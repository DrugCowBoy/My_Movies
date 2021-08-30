package com.demo.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.mymovies.R;
import com.demo.mymovies.adapters.ReviewAdapter;
import com.demo.mymovies.adapters.VideoMovieAdapter;
import com.demo.mymovies.data.MainViewModel;
import com.demo.mymovies.data.Movie;
import com.demo.mymovies.videos_and_reviews.Review;
import com.demo.mymovies.videos_and_reviews.VideoMovie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// класс для работы с сетью
public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";// url для взятия основной информации о фильмах
    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos";// url для взятия всех видео к фильму
    private static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews";// url для взятия всех отзывов к фильму

    // ключи для значений в запросе
    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";
    // значения для ключей в запросе
    private static final String API_KEY = "b75a90e2a50e65bf4bf368204bef90c3";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";
    private static final String MIN_VOTE_COUNT = "1000";

    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;


    // !!!часть кода, отвечающая за загрузку основных данных о фильме!!!
    // метод для составления полного запроса для загрузки основных данных о фильме
    private static String buildURL(int page, int sort, String lang){
        String url= null;
        // в зависимости от значения sort обозначим тип сортировки
        String sortBy;
        if(sort == POPULARITY){
            sortBy = SORT_BY_POPULARITY;
        } else{
            sortBy = SORT_BY_TOP_RATED;
        }
        // создадим запрос типа URI
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .appendQueryParameter(PARAMS_SORT_BY, sortBy)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT).build();
        // изменим его тип
        url = uri.toString();
        return url;
    }

    // метод для загрузки JSON-объекта и взятия массива movies из этого объекта
    public static void getJSONObject(int page, int sort, Context context, MainViewModel viewModel, boolean[] isLoading, ProgressBar progressBar, String lang){
        progressBar.setVisibility(View.VISIBLE);
        isLoading[0] = true;
        String url = buildURL(page, sort, lang);

        // работаем с библиотекой volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ArrayList<Movie> movies = JSONUtils.getMoviesFromJSONObject(jsonObject);
                            if (!movies.isEmpty()){
                                Log.i("My", "массив movies не пустой, загрузим его в БД!");

                                if (page == 1){
                                    viewModel.deleteAllMovies();
                                }
                                for (Movie movie : movies){
                                    viewModel.insertMovie(movie);
                                }

                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            isLoading[0] = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("My", "JSON не загрузился");
                Toast.makeText(context, R.string.error_connection, Toast.LENGTH_SHORT).show();
                isLoading[0] = false;
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    // !!!

    // !!!часть кода, отвечающая за загрузку видео-трейлеров к фильму!!!
    // метод для составления полного запроса при скачивании видео к фильму
    private static String buildURLVideos(int id, String lang){
        String url = null;
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS, id)).buildUpon().
                appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang).build();
        url = uri.toString();
        return url;
    }

    // метод для загрузки JSON-объекта по составленному запросу
    public static void getJSONObjectVideos(int id, Context context, VideoMovieAdapter videoMovieAdapter, RecyclerView recyclerViewVideos, String lang, TextView textViewLabelTrailers){
        String url = buildURLVideos(id, lang);
        // работаем с библиотекой volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ArrayList<VideoMovie> videos = JSONUtils.getVideosFromJSONObject(jsonObject);
                    recyclerViewVideos.setLayoutManager(new LinearLayoutManager(context));
                    videoMovieAdapter.setVideos(videos);
                    recyclerViewVideos.setAdapter(videoMovieAdapter);
                    Log.i("My", "установили адаптер в recyclerViewVideos");

                    if (!videos.isEmpty()){
                        textViewLabelTrailers.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("My", "JSON-Videos не загрузился");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    // !!!

    // !!!часть кода, отвечающая за загрузку отзывов о фильме!!!
    // метод для составления полного запроса для загрузки отзывов
    private static String buildURLReview(int id, String lang){
        String url = null;
        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang).build();
        url = uri.toString();
        return url;
    }

    // метод для загрузки JSON-объекта по составленному запросу
    public static void getJSONObjectReviews(int id, Context context, ReviewAdapter reviewAdapter, RecyclerView recyclerViewReviews, String lang, TextView textViewLabelReviews){
        String url = buildURLReview(id, lang);
        // используем библиотеку Volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ArrayList<Review> reviews =JSONUtils.getReviewsFromJSON(jsonObject);
                    reviewAdapter.setReviews(reviews);
                    recyclerViewReviews.setLayoutManager(new LinearLayoutManager(context));
                    recyclerViewReviews.setAdapter(reviewAdapter);
                    Log.i("My", "установили адаптер в recyclerViewReviews");

                    if (!reviews.isEmpty()){
                        textViewLabelReviews.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("My", "JSON-Review не загружен");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    // !!!
}
