package com.demo.mymovies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// класс для создания БД в синглтоне (в единственном экземпляре)
@Database(entities = {Movie.class, FavouriteMovie.class}, version = 6, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase database;
    private static final String DB_NAME = "movies.db";
    private static final Object LOCK = new Object();// объект синхронизации

    public static MoviesDatabase getInstance(Context context){
        synchronized (LOCK){
            if (database == null){
                database = Room.databaseBuilder(context, MoviesDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            }
            return database;
        }
    }

    public abstract MovieDao movieDao();// абстрактный метод для создания объекта MovieDao, чтобы можно было пользоваться его методами для работы с БД
}
