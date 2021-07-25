package com.example.user.recycleradaptertest;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.user.recycleradaptertest.bean.MainBean;
import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.holder.ViewHolder;
import com.yzx.delegate.items.CommonItem;
import com.yzx.delegate.items.CommonMultipleItem;
import com.yzx.delegate.items.FixItem;
import com.yzx.delegate.items.FooterItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] titles = {"a", "b", "c", "d", "e"};

    public <T extends View> T $(@IdRes int id) {
        return findViewById(id);
    }

    RecyclerDelegateAdapter adapter;


    public List<Object> mutiItemDataSource = new ArrayList<>();
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new MainViewModel();

        //初始化recyclerView
        recyclerView = $(R.id.recycler);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        GridLayoutManager.SpanSizeLookup lookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getSpanSize(position);
            }
        };
        manager.setSpanSizeLookup(lookup);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerDelegateAdapter(this);
        recyclerView.setAdapter(adapter);

        // 模拟 ViewModel 数据解析
        mutiItemDataSource.addAll(viewModel.getData());

        // 初始化adapter与设置数据
        MainAdapter.initMainAdapter(adapter, titles, mutiItemDataSource);
    }

}
