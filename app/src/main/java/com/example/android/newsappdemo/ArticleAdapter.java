package com.example.android.newsappdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Archita on 03-09-2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    Context mContext;
    ArrayList<ArticleResponse.Article> mArticleArrayList;

    public ArticleAdapter(Context context, ArrayList<ArticleResponse.Article> articleArrayList) {
        mContext = context;
        mArticleArrayList = articleArrayList;
    }

    @Override
    public ArticleAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.article_item_view, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Glide.with(mContext.getApplicationContext()).load(mArticleArrayList.get(position).getUrlToImage())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.articleImage);
        holder.title.setText(mArticleArrayList.get(position).getTitle());
        holder.description.setText(mArticleArrayList.get(position).getDescription());
        holder.author.setText(mArticleArrayList.get(position).getAuthor());
        holder.publishTime.setText(mArticleArrayList.get(position).getPublishedAt());
    }

    @Override
    public int getItemCount() {
        return mArticleArrayList.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_article)
        public CardView articleCardView;
        @BindView(R.id.image_view_article)
        public ImageView articleImage;
        @BindView(R.id.text_view_author)
        public TextView author;
        @BindView(R.id.text_view_title)
        public TextView title;
        @BindView(R.id.text_view_description)
        public TextView description;
        @BindView(R.id.text_view_publishTime)
        public TextView publishTime;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            articleImage.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels);
            articleImage.getLayoutParams().height = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.5);
            articleCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mArticleArrayList.get(getAdapterPosition()).getUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            });
        }
    }
}
