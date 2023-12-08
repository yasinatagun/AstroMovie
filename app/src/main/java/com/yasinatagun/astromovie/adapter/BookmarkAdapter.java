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

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkHolder> {
    private LayoutInflater mInflater;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    Context context;

    public BookmarkAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item_popular, parent, false);
        return new BookmarkHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkHolder holder, int position) {
        Movie movie = MainActivity.bookmarkedMovies.get(position);
        holder.mName.setText(movie.title);
        holder.mDuration.setText(String.valueOf(movie.runtime / 60) + " h " + String.valueOf(movie.runtime % 60) + " m");
        holder.mImdb.setText(String.format("%.1f", ((LazilyParsedNumber) movie.vote_average).floatValue()));
        holder.mPoster.setImageResource(R.drawable.movie_logo);
        Glide.with(context).load("https://image.tmdb.org/t/p/w500/" + movie.poster_path).into(holder.mPoster);
        holder.mBookmarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_24);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        holder.mRecyclerView.setLayoutManager(layoutManager);
        GenreAdapter genreAdapter = new GenreAdapter(context, movie.genres);
        System.out.println(movie.genres);
        holder.mRecyclerView.setAdapter(genreAdapter);
        int positionV = position;
        holder.mBookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = MainActivity.bookmarkedMovies.get(positionV);
                MovieId movieId = new MovieId();
                movieId.movieId = movie.id;
                String userEmail = context.getSharedPreferences("default", MODE_PRIVATE).getString("userEmail", "null");
                firebaseFirestore.collection("users").document(userEmail).collection("movieId").document(String.valueOf(movie.id)).delete();
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainActivity.bookmarkedMovies.size();
    }

    public class BookmarkHolder extends RecyclerView.ViewHolder {
        TextView mName, mDuration, mImdb;
        ImageView mPoster, mBookmarkIcon;
        RecyclerView mRecyclerView;

        public BookmarkHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.etMovieName);
            mDuration = itemView.findViewById(R.id.etDuration);
            mImdb = itemView.findViewById(R.id.etImdb);
            mPoster = itemView.findViewById(R.id.imageView);
            mRecyclerView = itemView.findViewById(R.id.genreRW);
            mBookmarkIcon = itemView.findViewById(R.id.bookmarkIconView);
        }
    }
}
