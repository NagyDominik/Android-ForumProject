package com.exam.forumproject.GUI;

import android.content.Context;
import android.databinding.ObservableList;
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

import com.bumptech.glide.Glide;
import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private ObservableList<ForumPost> forumPostList;
    private Model model;

    RecyclerViewAdapter(Context context, ObservableList<ForumPost> forumPostList) {
        this.mContext = context;
        this.forumPostList = forumPostList;
        this.model = Model.getInstance(mContext);
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
        holder.btnDelete.setOnClickListener(v -> {
            ForumPost currentPost = forumPostList.get(position);
            model.deletePost(currentPost.getId());
        });
        viewGenerator(holder, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
     * It creates imageview or textview depending on the type of post
     */
    private void viewGenerator(ViewHolder holder, int position) {
        if (holder.constraintLayout.getChildCount() == 0) {
            if (forumPostList.get(position).getDescription() == null) {
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setAdjustViewBounds(true);
                holder.constraintLayout.addView(imageView);
                loadPictures(holder, position);
            } else {
                TextView textView = new TextView(mContext);
                textView.setText(forumPostList.get(position).getDescription());
                holder.constraintLayout.addView(textView);
            }
        } else {
            if (holder.constraintLayout.getChildAt(0) instanceof ImageView) {
                if (((ImageView) holder.constraintLayout.getChildAt(0)).getDrawable() == null)
                    loadPictures(holder, position);
            }
        }
    }

    private void loadPictures(ViewHolder holder, int pos) {
        Glide.with(mContext)
            .asDrawable()
            .load(forumPostList.get(pos).getPictureUrl())
            .into((ImageView) holder.constraintLayout.getChildAt(0));
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
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvDateOfPost = itemView.findViewById(R.id.tvDateOfPost);
            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

}
