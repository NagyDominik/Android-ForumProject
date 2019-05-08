package com.exam.forumproject.GUI;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

        newPostBtn = findViewById(R.id.fab);
            newPostBtn.setOnClickListener(v ->  {
                    Intent intent = new Intent(ctx, NewPostActivity.class);
                    startActivity(intent);
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
                    Log.d(TAG, "Loading");
                }
                else {

                    Log.d(TAG, "init RecyclerView.");
                    adapter = new RecyclerViewAdapter(ctx, model.getAllForumPost());
                    adapter.setItems(model.getAllForumPost());
                    postListView.setAdapter(adapter);
                    postListView.setLayoutManager(new LinearLayoutManager(ctx));
                }

            }
        });
    }

}
