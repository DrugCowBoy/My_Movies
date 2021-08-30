package com.demo.mymovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// объекты Movie будут храниться в БД
@Entity (tableName = "movies")
public class Movie implements Parcelable {
    // поля класса
    @PrimaryKey(autoGenerate = true)
    private int uniqueId;
    private int id;
    private String title;
    private String originalTitle;
    private int voteCount;
    private double voteAverage;
    private String overview;
    private String backdropPath;
    private String posterPath;
    private String bigPosterPath;
    private String releaseDate;

    protected Movie(Parcel in) {
        uniqueId = in.readInt();
        id = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        voteCount = in.readInt();
        voteAverage = in.readDouble();
        overview = in.readString();
        backdropPath = in.readString();
        posterPath = in.readString();
        bigPosterPath = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uniqueId);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);
        dest.writeString(backdropPath);
        dest.writeString(posterPath);
        dest.writeString(bigPosterPath);
        dest.writeString(releaseDate);
    }

    // конструктор класса
    public Movie(int uniqueId,int id, String title, String originalTitle, int voteCount, double voteAverage, String overview, String backdropPath, String posterPath, String bigPosterPath, String releaseDate) {
        this.uniqueId = uniqueId;
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.releaseDate = releaseDate;
    }

    @Ignore
    public Movie(int id, String title, String originalTitle, int voteCount, double voteAverage, String overview, String backdropPath, String posterPath, String bigPosterPath, String releaseDate) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.releaseDate = releaseDate;
    }


    // геттеры и сеттеры
    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getBigPosterPath() {
        return bigPosterPath;
    }

    public void setBigPosterPath(String bigPosterPath) {
        this.bigPosterPath = bigPosterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
