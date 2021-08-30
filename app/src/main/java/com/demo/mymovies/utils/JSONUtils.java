package com.demo.mymovies.utils;

import android.util.Log;

import com.demo.mymovies.data.Movie;
import com.demo.mymovies.videos_and_reviews.Review;
import com.demo.mymovies.videos_and_reviews.VideoMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// класс для обработки JSON-ответа
public class JSONUtils {

    // ключи для работы с JSON-объектом
    private static final String KEY_RESULTS = "results";

    // ключи для видео
    private static final String KEY_NAME = "name";
    private static final String KEY_KEY_VIDEO = "key";

    // ключи для отзывов
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT= "content";

    // ключи для фильма
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_RELEASE_DATE = "release_date";

    // константы для загрузки изображений
    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE = "w342";
    private static final String BIG_POSTER_SIZE = "w780";

    // константы для загрузки видео
    private static final String BASE_YOUTUBE = "https://www.youtube.com/watch?v=";

    // метод для получения массива с фильмами из JSON-объекта
    public static ArrayList<Movie> getMoviesFromJSONObject(JSONObject jsonObject){

        ArrayList<Movie> movies = new ArrayList<>();// создали пустой массив для фильмов
        if (jsonObject == null){
            Log.i("My", "jsonObject = null, массив movies - пустой");
            return movies;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            // будем фильмы добавлять в массив movies в цикле, пока не добавим их все
            for (int i =0; i<jsonArray.length();i++){
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                // получим значения для создания объекта Movie
                int id = objectMovie.getInt(KEY_ID);
                String title = objectMovie.getString(KEY_TITLE);
                String originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE);
                int voteCount = objectMovie.getInt(KEY_VOTE_COUNT);
                double voteAverage = objectMovie.getDouble(KEY_VOTE_AVERAGE);
                String overview = objectMovie.getString(KEY_OVERVIEW);
                String backdropPath = objectMovie.getString(KEY_BACKDROP_PATH);
                String posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String releaseDate = objectMovie.getString(KEY_RELEASE_DATE);
                // создадим Movie
                Movie movie = new Movie(id, title, originalTitle, voteCount, voteAverage, overview, backdropPath, posterPath, bigPosterPath, releaseDate);
                // добавим его в массив movies
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    // метод для получения массива с видео для одного выбранного фильма
    public static ArrayList<VideoMovie> getVideosFromJSONObject(JSONObject jsonObject){
        ArrayList<VideoMovie> videos = new ArrayList<>();
        if (jsonObject == null){
            return videos;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i<jsonArray.length();i++){
                JSONObject objectVideo = jsonArray.getJSONObject(i);
                String name = objectVideo.getString(KEY_NAME);
                String key = BASE_YOUTUBE + objectVideo.getString(KEY_KEY_VIDEO);
                VideoMovie video = new VideoMovie(name, key);
                videos.add(video);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videos;
    }

    // метод для получения массива отзывов об одном выбранном фильме
    public static ArrayList<Review> getReviewsFromJSON(JSONObject jsonObject){
        ArrayList<Review> reviews = new ArrayList<>();
        if (jsonObject == null){
            return reviews;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i< jsonArray.length(); i++){
                JSONObject objectReview = jsonArray.getJSONObject(i);
                String author =objectReview.getString(KEY_AUTHOR);
                String content = objectReview.getString(KEY_CONTENT);
                Review review = new Review(author, content);
                reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
