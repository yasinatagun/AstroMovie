package com.yasinatagun.astromovie.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieApi {
    @GET("trending/movie/day?api_key=7efd9f231d01b7c27d9779e4eb6fa881")
    Call<PopularMovieResponse> getPopularMovies();

    @GET("movie/{movie_id}?api_key=7efd9f231d01b7c27d9779e4eb6fa881")
    Call<Movie> getMovieWithId(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/credits?api_key=7efd9f231d01b7c27d9779e4eb6fa881")
    Call<MovieCastList> getCastWithMovieId(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/videos?api_key=7efd9f231d01b7c27d9779e4eb6fa881")
    Call<MovieVideos> getMovieVideos(@Path("movie_id") int movie_id);

}
