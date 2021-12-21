package com.example.user.recycleradaptertest;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.user.recycleradaptertest.bean.MainBean;
import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.holder.ViewHolder;
import com.yzx.delegate.items.CommonItem;
import com.yzx.delegate.items.CommonMultipleItem;
import com.yzx.delegate.items.FixItem;
import com.yzx.delegate.items.FooterItem;
import com.yzx.delegate.layoutmanager.LayoutHelper;
import com.yzx.delegate.layoutmanager.PositionLayoutHelperFinder;
import com.yzx.delegate.layoutmanager.VirtualLayoutManager;
import com.yzx.delegate.layoutmanager.layout.GridLayoutHelper;
import com.yzx.delegate.layoutmanager.layout.LinearLayoutHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: yangzhenxiang@km.com
 * @data: 2020-06-29 21:07
 * @descripton:
 * @version:
 */
public class MainAdapter {

    public static void initMainAdapter(final RecyclerDelegateAdapter adapter, String[] titles, List<Object> mutiItemDataSource, VirtualLayoutManager layoutManager) {


        List<LayoutHelper> layoutHelpers = new ArrayList<>();
        final LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        final GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);

        layoutHelpers.add(linearLayoutHelper);
        layoutHelpers.add(gridLayoutHelper);
        layoutManager.setLayoutHelpers(layoutHelpers);

        //item的个数 随数据源而定，布局为一种
        CommonItem<String> commonItem = new CommonItem<String>(R.layout.cell_main_recycler_item2, 2) {
            @Override
            protected void convert(ViewHolder holder, final int position, int positionAtTotal, String s) {
                holder.setText(R.id.book_hot, String.format("最热榜榜%s", position + 1));
                holder.setOnViewTachStatusListener(new ViewHolder.OnViewTachStatusListener() {
                    @Override
                    public void onViewAttachedToWindow() {
                        Log.d("MainAdapter", "onViewAttachedToWindow" + position);
                    }

                    @Override
                    public void onViewDetachedFromWindow() {
                        Log.d("MainAdapter", "onViewDetachedFromWindow" + position);
                    }
                });
            }

            @Override
            public int findLayoutHelperByPosition(int i) {
                return gridLayoutHelper.hashCode();
            }
        };
        commonItem.setData(Arrays.asList(titles));

        //item 的个数 随数据源而定，布局为多种
        CommonMultipleItem<Object> commonMultipleItem = new CommonMultipleItem<>();
        commonMultipleItem.registerMultileChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout, 1) {
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
            public int findLayoutHelperByPosition(int i) {
                return gridLayoutHelper.hashCode();
            }

        }).registerMultileChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout5, 1) {
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
            public int findLayoutHelperByPosition(int i) {
                return gridLayoutHelper.hashCode();
            }
        }).registerMultileChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout4, 2) {
            @Override
            protected boolean handleItem(Object object) {
                return object instanceof MainBean.SectionTitle;
            }

            @Override
            protected void convert(ViewHolder holder, int position, int positionAtTotal, Object object) {
                MainBean.SectionTitle title = (MainBean.SectionTitle) object;
                holder.setText(R.id.section_title, title.title);
            }
        });
        commonMultipleItem.setData(mutiItemDataSource);


        FooterItem footerItem = new FooterItem(R.layout.cell_my_footer, 2) {
            int i = 0;

            @Override
            protected void convert(ViewHolder holder) {
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

        adapter.notifyDataSetChanged();
    }
}
