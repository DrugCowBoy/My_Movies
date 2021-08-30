package com.demo.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mymovies.adapters.MoviesAdapter;
import com.demo.mymovies.data.MainViewModel;
import com.demo.mymovies.data.Movie;
import com.demo.mymovies.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMovies;
    private MoviesAdapter moviesAdapter;
    private Switch switchSort;
    private TextView textViewPopularity;
    private TextView textViewTopRated;
    private ProgressBar progressBar;

    private MainViewModel viewModel;

    private int page = 1;// начало загрузки фильмов происходит с 1-ой страницы
    private boolean[] isLoading = {false};
    private boolean isCheckedSwitch;

    private String lang;// переменная для хранения языка на устройстве пользователя

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
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isCheckedSwitch", isCheckedSwitch);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // вытащим из savedInstanceState значение isCheckedSwitch
        if (savedInstanceState != null){
            isCheckedSwitch = savedInstanceState.getBoolean("isCheckedSwitch");
        } else{
            isCheckedSwitch = false;
        }

        // получим язык на устройстве
        lang = Locale.getDefault().getLanguage();
        // присвоим переменным ссылки на реальные объекты
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        switchSort = findViewById(R.id.switchSort);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        progressBar = findViewById(R.id.progressBar);

        // создадим ViewModel и соответственно БД
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // установим внешний вид recyclerViewMovies
        GridLayoutManager layoutManager = new GridLayoutManager(this,getColumnCount());
        recyclerViewMovies.setLayoutManager(layoutManager);
        // создадим пустой адаптер
        moviesAdapter = new MoviesAdapter();

        // установим начальное положение switch
        if (savedInstanceState == null || isCheckedSwitch == true){
            switchSort.setChecked(true);
        } else{
            switchSort.setChecked(false);
        }

        // установим слушатель на switchSort
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = 1;
                setMoviesForSwitch(isChecked, page);
            }
        });
        switchSort.setChecked(isCheckedSwitch);


        // вытащим все фильмы из БД и поместим их в адаптер
        // если мы не загрузили фильмы (нет интернета), то мы будем их брать просто из БД - из последней загрузки в базу данных
        LiveData<List<Movie>> moviesFromDB = viewModel.getAllMovies();
        moviesFromDB.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> moviesLiveData) {
                moviesAdapter.setMovies(moviesLiveData);// установили в адаптер массив фильмов
                Log.i("My", "установили в адаптер массив фильмов");
            }
        });
        // установим адаптер для recyclerView
        recyclerViewMovies.setAdapter(moviesAdapter);

        // слушатель кликов по элементу списка
        moviesAdapter.setOnPosterClickListener(new MoviesAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                // при нажатии на элемент будем открывать другую активность с детальной информацией о фильме
                // будем брать массив фильмов из Адаптера, так как там всегда актуальная информация
                Movie movie = moviesAdapter.getMovies().get(position);
                // в интент помещаем полученный фильм Movie
                Intent intentDetail = new Intent(MainActivity.this, DetailActivity.class);
                intentDetail.putExtra("Movie", movie);
                startActivity(intentDetail);
            }
        });

        // слушатель для отслеживания того, что скоро конец списка
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();// количество элементов на экране
                int totalItemCount = layoutManager.getItemCount();// всего элементов в списке
                int firstItemPosition = layoutManager.findFirstVisibleItemPosition();// позиция первого элемента
                // проверяем, грузим мы что-то или нет; это чтобы мы делали подгрузку, только если полностью загрузились предыдущие данные
                if (!isLoading[0]){
                    if ((firstItemPosition + visibleItemCount) >= totalItemCount && firstItemPosition != 0 && totalItemCount/page == 20){
                        // чтобы когда мы достигли конца списка - метод onScrolled выполнялся лишь 1 раз
                        isLoading[0] = true;

                        page++;
                        setMoviesForSwitch(isCheckedSwitch, page);
                        Log.i("My", "конец списка "+ page);
                    }
                }
            }
        };

        recyclerViewMovies.addOnScrollListener(onScrollListener);
    }

    // методы для нажатий по тексту о выборе сортировки
    public void onClickPopularity(View view) {
        switchSort.setChecked(false);
    }
    public void onClickTopRated(View view) {
        switchSort.setChecked(true);
    }

    // метод для установки нужных фильмов в зависимости от switch
    private void setMoviesForSwitch(boolean isChecked, int page){
        if (isChecked){
            isCheckedSwitch = true;
            Log.i("My", "setMoviesForSwitch = true");
            // установим в БД (ViewModel) нужный массив с фильмами
            NetworkUtils.getJSONObject(page,NetworkUtils.TOP_RATED, MainActivity.this,viewModel, isLoading, progressBar, lang);

            // установим нужные цвета у текста около Switch
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
            textViewTopRated.setTextColor(getResources().getColor(R.color.teal_200));
        } else {
            isCheckedSwitch = false;
            Log.i("My", "setMoviesForSwitch = false");
            // установим в БД (ViewModel) нужный массив с фильмами
            NetworkUtils.getJSONObject(page,NetworkUtils.POPULARITY, MainActivity.this,viewModel, isLoading, progressBar, lang);

            // установим нужные цвета у текста около Switch
            textViewPopularity.setTextColor(getResources().getColor(R.color.teal_200));
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
        }
    }

}