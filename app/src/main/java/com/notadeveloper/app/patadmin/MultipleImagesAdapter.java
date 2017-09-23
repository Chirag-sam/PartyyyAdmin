package com.notadeveloper.app.patadmin;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

/**
 * Created by Chirag on 02-Aug-17.
 */

public class MultipleImagesAdapter extends RecyclerView.Adapter<MultipleImagesAdapter.ImagesViewHolder> {
    ArrayList<Uri> images;
    Context context;

    public MultipleImagesAdapter(ArrayList<Uri> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_image_item, parent, false);
      return new ImagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImagesViewHolder holder, final int position) {
        GlideApp.with(context).load(images.get(position)).into(holder.imageView);
      holder.imageButton.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          images.remove(position);
          notifyDataSetChanged();
        }
      });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }
    class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
      ImageButton imageButton;
        public ImagesViewHolder(View itemView) {
            super(itemView);
          imageView = itemView.findViewById(R.id.picture);
          imageButton = itemView.findViewById(R.id.close);
        }
    }
}
