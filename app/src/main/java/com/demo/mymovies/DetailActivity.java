package com.demo.mymovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mymovies.adapters.ReviewAdapter;
import com.demo.mymovies.adapters.VideoMovieAdapter;
import com.demo.mymovies.data.FavouriteMovie;
import com.demo.mymovies.data.MainViewModel;
import com.demo.mymovies.data.Movie;
import com.demo.mymovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewFavouriteMovie;
    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewRelease;
    private TextView textViewOverview;

    private TextView textViewLabelReviews;
    private TextView textViewLabelTrailers;

    private Movie movie;
    private int idMovie;

    private MainViewModel viewModel;

    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;

    private RecyclerView recyclerViewVideos;
    private VideoMovieAdapter videoMovieAdapter;

    private String lang;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        lang = Locale.getDefault().getLanguage();

        imageViewFavouriteMovie = findViewById(R.id.imageViewFavouriteMovie);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewRelease = findViewById(R.id.textViewRelease);
        textViewOverview = findViewById(R.id.textViewOverview);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewVideos = findViewById(R.id.recyclerViewVideos);
        textViewLabelReviews = findViewById(R.id.textViewLabelReviews);
        textViewLabelTrailers = findViewById(R.id.textViewLabelTrailers);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // получим интент, который вызвал данную активность, и получим из него фильм, на который нажали
        Intent intent = getIntent();
        if (intent.hasExtra("Movie")){
            movie = intent.getParcelableExtra("Movie");
            idMovie = movie.getId();
            // установим в поля макета значения полученного фильма
            Picasso.get().load(movie.getBigPosterPath()).placeholder(R.drawable.place_holder).into(imageViewBigPoster);
            textViewTitle.setText(movie.getTitle());
            textViewOriginalTitle.setText(movie.getOriginalTitle());
            textViewRating.setText(String.format("%s", movie.getVoteAverage()));
            textViewRelease.setText(movie.getReleaseDate());
            textViewOverview.setText(movie.getOverview());
        } else{
            // иначе закроем данную активность и запустим предыдущую активность
            finish();
        }

        // установим нужный цвет звезды
        setColorStar();

        // создаём адаптер без массива
        videoMovieAdapter = new VideoMovieAdapter();
        reviewAdapter = new ReviewAdapter();
        // получаем из интернета JSON-объект, получаем из него массив с отзывами reviews или массив videos;
        // далее устанавливаем этот массив в адаптер reviewAdapter или videoMovieAdapter, и наконец устанавливаем адаптер в recyclerView
        NetworkUtils.getJSONObjectVideos(idMovie, this, videoMovieAdapter, recyclerViewVideos, lang, textViewLabelTrailers);
        NetworkUtils.getJSONObjectReviews(idMovie, this, reviewAdapter, recyclerViewReviews, lang, textViewLabelReviews);

        // установим слушатель кликов для видео к фильму
        videoMovieAdapter.setOnClickVideoListener(new VideoMovieAdapter.OnClickVideoListener() {
            @Override
            public void onClickVideo(String url) {
                Intent intentToYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToYoutube);
            }
        });


    }

    // этот метод при клике на звезду будет изменять состояние фильма: будет добавляться в избранное или удаляться из избранного
    public void onClickChangeFavouriteMovie(View view) {
        FavouriteMovie favouriteMovie = viewModel.getFavouriteMovieById(idMovie);
        if(favouriteMovie == null){
            viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, R.string.add_to_favourite, Toast.LENGTH_SHORT).show();
            imageViewFavouriteMovie.setImageResource(R.drawable.zvezda_on);
        } else{
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, R.string.delete_from_favourite, Toast.LENGTH_SHORT).show();
            imageViewFavouriteMovie.setImageResource(R.drawable.zvezda);
        }
    }

    // метод для установки цвета звезды при запуске активности
    private void setColorStar(){
        FavouriteMovie favouriteMovie = viewModel.getFavouriteMovieById(idMovie);
        if (favouriteMovie != null){
            imageViewFavouriteMovie.setImageResource(R.drawable.zvezda_on);
        }
    }
}