package com.yasinatagun.astromovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yasinatagun.astromovie.R;
import com.yasinatagun.astromovie.model.Genre;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreHolder> {
    Context context;
    ArrayList<Genre> genreArrayList;

    public GenreAdapter(Context context, ArrayList<Genre> genreArrayList) {
        this.context = context;
        this.genreArrayList = genreArrayList;
    }

    @NonNull
    @Override
    public GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item_genre, parent, false);
        return new GenreHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreHolder holder, int position) {
        Genre genre = genreArrayList.get(position);
        holder.genreTitle.setText(genre.name);
    }

    @Override
    public int getItemCount() {
        return genreArrayList.size();
    }

    public class GenreHolder extends RecyclerView.ViewHolder {
        TextView genreTitle;

        public GenreHolder(@NonNull View itemView) {
            super(itemView);
            genreTitle = itemView.findViewById(R.id.genreTitle);
        }
    }
}
