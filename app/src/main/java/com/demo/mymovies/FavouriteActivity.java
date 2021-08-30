package com.demo.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mymovies.adapters.MoviesAdapter;
import com.demo.mymovies.data.FavouriteMovie;
import com.demo.mymovies.data.MainViewModel;
import com.demo.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavouriteMovies;

    private MainViewModel viewModel;

    // метод для создания меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();// MenuInflater нужен для создания меню из файла xml
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // метод для нажатия по элементам меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();
        if (idItem == R.id.item_main){
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        } else if(idItem == R.id.item_favourite){
            Intent intentFavourite = new Intent(this, FavouriteActivity.class);
            startActivity(intentFavourite);
        }
        return super.onOptionsItemSelected(item);
    }

    // метод для получения количества колонок с фильмами в зависимости от размера экрана
    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();// создадим новый объект DisplayMetrics
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);// в переменную displayMetrics поместим информацию об экране
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);// получили ширину экрана в аппаратных пикселях
        // 185 - ширина маленького постера
        if (width/185 > 2){
            int columns =(int) width/185;
            return columns;
        } else{
            return 2;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        // присвоим recyclerViewFavouriteMovies ссылку на реальный объект и установим ему внешний вид
        recyclerViewFavouriteMovies = findViewById(R.id.recyclerViewFavouriteMovies);
        recyclerViewFavouriteMovies.setLayoutManager(new GridLayoutManager(this,getColumnCount()));

        // создадим адаптер и установим его для списка
        MoviesAdapter moviesAdapter = new MoviesAdapter();
        recyclerViewFavouriteMovies.setAdapter(moviesAdapter);

        // создадим viewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // возьмём из viewModel массив с избранными фильмами
        LiveData<List<FavouriteMovie>> favouriteMovies = viewModel.getFavouriteMovies();
        favouriteMovies.observe(this, new Observer<List<FavouriteMovie>>() {
            @Override
            public void onChanged(List<FavouriteMovie> favouriteMovies) {
                List<Movie> movies = new ArrayList<>();
                movies.addAll(favouriteMovies);
                moviesAdapter.setMovies(movies);
            }
        });

        // установим слушатель кликов по элементу списка
        moviesAdapter.setOnPosterClickListener(new MoviesAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = moviesAdapter.getMovies().get(position);
                // в интент помещаем полученный фильм Movie
                Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
                intent.putExtra("Movie", movie);
                startActivity(intent);
            }
        });
    }
}