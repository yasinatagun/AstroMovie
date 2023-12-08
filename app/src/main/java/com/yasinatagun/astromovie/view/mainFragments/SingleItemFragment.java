package com.yasinatagun.astromovie.view.mainFragments;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Math.min;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yasinatagun.astromovie.R;
import com.yasinatagun.astromovie.adapter.GenreAdapter;
import com.yasinatagun.astromovie.adapter.PopularAdapter;
import com.yasinatagun.astromovie.databinding.FragmentSingleItemBinding;
import com.yasinatagun.astromovie.model.ApiClient;
import com.yasinatagun.astromovie.model.Cast;
import com.yasinatagun.astromovie.model.Genre;
import com.yasinatagun.astromovie.model.Movie;
import com.yasinatagun.astromovie.model.MovieApi;
import com.yasinatagun.astromovie.model.MovieCastList;
import com.yasinatagun.astromovie.model.MovieId;
import com.yasinatagun.astromovie.model.MovieVideos;
import com.yasinatagun.astromovie.model.Video;
import com.yasinatagun.astromovie.view.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleItemFragment extends Fragment {

    public SingleItemFragment(Movie movie) {
        this.movie = movie;
    }
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FragmentSingleItemBinding binding;
    ArrayList<Video> castArrayList;
    private Movie movie;
    GenreAdapter genreAdapter;
    RecyclerView recyclerView;

    public SingleItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCast();
        getVideos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSingleItemBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = binding.singleGenreRW;
        recyclerView.setLayoutManager(layoutManager);
        genreAdapter = new GenreAdapter(getContext(), movie.genres);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(genreAdapter);

//        MediaPlayer mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource("https://www.youtube.com/embed/" + movie.videoId.key);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.singleTitle.setText(movie.title);
        binding.movieDescription.setText(movie.overview);
        Glide.with(getContext()).load("https://image.tmdb.org/t/p/w500/" + movie.poster_path).into(binding.movieTrailerImage);
        binding.movieLanguage.setText(movie.original_language);
        binding.movieDuration.setText(String.valueOf(movie.runtime / 60) + " h " + String.valueOf(movie.runtime % 60) + " m");
        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonClicked(v);
            }
        });
        getCast();
        getVideos();
        for (int i = 0; i < MainActivity.bookmarkedMovies.size(); i++) {
            if (MainActivity.bookmarkedMovies.get(i).id == movie.id) {
                binding.bookmarkImage.setImageResource(R.drawable.ic_baseline_bookmark_24);
                break;
            }
        }

        binding.bookmarkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = false;
                for (int i = 0; i < MainActivity.bookmarkedMovies.size(); i++) {
                    if (MainActivity.bookmarkedMovies.get(i).id == movie.id) {
                        isBookmarked = true;
                        break;
                    }
                }
                if (isBookmarked) {
                    binding.bookmarkImage.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                    MovieId movieId = new MovieId();
                    movieId.movieId = movie.id;
                    String userEmail = getContext().getSharedPreferences("default", MODE_PRIVATE).getString("userEmail", "null");
                    firebaseFirestore.collection("users").document(userEmail).collection("movieId").document(String.valueOf(movie.id)).delete();
                } else {
                    binding.bookmarkImage.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    MovieId movieId = new MovieId();
                    movieId.movieId = movie.id;
                    String userEmail = getContext().getSharedPreferences("default", MODE_PRIVATE).getString("userEmail", "null");
                    firebaseFirestore.collection("users").document(userEmail).collection("movieId").document(String.valueOf(movie.id)).set(movieId);
                }
            }
        });

    }

    public void getCast() {
        MovieApi movieApi = ApiClient.getClient().create(MovieApi.class);
        movieApi.getCastWithMovieId(movie.id).enqueue(new Callback<MovieCastList>() {
            @Override
            public void onResponse(Call<MovieCastList> call, Response<MovieCastList> response) {
                MovieCastList castList = response.body();
//                movie.casts = new ArrayList<>();
                movie.casts = castList.cast;

                ArrayList<Cast> castArrayList = new ArrayList<>();
                for (int i = 0; i < min(4, castList.cast.size()); i++) {
                    String viewId = "cast" + (i + 1);
//                    LinearLayout castLayout = (LinearLayout) getView().findViewById(getResources().getIdentifier(viewId, "id", getContext().getPackageName()));
//                    binding.cast1.getRoot().set
                    castArrayList.add(movie.casts.get(i));
                }


                // IF ATMAM LAZIM!!!!!!!!!!
                binding.cast1.castName.setText(castArrayList.get(0).name);
                Glide.with(getContext()).load("https://image.tmdb.org/t/p/w500/" + castArrayList.get(0).profile_path).into(binding.cast1.castImage);
                binding.cast2.castName.setText(castArrayList.get(1).name);
                Glide.with(getContext()).load("https://image.tmdb.org/t/p/w500/" + castArrayList.get(1).profile_path).into(binding.cast2.castImage);
                binding.cast3.castName.setText(castArrayList.get(2).name);
                Glide.with(getContext()).load("https://image.tmdb.org/t/p/w500/" + castArrayList.get(2).profile_path).into(binding.cast3.castImage);
                binding.cast4.castName.setText(castArrayList.get(3).name);
                Glide.with(getContext()).load("https://image.tmdb.org/t/p/w500/" + castArrayList.get(3).profile_path).into(binding.cast4.castImage);

            }

            @Override
            public void onFailure(Call<MovieCastList> call, Throwable t) {
            }
        });
    }

    public void getVideos() {
        MovieApi movieApi = ApiClient.getClient().create(MovieApi.class);
        movieApi.getMovieVideos(movie.id).enqueue(new Callback<MovieVideos>() {
            @Override
            public void onResponse(Call<MovieVideos> call, Response<MovieVideos> response) {
                MovieVideos videos = response.body();
                Video trailer = null;
                if (videos.results != null) {
                    for (int i = 0; i < videos.results.size(); i++) {
                        Video video = videos.results.get(i);
                        if (video.type.equals("Trailer") && video.site.equals("YouTube")) {
                            trailer = video;
                            break;
                        }
                    }
                } else {
                }
                if (trailer != null) {
                    movie.videoId = trailer;

                }
            }

            @Override
            public void onFailure(Call<MovieVideos> call, Throwable t) {
            }
        });
    }

    public void playButtonClicked(View view){
        if (movie.videoId != null){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movie.videoId.key));
            startActivity(intent);
        }
    }
}