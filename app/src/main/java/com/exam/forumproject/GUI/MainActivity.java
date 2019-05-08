package com.exam.forumproject.GUI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    RecyclerViewAdapter adapter;
    public Context ctx;
    RecyclerView postListView;
    FloatingActionButton newPostBtn;
    private Model model;
    private ObservableBoolean isLoading;
    private ObservableBoolean isPictureLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ctx = this.getBaseContext();

        model = Model.getInstance(ctx);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

            newPostBtn = fab;
            newPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, NewPostActivity.class);
                    startActivity(intent);
                }
            });
        });
        postListView = findViewById(R.id.recyclerView);
        loadingContents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadingContents(){
        model.getIsLoading().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ObservableBoolean temp = (ObservableBoolean) sender;
                if(temp.get()){

                }
                else {
                    Log.d(TAG, "initRecyclerView: init RecyclerView.");
                    adapter = new RecyclerViewAdapter(ctx, model.getAllForumPost());
                    adapter.setItems(model.getAllForumPost());
                    postListView.setAdapter(adapter);
                    postListView.setLayoutManager(new LinearLayoutManager(ctx));
                }

            }
        });
    }

}
