package com.btech.project.technofeed.network;

import com.btech.project.technofeed.model.ArticleResponse;
import com.btech.project.technofeed.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<NewsResponse> getHeadlines(@Query("sources") String sources,
                                    @Query("apiKey") String apiKey);

    @GET("everything")
    Call<NewsResponse> getSearchResults(@Query("q") String query,
                                           @Query("sortBy") String sortBy,
                                           @Query("language") String language,
                                           @Query("apiKey") String apiKey);

}
