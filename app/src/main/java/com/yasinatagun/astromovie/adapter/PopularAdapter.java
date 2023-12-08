package com.yasinatagun.astromovie.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.LazilyParsedNumber;
import com.yasinatagun.astromovie.R;
import com.yasinatagun.astromovie.model.Movie;
import com.yasinatagun.astromovie.model.MovieId;
import com.yasinatagun.astromovie.view.MainActivity;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularHolder> {
    private LayoutInflater mInflater;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ItemClickListener mClickListener;
    Context context;
    ArrayList<Movie> popularMovieArrayList;

    public PopularAdapter(Context context, ArrayList<Movie> popularMovieArrayList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.popularMovieArrayList = popularMovieArrayList;
    }

    @NonNull
    @Override
    public PopularHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item_popular, parent, false);
        return new PopularHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularHolder holder, int position) {
        int positionV = position;
        Movie movie = popularMovieArrayList.get(position);
        holder.mName.setText(movie.title);
        holder.mDuration.setText(String.valueOf(movie.runtime / 60) + " h " + String.valueOf(movie.runtime % 60) + " m");
        holder.mImdb.setText(String.format("%.1f", ((LazilyParsedNumber) movie.vote_average).floatValue()));
        holder.mPoster.setImageResource(R.drawable.movie_logo);
        System.out.println("https://image.tmdb.org/t/p/w500/" + movie.poster_path);
        Glide.with(context).load("https://image.tmdb.org/t/p/w500/" + movie.poster_path).into(holder.mPoster);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        holder.mRecyclerView.setLayoutManager(layoutManager);

        GenreAdapter genreAdapter = new GenreAdapter(context, movie.genres);
        System.out.println(movie.genres);
        holder.mRecyclerView.setAdapter(genreAdapter);
        boolean isBookmarked = false;
        for (int i = 0; i < MainActivity.bookmarkedMovies.size(); i++) {
            if (MainActivity.bookmarkedMovies.get(i).id == popularMovieArrayList.get(position).id) {
                holder.mBookmarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_24);
                isBookmarked = true;
                break;
            }
        }
        holder.mBookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = false;
                for (int i = 0; i < MainActivity.bookmarkedMovies.size(); i++) {
                    if (MainActivity.bookmarkedMovies.get(i).id == popularMovieArrayList.get(position).id) {
                        isBookmarked = true;
                        break;
                    }
                }
                if (isBookmarked) {
                    holder.mBookmarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                    Movie movie = popularMovieArrayList.get(positionV);
                    MovieId movieId = new MovieId();
                    movieId.movieId = movie.id;
                    String userEmail = context.getSharedPreferences("default", MODE_PRIVATE).getString("userEmail", "null");
                    firebaseFirestore.collection("users").document(userEmail).collection("movieId").document(String.valueOf(movie.id)).delete();
                } else {
                    holder.mBookmarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    Movie movie = popularMovieArrayList.get(positionV);
                    MovieId movieId = new MovieId();
                    movieId.movieId = movie.id;
                    String userEmail = context.getSharedPreferences("default", MODE_PRIVATE).getString("userEmail", "null");
                    firebaseFirestore.collection("users").document(userEmail).collection("movieId").document(String.valueOf(movie.id)).set(movieId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularMovieArrayList.size();
    }

    public class PopularHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName, mDuration, mImdb;
        ImageView mPoster, mBookmarkIcon;
        RecyclerView mRecyclerView;

        public PopularHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.etMovieName);
            mDuration = itemView.findViewById(R.id.etDuration);
            mImdb = itemView.findViewById(R.id.etImdb);
            mPoster = itemView.findViewById(R.id.imageView);
            mRecyclerView = itemView.findViewById(R.id.genreRW);
            mBookmarkIcon = itemView.findViewById(R.id.bookmarkIconView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
