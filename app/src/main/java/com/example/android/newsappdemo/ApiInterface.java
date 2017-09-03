package com.example.android.newsappdemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Archita on 02-09-2017.
 */

public interface ApiInterface {

    //GET ARTICLE BY SOURCE
    @GET("articles")
    Call<ArticleResponse> getArticleBySource(@Query("source") String source, @Query("sortBy") String sort, @Query("apiKey") String api_key);

    //GET SORTS FOR SOURCE
    @GET("sources")
    Call<Source> getSortByOfSource();

    //GET SOURCE BY CATEGORY
    @GET("sources")
    Call<Source> getSourceByCategory(@Query("category") String category);
}
