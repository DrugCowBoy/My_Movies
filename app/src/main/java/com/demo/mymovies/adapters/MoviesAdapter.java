package com.demo.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mymovies.R;
import com.demo.mymovies.data.Movie;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;// поле адаптера - массив с фильмами
    private OnPosterClickListener onPosterClickListener;

    // конструктор адаптера будет просто создавать пустой массив movies; для установки массива в адаптер будем использовать сеттер
    public MoviesAdapter(){
        movies = new ArrayList<>();
    }

    // геттер для получения массива movies из адаптера
    public List<Movie> getMovies() {
        return movies;
    }

    // сеттер для установки массива movies в адаптер
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();// нужно оповестить адаптер о том, что массив movies изменился
    }

    // метод для добавления массива в адаптер, при этом мы не удаляем старый
    public void addMovies(List<Movie> movies){
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    // интерфейс для слушателя по нажатию на элемент списка
    public interface OnPosterClickListener{
        void onPosterClick(int position);
    }
    // сеттер для onPosterClickListener
    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent,false);// получим view из макета movie_item
        return new MovieViewHolder(view);// возвращаем созданный ViewHolder с параметром view
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MoviesAdapter.MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        // загружаем картинку с помощью библиотеки Picasso; указываем адрес, откуда скачиваем картинку, и место в макете, куда устанавливаем её
        Picasso.get().load(movie.getPosterPath()).into(holder.imageViewMovie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // класс для ViewHolder
    public class MovieViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewMovie;// единственное поле во ViewHolder - маленький постер с фильмом

        public MovieViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            imageViewMovie = itemView.findViewById(R.id.imageViewMovie);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPosterClickListener != null){
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
