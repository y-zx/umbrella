package com.example.user.recycleradaptertest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.RecyclerDelegateDiffAdapter;
import com.yzx.delegate.items.DelegateItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] titles = {"a", "b", "c", "d", "e", "y", "x", "z", "c", "e", "f", "g"};


    public <T extends View> T $(@IdRes int id) {
        return findViewById(id);
    }

    RecyclerDelegateAdapter adapter;

    public List<Object> mutiItemDataSource = new ArrayList<>();

    MainViewModel viewModel;
    MainAdapter mainAdapter;
    List<Object> origal;

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
//        adapter = new RecyclerDelegateAdapter(this);
        adapter = new RecyclerDelegateDiffAdapter(this);
        recyclerView.setAdapter(adapter);
        origal = viewModel.getData();
        // 模拟 ViewModel 数据解析
        mutiItemDataSource.addAll(origal);


        mainAdapter = new MainAdapter();
        // 初始化adapter与设置数据
        mainAdapter.initMainAdapter(adapter, titles, mutiItemDataSource);

        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainAdapter.setData(randomData(), randomMutipleData());
            }
        });
    }

    public List<String> randomData() {
        List<String> s = new ArrayList<>();
        double random1 = Math.random();
        int listSize = (int) (random1 * 100);
        for (int i = 0; i < listSize; i++) {
            s.add(Math.random() + "^" + i);
        }
        Log.d("DiffUU", "随机 title 长度为" + listSize + "\n" + s);
        return s;
    }

    public List<Object> randomMutipleData() {
        double random = Math.random();
        // 最大为 3倍率原始数据
        int randomSize = (int) (random * origal.size() * 3);

        List<Object> s = new ArrayList<>();
        for (int i = 0; i < randomSize; i++) {
            double a = Math.random();
            int index = (int) (a * origal.size());
            s.add(origal.get(index));
        }
        Log.d("DiffUU", "随机 MutipleData 长度为" + randomSize + "\n" + s);
        return s;
    }


    @NonNull
    private DiffUtil.ItemCallback<DelegateItem.DiffBean> getDiffBeanItemCallback() {
        return new DiffUtil.ItemCallback<DelegateItem.DiffBean>() {
            Gson gson = new Gson();

            @Override
            public boolean areItemsTheSame(@NonNull DelegateItem.DiffBean oldItem, @NonNull DelegateItem.DiffBean newItem) {
                boolean b = oldItem.areItemsTheSame(newItem);
                Log.d("DiffUU", String.format("areItemsTheSame\nold=%1s,%2s\nnew=%3s,%4s\nsame=%5s",
                        mainAdapter.getLayoutStringInfo(oldItem.layoutResId),
                        gson.toJson(oldItem),
                        mainAdapter.getLayoutStringInfo(newItem.layoutResId),
                        gson.toJson(newItem),
                        b));
                // 布局类型是否一样
                return b;
            }

            @Override
            public boolean areContentsTheSame(@NonNull DelegateItem.DiffBean oldItem, @NonNull DelegateItem.DiffBean newItem) {
                boolean b = oldItem.areContentsTheSame(newItem);
                Log.d("DiffUU", String.format("areContentsTheSame\nold=%1s,%2s\nnew=%3s,%4s\nsame=%5s",
                        mainAdapter.getLayoutStringInfo(oldItem.layoutResId),
                        gson.toJson(oldItem),
                        mainAdapter.getLayoutStringInfo(newItem.layoutResId),
                        gson.toJson(newItem),
                        b));

                // 数据是否一样
                return b;
            }
        };
    }

}
