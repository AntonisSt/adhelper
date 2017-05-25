package cellock.com.testingapp.ViewHolders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cellock.com.testingapp.Activities.NewsDetailsActivity;
import cellock.com.testingapp.Models.SportsNew;
import cellock.com.testingapp.R;

/**
 * Created by AntonisS on 4/3/2017.
 */

public class NewsViewHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView title;

    public NewsViewHolder(final View itemView, final List<SportsNew> news) {
        super(itemView);

        image = (ImageView) itemView.findViewById(R.id.iv_new);
        title = (TextView) itemView.findViewById(R.id.tv_new);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), NewsDetailsActivity.class);
                intent.putExtra("title", news.get(getAdapterPosition()).getName());
                intent.putExtra("description", news.get(getAdapterPosition()).getDescription());
                intent.putExtra("image", news.get(getAdapterPosition()).getImageUrl());
                itemView.getContext().startActivity(intent);
            }
        });
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setImage(String url) {
        Glide.with(image.getContext())
                .load(url)
                .into(image);
    }
}
