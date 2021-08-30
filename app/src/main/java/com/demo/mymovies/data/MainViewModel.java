package com.demo.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static MoviesDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavouriteMovie>> favouriteMovies;

    public MainViewModel(@NonNull @NotNull Application application) {
        super(application);
        // создадим БД в конструкторе MainViewModel
        database = MoviesDatabase.getInstance(getApplication());
        // получим массив фильмов из БД
        movies = database.movieDao().getAllMovies();
        // получим массив избранных фильмов из БД
        favouriteMovies = database.movieDao().getAllFavouriteMovies();
    }

    // методы для объектов класса Movie
    // метод для вытаскивания из БД всех фильмов
    public LiveData<List<Movie>> getAllMovies() {
        return movies;
    }

    // метод для удаления всех фильмов из БД
    public void deleteAllMovies(){
        new deleteAllTask().execute();
    }
    private static class deleteAllTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            database.movieDao().deleteAllMovies();
            return null;
        }
    }

    // метод для вставки фильма в БД
    public void insertMovie(Movie movie){
        new insertTask().execute(movie);
    }
    private static class insertTask extends AsyncTask<Movie, Void, Void>{
        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies.length > 0){
                database.movieDao().insertMovie(movies[0]);
            }
            return null;
        }
    }

    // метод для удаления фильма из БД
    public void deleteMovie(Movie movie){
        new deleteTask().execute(movie);
    }
    private static class deleteTask extends AsyncTask<Movie,Void,Void>{
        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies.length > 0){
                database.movieDao().deleteMovie(movies[0]);
            }
            return null;
        }
    }

    // метод для получения одного фильма из БД по его id
    public Movie getMovieById(int idMovie){
        Movie movie = null;
        try {
            movie = new getMovieTask().execute(idMovie).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return movie;
    }
    private static class getMovieTask extends AsyncTask<Integer,Void,Movie>{
        @Override
        protected Movie doInBackground(Integer... integers) {
            Movie movie = null;
            if (integers.length > 0){
                movie = database.movieDao().getMovieById(integers[0]);
            }
            return movie;
        }
    }

    // методы для объектов класса FavouriteMovie
    // метод для вытаскивания из БД всех избранных фильмов
    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    // метод для вставки избранного фильма в БД
    public void insertFavouriteMovie(FavouriteMovie favouriteMovie){
        new InsertFavouriteTask().execute(favouriteMovie);
    }
    private static class InsertFavouriteTask extends AsyncTask<FavouriteMovie, Void, Void>{
        @Override
        protected Void doInBackground(FavouriteMovie... favouriteMovies) {
            if (favouriteMovies.length > 0){
                database.movieDao().insertFavouriteMovie(favouriteMovies[0]);
            }
            return null;
        }
    }

    // метод для удаления избранного фильма из БД
    public void deleteFavouriteMovie(FavouriteMovie favouriteMovie){
        new DeleteFavouriteTask().execute(favouriteMovie);
    }
    private static class DeleteFavouriteTask extends AsyncTask<FavouriteMovie, Void, Void>{
        @Override
        protected Void doInBackground(FavouriteMovie... favouriteMovies) {
            if (favouriteMovies.length > 0){
                database.movieDao().deleteFavouriteMovie(favouriteMovies[0]);
            }
            return null;
        }
    }

    // метод для получения одного избранного фильма из БД по его id
    public FavouriteMovie getFavouriteMovieById(int idFavouriteMovie){
        FavouriteMovie favouriteMovie = null;
        try {
            favouriteMovie = new GetFavouriteMovieTask().execute(idFavouriteMovie).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return favouriteMovie;
    }
    private static class GetFavouriteMovieTask extends AsyncTask<Integer, Void, FavouriteMovie>{
        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            FavouriteMovie favouriteMovie = null;
            if (integers.length > 0){
                favouriteMovie = database.movieDao().getFavouriteMovieById(integers[0]);
            }
            return favouriteMovie;
        }
    }
}
