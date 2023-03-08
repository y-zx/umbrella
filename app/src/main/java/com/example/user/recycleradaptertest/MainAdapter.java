package com.example.user.recycleradaptertest;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.user.recycleradaptertest.bean.MainBean;
import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.holder.ViewHolder;
import com.yzx.delegate.items.CommonItem;
import com.yzx.delegate.items.CommonMultipleItem;
import com.yzx.delegate.items.FixItem;
import com.yzx.delegate.items.FooterItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author: yangzhenxiang@km.com
 * @data: 2020-06-29 21:07
 * @descripton:
 * @version:
 */
public class MainAdapter {

    RecyclerDelegateAdapter adapter;
    CommonItem<String> commonItem;
    CommonMultipleItem<Object> commonMultipleItem;
    HashMap<Integer, String> layoutMap = new HashMap<>();

    public String getLayoutStringInfo(int layoutResId){
        return layoutMap.get(layoutResId);
    }

    public void setData(List<String> titles, List<Object> mutiItemDataSource){
        commonMultipleItem.setData(mutiItemDataSource);
        commonItem.setData(titles);
        adapter.submitList();
    }

    public void initMainAdapter(RecyclerDelegateAdapter adapter, String[] titles, List<Object> mutiItemDataSource) {
        layoutMap.put(R.layout.cell_main_recycler_item2, "R.layout.cell_main_recycler_item2: CommonItem: 信息流");
        layoutMap.put(R.layout.cell_my_layout, "R.layout.cell_my_layout: MultipleChildItem: 图+文+标签+2列");
        layoutMap.put(R.layout.cell_my_layout5, "R.layout.cell_my_layout5: MultipleChildItem: 文+标签+2列");
        layoutMap.put(R.layout.cell_my_layout4, "R.layout.cell_my_layout4: MultipleChildItem: 标题");
        layoutMap.put(R.layout.cell_my_footer, "R.layout.cell_my_footer: Footer: 加载更多");
        layoutMap.put(R.layout.cell_main_recycler_item, "R.layout.cell_main_recycler_item: Fix: banner");
        this.adapter = adapter;
        //item的个数 随数据源而定，布局为一种
        commonItem = new CommonItem<String>(R.layout.cell_main_recycler_item2, 2) {
            @Override
            protected void convert(ViewHolder holder, final int position, int positionAtTotal, String s) {
                holder.setText(R.id.book_hot, String.format("最热榜榜%s", position + 1))
                        .setText(R.id.tv_sub_recycler_item2, s);
                holder.setOnViewTachStatusListener(new ViewHolder.OnViewTachStatusListener() {
                    @Override
                    public void onViewAttachedToWindow() {

                    }

                    @Override
                    public void onViewDetachedFromWindow() {

                    }
                });
            }

            @Override
            public long getItemId(int i) {
                Log.d("MainAdapter", "item2 getItemId = " + (100 + i));
                return 100 + i;
            }
        };
        commonItem.setData(Arrays.asList(titles));

        //item 的个数 随数据源而定，布局为多种
        commonMultipleItem = new CommonMultipleItem<>();
        commonMultipleItem.registerMultipleChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout, 1) {
            @Override
            protected boolean handleItem(Object object) {
                return object instanceof MainBean.Book && "1".equals(((MainBean.Book) object).section_type);
            }

            @Override
            protected void convert(ViewHolder holder, int position, int positionAtTotal, Object object) {
                MainBean.Book book = (MainBean.Book) object;
                holder.setText(R.id.book_title, book.title)
                        .setText(R.id.book_tag, book.tag)
                        .setViewVisible(R.id.book_tag, TextUtils.isEmpty(book.tag) ? View.GONE : View.VISIBLE);
                if (book.TYPE_LEFT.equals(book.type)) {
                    // 左边
                    holder.itemView.setPadding(getContext().getResources().getDimensionPixelOffset(R.dimen.dp_12)
                            , holder.itemView.getPaddingTop(),
                            0,
                            holder.itemView.getPaddingBottom());
                } else {
                    // 右边
                    holder.itemView.setPadding(0
                            , holder.itemView.getPaddingTop(),
                            getContext().getResources().getDimensionPixelOffset(R.dimen.dp_12),
                            holder.itemView.getPaddingBottom());
                }

            }

            @Override
            public long getItemId(int i) {
                return 200 + i;
            }
        }).registerMultipleChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout5, 1) {
            @Override
            protected boolean handleItem(Object object) {
                return object instanceof MainBean.Book && "2".equals(((MainBean.Book) object).section_type);
            }

            @Override
            protected void convert(ViewHolder holder, int position, int positionAtTotal, Object object) {
                MainBean.Book book = (MainBean.Book) object;
                holder.setText(R.id.book_title, book.title)
                        .setText(R.id.book_tag, book.tag)
                        .setViewVisible(R.id.book_tag, TextUtils.isEmpty(book.tag) ? View.GONE : View.VISIBLE);
                if (book.TYPE_LEFT.equals(book.type)) {
                    // 左边
                    holder.itemView.setPadding(getContext().getResources().getDimensionPixelOffset(R.dimen.dp_12)
                            , holder.itemView.getPaddingTop(),
                            0,
                            book.lastOne ? getContext().getResources().getDimensionPixelOffset(R.dimen.dp_12) :
                                    getContext().getResources().getDimensionPixelOffset(R.dimen.dp_4));
                } else {
                    // 右边
                    holder.itemView.setPadding(0
                            , holder.itemView.getPaddingTop(),
                            getContext().getResources().getDimensionPixelOffset(R.dimen.dp_12),
                            book.lastOne ? getContext().getResources().getDimensionPixelOffset(R.dimen.dp_12) :
                                    getContext().getResources().getDimensionPixelOffset(R.dimen.dp_4));
                }
            }

            @Override
            public long getItemId(int i) {
                Log.d("MainAdapter", "my_layout5 getItemId = " + (300 + i));
                return 300 + i;
            }
        }).registerMultipleChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout4, 2) {
            @Override
            protected boolean handleItem(Object object) {
                return object instanceof MainBean.SectionTitle;
            }

            @Override
            protected void convert(ViewHolder holder, int position, int positionAtTotal, Object object) {
                MainBean.SectionTitle title = (MainBean.SectionTitle) object;
                holder.setText(R.id.section_title, title.title);
            }

            @Override
            public long getItemId(int i) {
                Log.d("MainAdapter", "my_layout4 getItemId = " + (400 + i));
                return 400 + i;
            }
        });
        commonMultipleItem.setData(mutiItemDataSource);


        FooterItem footerItem = new FooterItem(R.layout.cell_my_footer, 2) {
            int i = 0;

            @Override
            protected void convert(ViewHolder holder) {
            }

            @Override
            public long getItemId(int i) {
                Log.d("MainAdapter", "footer getItemId = " + (500 + i));
                return 500 + i;
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

        adapter
                // banner
                .registerItem(new FixItem(R.layout.cell_main_recycler_item, 1, 2))
                // 多type布局
                .registerItem(commonMultipleItem)
                // 单type布局
                .registerItem(commonItem)
                // 加载更多
                .registerItem(footerItem);

//        adapter.notifyDataSetChanged();
        adapter.submitList();
    }
}
