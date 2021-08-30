package com.demo.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie {

    public FavouriteMovie(int uniqueId, int id, String title, String originalTitle, int voteCount, double voteAverage, String overview, String backdropPath, String posterPath, String bigPosterPath, String releaseDate) {
        super(uniqueId, id, title, originalTitle, voteCount, voteAverage, overview, backdropPath, posterPath, bigPosterPath, releaseDate);
    }

    // 2-ой конструктор для создания FavouriteMovie из Movie
    @Ignore
    public FavouriteMovie(Movie movie){
        super(movie.getUniqueId() ,movie.getId(), movie.getTitle(), movie.getOriginalTitle(), movie.getVoteCount(), movie.getVoteAverage(), movie.getOverview(),
                movie.getBackdropPath(), movie.getPosterPath(), movie.getBigPosterPath(), movie.getReleaseDate());
    }
}
