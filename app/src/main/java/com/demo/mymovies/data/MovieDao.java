package com.demo.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// интерфейс, в котором будут методы для работы с БД
@Dao
public interface MovieDao {
    // методы для объектов класса Movie
    // метод для вытаскивания из БД всех фильмов
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    // метод для удаления всех фильмов из БД
    @Query("DELETE FROM movies")
    void deleteAllMovies();

    // метод для вставки фильма в БД
    @Insert
    void insertMovie(Movie movie);

    // метод для удаления фильма из БД
    @Delete
    void deleteMovie(Movie movie);

    // метод для получения одного фильма из БД по его id
    @Query("SELECT * FROM movies WHERE id == :idMovie")
    Movie getMovieById(int idMovie);

    // методы для объектов класса FavouriteMovie
    // метод для вытаскивания из БД всех избранных фильмов
    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavouriteMovie>> getAllFavouriteMovies();

    // метод для вставки избранного фильма в БД
    @Insert
    void insertFavouriteMovie(FavouriteMovie favouriteMovie);

    // метод для удаления избранного фильма из БД
    @Delete
    void deleteFavouriteMovie(FavouriteMovie favouriteMovie);

    // метод для получения одного избранного фильма из БД по его id
    @Query("SELECT * FROM favourite_movies WHERE id == :idFavouriteMovie")
    FavouriteMovie getFavouriteMovieById(int idFavouriteMovie);
}
