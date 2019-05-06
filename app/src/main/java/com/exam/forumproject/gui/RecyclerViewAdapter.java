package com.exam.forumproject.gui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exam.forumproject.R;
import com.exam.forumproject.be.ForumPost;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context context;
    private List<ForumPost> forumPostList;

    RecyclerViewAdapter(Context context, List<ForumPost> forumPostList) {
        this.context = context;
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
     * @param friends New list of items
     */
    void setItems(List<ForumPost> friends) {
        this.forumPostList = friends;
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);

        }
    }

}
