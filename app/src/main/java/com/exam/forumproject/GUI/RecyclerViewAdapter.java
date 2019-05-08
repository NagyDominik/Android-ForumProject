package com.exam.forumproject.GUI;


import android.content.Context;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private ObservableList<ForumPost> forumPostList;

    RecyclerViewAdapter(Context context, ObservableList<ForumPost> forumPostList) {
        this.mContext = context;
        this.forumPostList = forumPostList;
    }

    /**
     * This method is responsible for inflating the view.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the inflated view's nodes with the connecting values
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.tvDateOfPost.setText(forumPostList.get(position).getPostDate());
        holder.tvPostTitle.setText(forumPostList.get(position).getTitle());
        viewGenerator(holder,position);
    }

    /**
     *It creates imageview or textview depends on the type of post
     *
     */
    public void viewGenerator(ViewHolder holder, int position){

        if(forumPostList.get(position).getPicture() != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(forumPostList.get(position).getPicture(), 0, forumPostList.get(position).getPicture().length);
            ImageView imageView = new ImageView(mContext);
            imageView.setImageBitmap(bmp);
            imageView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.constraintLayout.addView(imageView);
        }
        else {
            TextView textView = new TextView(mContext);
            textView.setText(forumPostList.get(position).getDescription());
            holder.constraintLayout.addView(textView);
        }
    }


    /**
     * Returns the item count of the adapter.
     */
    @Override
    public int getItemCount() {
        return forumPostList.size();
    }

    /**
     * Sets the item list of the RecyclerView
     *
     * @param posts New list of items
     */
    void setItems(ObservableList<ForumPost> posts) {
        this.forumPostList = posts;
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDateOfPost;
        TextView tvPostTitle;
        ConstraintLayout constraintLayout;
        ConstraintLayout parentLayout;
        ImageButton btnLike;
        ImageButton btnShare;

        ViewHolder(View itemView) {
            super(itemView);
            tvDateOfPost = itemView.findViewById(R.id.tvDateOfPost);
            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnShare = itemView.findViewById(R.id.btnShare);
        }
    }

}
