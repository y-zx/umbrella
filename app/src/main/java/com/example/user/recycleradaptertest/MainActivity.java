package com.example.user.recycleradaptertest;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yzx.delegate.RecyclerDelegateAdapter;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] titles = {"a", "b", "c", "d", "e"};
    Integer[] ints = {5, 8, 4, 9, 0};

    public <T extends View> T $(@IdRes int id) {
        return findViewById(id);
    }

    RecyclerDelegateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = $(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        //通用适配器
         adapter = new RecyclerDelegateAdapter(this);
        recyclerView.setAdapter(adapter);



    }


}
