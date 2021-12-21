package com.example.user.recycleradaptertest;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.layoutmanager.LayoutHelper;
import com.yzx.delegate.layoutmanager.PositionLayoutHelperFinder;
import com.yzx.delegate.layoutmanager.VirtualLayoutManager;
import com.yzx.delegate.layoutmanager.layout.GridLayoutHelper;
import com.yzx.delegate.layoutmanager.layout.LinearLayoutHelper;

import java.util.ArrayList;
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


        VirtualLayoutManager layoutManager = new VirtualLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        PositionLayoutHelperFinder layoutHelperFinder = new PositionLayoutHelperFinder();
        layoutHelperFinder.setFinderListener(new PositionLayoutHelperFinder.LayoutHelperFinderListener() {
            @Override
            public int getLayoutHelper(int position) {
                return adapter.findLayoutHelperByPosition(position);
            }
        });
        layoutManager.setHelperFinder(layoutHelperFinder);

        adapter = new RecyclerDelegateAdapter(this);
        recyclerView.setAdapter(adapter);

        // 模拟 ViewModel 数据解析
        mutiItemDataSource.addAll(viewModel.getData());

        // 初始化adapter与设置数据
        MainAdapter.initMainAdapter(adapter, titles, mutiItemDataSource, layoutManager);
    }

}
