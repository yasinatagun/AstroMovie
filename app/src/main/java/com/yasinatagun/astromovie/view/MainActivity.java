package com.yasinatagun.astromovie.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.yasinatagun.astromovie.R;
import com.yasinatagun.astromovie.databinding.ActivityMainBinding;
import com.yasinatagun.astromovie.model.ApiClient;
import com.yasinatagun.astromovie.model.Movie;
import com.yasinatagun.astromovie.model.MovieApi;
import com.yasinatagun.astromovie.view.mainFragments.BookmarkFragment;
import com.yasinatagun.astromovie.view.mainFragments.PopularFragment;
import com.yasinatagun.astromovie.view.mainFragments.SingleItemFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    MovieApi movieApi;
    private ActivityMainBinding binding;
    ListenerRegistration listenerRegistration;
    public static ArrayList<Movie> bookmarkedMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseAuth = FirebaseAuth.getInstance();


        firebaseFirestore = FirebaseFirestore.getInstance();
        movieApi = ApiClient.getClient().create(MovieApi.class);

        replaceFragment(new PopularFragment());

        getSupportActionBar().hide();

        binding.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        replaceFragment(new PopularFragment());
                        break;
                    case R.id.bookmark:
                        replaceFragment(new BookmarkFragment());
                        break;
                }

                return true;
            }
        });
        getMovieIds();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myFrames, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void finish() {
        listenerRegistration.remove();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        Fragment activeFragment = null;
        for (Fragment f : fragments) {
            if ((f != null) && f.isVisible()) {
                activeFragment = f;
                break;
            }
        }
        System.out.println("0000000");
        System.out.println(activeFragment.getClass().toString());
        if (activeFragment instanceof SingleItemFragment) {
            System.out.println("aaaaa");
            replaceFragment(new PopularFragment());
        } else if (activeFragment instanceof BookmarkFragment) {
            System.out.println("bbbbbb");
            replaceFragment(new PopularFragment());
        } else {
            System.out.println("cccccccc");
            super.finish();
        }

    }

    public void getMovieIds() {
        String userEmail = this.getSharedPreferences("default", MODE_PRIVATE).getString("userEmail", "null");
        CollectionReference collectionReference = firebaseFirestore.collection("users").document(userEmail).collection("movieId");
        MainActivity.bookmarkedMovies.clear();
        listenerRegistration = collectionReference.addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if (value != null) {
                    for (DocumentChange snapshot : value.getDocumentChanges()) {

                        if (snapshot.getType() == DocumentChange.Type.ADDED) {
                            Map<String, Object> data = snapshot.getDocument().getData();
                            int id = ((Long) data.get("movieId")).intValue();
                            getMovieWithId(id);
                        } else if (snapshot.getType() == DocumentChange.Type.REMOVED) {
                            Map<String, Object> data = snapshot.getDocument().getData();

                            int id = ((Long) data.get("movieId")).intValue();
                            for (int i = 0; i < MainActivity.bookmarkedMovies.size(); i++) {
                                if (MainActivity.bookmarkedMovies.get(i).id == id) {
                                    MainActivity.bookmarkedMovies.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    Fragment activeFragment = null;
                    for (Fragment f : fragments) {
                        if ((f != null) && f.isVisible()) {
                            activeFragment = f;
                            break;
                        }
                    }

                    if (activeFragment instanceof BookmarkFragment) {
                        ((BookmarkFragment) activeFragment).bookmarkAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    public void getMovieWithId(int id) {
        movieApi.getMovieWithId(id).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                MainActivity.bookmarkedMovies.add(movie);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    public void menuIconClicked(View view) {
        View view1 = (View) binding.topMenu.getRoot();
        view1.setVisibility(View.VISIBLE);
    }


    public void cancelIconClicked(View view) {
        View view1 = (View) binding.topMenu.getRoot();
        view1.setVisibility(View.INVISIBLE);
    }

    public void logoutClicked(View view) {
        firebaseAuth.signOut();
        Toast.makeText(MainActivity.this, "LOGOUT SUCCESSFULLY", Toast.LENGTH_LONG).show();
        Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intentLogout);
        finish();
    }


}