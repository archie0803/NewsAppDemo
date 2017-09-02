package com.example.android.newsappdemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static android.R.attr.apiKey;

/**
 * Created by Archita on 02-09-2017.
 */

public interface ApiInterface {

    //GET SOURCES
    @GET("sources")
    Call<Source> getSourceList();

    //GET ARTICLE BY SOURCE
    @GET("articles")
    Call<ArticleResponse> getArticleBySource(@Query("source") String source, @Query("sortBy") String sort, @Query("apiKey") String api_key);

    //GET ARTICLE BY SOURCE AND CATEGORY
    @GET("sources")
    Call<ArticleResponse.Article> getArticleByCategory(@Query("category") String category);
}
