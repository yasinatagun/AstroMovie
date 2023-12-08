package com.yasinatagun.astromovie.view.mainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.yasinatagun.astromovie.adapter.BookmarkAdapter;
import com.yasinatagun.astromovie.databinding.FragmentBookmarkBinding;
import com.yasinatagun.astromovie.model.MovieApi;

public class BookmarkFragment extends Fragment {
    private FragmentBookmarkBinding binding;
    public  BookmarkAdapter bookmarkAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    ListenerRegistration listenerRegistration;
    MovieApi movieApi;

    public BookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
//                    MainActivity.bookmarkedMovies.add(movie);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookmarkBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = binding.bookmarkRW;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookmarkAdapter = new BookmarkAdapter(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bookmarkAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void goHome(View view){
        NavDirections action = BookmarkFragmentDirections.actionBookmarkFragmentToPopularFragment();
        Navigation.findNavController(view).navigate(action);
    }
}