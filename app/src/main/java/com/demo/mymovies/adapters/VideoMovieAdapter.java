package com.demo.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mymovies.R;
import com.demo.mymovies.videos_and_reviews.VideoMovie;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

// адаптер для VideoMovie
public class VideoMovieAdapter extends RecyclerView.Adapter<VideoMovieAdapter.VideoViewHolder> {

    private ArrayList<VideoMovie> videos;
    private OnClickVideoListener onClickVideoListener;

    public VideoMovieAdapter() {
    }

    public void setVideos(ArrayList<VideoMovie> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public interface OnClickVideoListener{
        void onClickVideo(String url);
    }

    public void setOnClickVideoListener(OnClickVideoListener onClickVideoListener) {
        this.onClickVideoListener = onClickVideoListener;
    }

    @NonNull
    @NotNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VideoMovieAdapter.VideoViewHolder holder, int position) {
        VideoMovie video = videos.get(position);
        holder.textViewNameOfVideo.setText(video.getName());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNameOfVideo;

        public VideoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewNameOfVideo = itemView.findViewById(R.id.textViewNameOfVideo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickVideoListener != null){
                        VideoMovie video = videos.get(getAdapterPosition());
                        onClickVideoListener.onClickVideo(video.getKey());
                    }
                }
            });
        }
    }
}
