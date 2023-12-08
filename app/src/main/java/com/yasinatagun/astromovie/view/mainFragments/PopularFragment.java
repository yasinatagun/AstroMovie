package com.yasinatagun.astromovie.view.mainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.yasinatagun.astromovie.R;
import com.yasinatagun.astromovie.adapter.PopularAdapter;
import com.yasinatagun.astromovie.databinding.FragmentPopularBinding;
import com.yasinatagun.astromovie.model.ApiClient;
import com.yasinatagun.astromovie.model.Genre;
import com.yasinatagun.astromovie.model.Movie;
import com.yasinatagun.astromovie.model.MovieApi;
import com.yasinatagun.astromovie.model.PopularMovieResponse;
import com.yasinatagun.astromovie.view.MainActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PopularFragment extends Fragment implements PopularAdapter.ItemClickListener{
    ArrayList<Movie> popularMovieArrayList;
    private FragmentPopularBinding binding;
    Call<PopularMovieResponse> callGetPopularMovies;
    PopularAdapter popularAdapter;
    RecyclerView recyclerView;
    MovieApi movieApi;
    ArrayList<Genre> genreArrayList;
    FirebaseFirestore firebaseFirestore;

    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popularMovieArrayList = new ArrayList<>();
        genreArrayList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onItemClick(View view, int position) {
        Movie movie = popularMovieArrayList.get(position);
//        Navigation.findNavController(view).navigate((NavDirections) PopularFragmentDirections.actionPopularFragmentToSingleItemFragment(movie));
        FragmentManager fragmentManager = ((MainActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myFrames, new SingleItemFragment(movie));
        fragmentTransaction.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPopularBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        movieApi = ApiClient.getClient().create(MovieApi.class);
        callGetPopularMovies = movieApi.getPopularMovies();

        recyclerView = binding.popularRW;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        popularAdapter = new PopularAdapter(getContext(), popularMovieArrayList);
        popularAdapter.setClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(popularAdapter);
        getPopularMovies();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void getPopularMovies() {
        callGetPopularMovies.enqueue(new Callback<PopularMovieResponse>() {

            @Override
            public void onResponse(Call<PopularMovieResponse> call, Response<PopularMovieResponse> response) {
                PopularMovieResponse popularMovies = response.body();
                for (Movie movie : popularMovies.results) {
                    getMovieWithId(movie.id);
                }
            }
            @Override
            public void onFailure(Call<PopularMovieResponse> call, Throwable t) {

            }
        });
    }

    public void getMovieWithId(int id) {
        movieApi.getMovieWithId(id).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                popularMovieArrayList.add(movie);
                popularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }



}