package com.example.android.newsappdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.newsappdemo.Constants.API_KEY;
import static com.example.android.newsappdemo.Constants.BASE_URL;

/**
 * Created by Archita on 02-09-2017.
 */

public class MainFragment extends Fragment {
    ArrayList<ArticleResponse.Article> articleArrayList;
    ArticleAdapter mArticleAdapter;
    RecyclerView mArticleRecyclerView;
    String sort;

    RetrofitHelper retrofitHelper;
    ApiInterface apiInterface;
    Call<ArticleResponse> articleResponseCall;
    Call<Source> sortResponseCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        retrofitHelper = new RetrofitHelper(BASE_URL);
        apiInterface = retrofitHelper.getAPI();

        articleArrayList = new ArrayList<>();
        mArticleAdapter = new ArticleAdapter(getContext(), articleArrayList);
        mArticleRecyclerView = rootView.findViewById(R.id.main_recycler_view);
        mArticleRecyclerView.setAdapter(mArticleAdapter);
        mArticleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                articleArrayList.get(position).getUrl();
                String url = articleArrayList.get(position).getUrl();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                mArticleAdapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(mArticleRecyclerView);
        String str = "bbc-news";
        changeData(str);
        return rootView;
    }

    public void changeData(String str) {
        if (!str.trim().isEmpty()) {
            getSortBy(str);
        }

    }

    public void getArticle(String str, String sort) {
        articleResponseCall = apiInterface.getArticleBySource(str, sort, API_KEY);
        articleResponseCall.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if (!response.isSuccessful()) {
                    articleResponseCall = call.clone();
                    articleResponseCall.enqueue(this);
                    return;
                }
                articleArrayList.clear();
                if (response.body() == null) return;
                for (ArticleResponse.Article article : response.body().getArticles()) {
                    if (article != null && article.getTitle() != null && article.getUrlToImage() != null && article.getUrl() != null
                            && article.getDescription() != null)
                        articleArrayList.add(article);
                }
                mArticleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });
    }

    public void getSortBy(String str) {
        final String source = str;

        sort = "top";
        sortResponseCall = apiInterface.getSortByOfSource();
        sortResponseCall.enqueue(new Callback<Source>() {
            @Override
            public void onResponse(Call<Source> call, Response<Source> response) {
                if (!response.isSuccessful()) {
                    sortResponseCall = call.clone();
                    sortResponseCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                for (Source.Res res : response.body().getSources()) {
                    if (res.getId().equalsIgnoreCase(source)) {
                        sort = res.getSortBysAvailable().get((res.getSortBysAvailable().size()) - 1);
                        Log.i("TAG", "Here we are" + sort);
                        getArticle(source, sort);
                    }

                }
            }

            @Override
            public void onFailure(Call<Source> call, Throwable t) {
                Log.i("TAG", "Oh no");
            }
        });

    }
}
