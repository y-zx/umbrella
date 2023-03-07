package com.example.user.recycleradaptertest;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.RecyclerDelegateDiffAdapter;
import com.yzx.delegate.items.DelegateItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] titles = {"a", "b", "c", "d", "e"};

    public <T extends View> T $(@IdRes int id) {
        return findViewById(id);
    }

    RecyclerDelegateAdapter adapter;
    RecyclerDelegateDiffAdapter diffAdapter;


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
        diffAdapter = new RecyclerDelegateDiffAdapter(this, getDiffBeanItemCallback());
        recyclerView.setAdapter(adapter);

        // 模拟 ViewModel 数据解析
        mutiItemDataSource.addAll(viewModel.getData());

        // 初始化adapter与设置数据
        MainAdapter.initMainAdapter(adapter, titles, mutiItemDataSource);
    }

    @NonNull
    private DiffUtil.ItemCallback<DelegateItem.DiffBean> getDiffBeanItemCallback() {
        return new DiffUtil.ItemCallback<DelegateItem.DiffBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull DelegateItem.DiffBean oldItem, @NonNull DelegateItem.DiffBean newItem) {
                // 布局类型是否一样
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull DelegateItem.DiffBean oldItem, @NonNull DelegateItem.DiffBean newItem) {
                // 数据是否一样
                return oldItem.areContentsTheSame(newItem);
            }
        };
    }

}
