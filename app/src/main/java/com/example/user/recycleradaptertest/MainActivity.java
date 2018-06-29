package com.example.user.recycleradaptertest;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.holder.ViewHolder;
import com.yzx.delegate.items.CommonItem;
import com.yzx.delegate.items.CommonMultipleItem;
import com.yzx.delegate.items.FixItem;
import com.yzx.delegate.items.FooterItem;

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

        initDelegateAdpter();

    }

    private void initDelegateAdpter() {
        //item的个数 随数据源而定，布局为一种
        CommonItem<String> commonItem = new CommonItem<String>(R.layout.cell_main_recycler_item2) {
            @Override
            protected void convert(ViewHolder holder, int position, int positionAtTotal, String s) {
                holder.setText(R.id.tv_main_recycler_item2, s);
            }
        };
        commonItem.setData(Arrays.asList(titles));

        //item 的个数 随数据源而定，布局为多种
        CommonMultipleItem<Integer> commonMultipleItem = new CommonMultipleItem<>();
        commonMultipleItem.registerMultileChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout) {
            @Override
            protected boolean handleItem(Integer integer) {
                return integer < 7;
            }

            @Override
            protected void convert(ViewHolder holder, int position, int positionAtTotal, Integer integer) {
                holder.setText(R.id.btn, integer + "");

            }
        }).registerMultileChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout2) {
            @Override
            protected boolean handleItem(Integer integer) {
                return integer >= 7;
            }

            @Override
            protected void convert(ViewHolder holder, int position, int positionAtTotal, Integer integer) {
                holder.setText(R.id.btn2, integer + "");
            }
        });
        commonMultipleItem.addData(Arrays.asList(ints));



        FooterItem footerItem = new FooterItem(R.layout.cell_my_footer) {
            int i = 0;
            @Override
            protected void convert(ViewHolder holder) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i++;
                        adapter.setCurrentStatus(i%2);

                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, i%2+"", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public FooterStatusChangedListener setFooterStatusChangedListener() {
                return new FooterStatusChangedListener() {
                    @Override
                    public void loadComplete(ViewHolder holder) {
                        holder.setText(R.id.tv_footer_text, "加载更多")
                                .setViewVisible(R.id.pb_footer_progress, View.GONE);
                    }

                    @Override
                    public void loading(ViewHolder holder) {
                        holder.setText(R.id.tv_footer_text, "正在加载")
                                .setViewVisible(R.id.pb_footer_progress, View.VISIBLE);
                    }

                    @Override
                    public void loadError(ViewHolder holder) {
                        holder.setText(R.id.tv_footer_text, "网络异常")
                                .setViewVisible(R.id.pb_footer_progress, View.GONE);
                    }

                    @Override
                    public void noMore(ViewHolder holder) {
                        holder.setText(R.id.tv_footer_text, "没有更多")
                                .setViewVisible(R.id.pb_footer_progress, View.GONE);
                    }
                };
            }
        };

        adapter.registerItem(new FixItem(R.layout.cell_main_recycler_item, 1)) //固定一个item
                .registerItem(commonItem)
                .registerItem(commonMultipleItem)
                .registerItem(footerItem);

        adapter.notifyDataSetChanged();

        adapter.setDifferentStatus(1);
        adapter.registerItem(commonItem)
                .registerItem(commonMultipleItem)
                .registerItem(new FixItem(R.layout.cell_main_recycler_item, 1))
                .registerItem(footerItem);
    }


}
