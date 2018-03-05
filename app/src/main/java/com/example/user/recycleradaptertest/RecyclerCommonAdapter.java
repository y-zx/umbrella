package com.example.user.recycleradaptertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by yangzhenxiang on 2017/11/13.
 * recyclerView 多重万能adapter
 */

public class RecyclerCommonAdapter extends RecyclerView.Adapter<RecyclerCommonAdapter.ViewHolder> {


    public static final String TAG = "RecyclerCommonAdapter";

    protected Context context;

    private LayoutInflater factory;


    public RecyclerCommonAdapter(Context context) {
        this.context = context;
        factory = LayoutInflater.from(context);
    }

    private LinkedHashMap<Integer, Item> multiHandleItems = new LinkedHashMap<>();

    public <T extends Item> T getItemByTag(int tag){
       return (T) multiHandleItems.get(tag);
    }

    public static final int NO_LAYOUT_RESOURCE_FLAG = -432432424;  //最好定义为别人不知道的，负责，

    @Override
    public RecyclerCommonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType != NO_LAYOUT_RESOURCE_FLAG) {
            RecyclerCommonAdapter.ViewHolder viewHolder =
                    new RecyclerCommonAdapter.ViewHolder(factory.inflate(multiHandleItems.get(viewType).getLayoutResId(), parent, false));
            viewHolder.setTag(multiHandleItems.get(viewType));
            return viewHolder;
        } else {
            //没有布局则填充一个 view
            return new RecyclerCommonAdapter.ViewHolder(new View(context));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerCommonAdapter.ViewHolder holder, int position) {
        if (holder.getTag() != null) {
            Item item = (Item) holder.getTag();
            item.convert(holder, position - item.getScopeStartPosition(), position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        for (Integer integer : multiHandleItems.keySet()) {
            Item item = multiHandleItems.get(integer);
            if (item.handleItem(position)) {
                return integer;
            }
        }

        return NO_LAYOUT_RESOURCE_FLAG;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (Integer integer : multiHandleItems.keySet()) {
            multiHandleItems.get(integer).setScopeStartPosition(count);
            count += multiHandleItems.get(integer).getCount();
        }

        return count;
    }

    public <M extends Item> RecyclerCommonAdapter registerItem(@NonNull M m) {
        m.setAdapter(this);
        m.setContext(context);
        multiHandleItems.put(m.getTag(), m);
        return this;
    }


    public <M extends Item> RecyclerCommonAdapter registerItem(@NonNull M m, int location) {
        m.setAdapter(this);
        m.setContext(context);
        List<Item> list = new ArrayList<>();
        Set<Integer> sets = multiHandleItems.keySet();
        for (Integer set : sets) {
            if (set.intValue() != m.getTag()) {
                list.add(multiHandleItems.get(set));
            }
        }
        list.add(location, m);
        multiHandleItems.clear();
        for (Item item : list) {
            multiHandleItems.put(item.getTag(), item);
        }
        return this;
    }

    public <M extends Item> RecyclerCommonAdapter unregisterItem(@NonNull M m) {
        m.setAdapter(null);
        m.setContext(null);
        multiHandleItems.remove(m.getTag());
        return this;
    }

    public <M extends Item> void removeItem(M m) {
        multiHandleItems.remove(m.getTag());
    }

    public void clearMultiItem() {
        multiHandleItems.clear();
    }

    /**
     * 一般用于固定布局
     */
    public static abstract class Item {

        private RecyclerCommonAdapter adapter;
        private int count;
        private int scopeStartPosition;
        protected Context context;
        private int tag;

        public int getTag() {
            return tag;
        }

        public void setContext(@NonNull Context context) {
            this.context = context;
        }

        public Item(int tag, @LayoutRes int layoutResId) {
            this(tag, layoutResId, 0);
        }

        public Item(int tag, @LayoutRes int layoutResId, int count) {
            this.tag = tag;
            this.layoutResId = layoutResId;
            this.count = count;
        }

        /****数据改变，adapter刷新****/
        // 绑定adapter
        public void setAdapter(@NonNull RecyclerCommonAdapter adapter) {
            this.adapter = adapter;
        }

        public void setCount(@IntRange(from = 0) int count) {
            this.count = count;
        }

        public void notifyDataSetChanged() {
            adapter.notifyDataSetChanged();
        }

        public void notifyRangeSetChanged() {
            adapter.notifyItemRangeChanged(getScopeStartPosition(), getScopeEndPosition());
        }

        public RecyclerCommonAdapter getAdapter() {
            return adapter;
        }

        /****item layout相关****/
        //布局文件
        @LayoutRes
        private int layoutResId;

        //item layout资源
        public int getLayoutResId() {
            return layoutResId;
        }

        /***RecyclerCommonAdapter中调用判断是否处理对应position**/
        private boolean handleItem(int position) {
            return position >= getScopeStartPosition() && position < getScopeEndPosition();
        }

        /***RecyclerCommonAdapter中调用设置起始position**/
        private void setScopeStartPosition(int position) {
            scopeStartPosition = position;
        }

        /***获取处理该item的个数**/
        public int getCount() {
            return count;
        }

        /***RecyclerCommonAdapter中调用获取起始position**/
        public int getScopeStartPosition() {
            return scopeStartPosition;
        }

        /***获取结束position**/
        public int getScopeEndPosition() {
            return getScopeStartPosition() + getCount();
        }

        /**
         * 数据绑定，界面处理
         *
         * @Param RecyclerCommonAdapter.ViewHolder holder  ViewHolder
         * @Param int relativePosition 相对于起始位置
         * @Param int positionAtTotal 该item在Recycler中position
         */
        protected abstract void convert(RecyclerCommonAdapter.ViewHolder holder, int relativePosition, int positionAtTotal);


    }

    /**
     * 固定布局，
     */
    public static class FixItem extends Item {

        public FixItem(int tag, int layoutResId) {
            super(tag, layoutResId);
        }

        public FixItem(int tag, int layoutResId, int count) {
            super(tag, layoutResId, count);
        }


        @Override
        protected void convert(RecyclerCommonAdapter.ViewHolder holder, int position, int positionAtTotal) {

        }
    }

    public abstract static class CommonItem<T> extends Item {

        public List<T> data = new ArrayList<>();

        public CommonItem(int tag, int layoutResId) {
            super(tag, layoutResId);
        }

        public CommonItem(int tag, int layoutResId, List<T> data) {
            super(tag, layoutResId);
            setData(data);
        }


        @Override
        public int getCount() {
            return data.size();
        }


        public void setData(List<T> data) {
            if (data == null) throw new NullPointerException("List<T> can't be null");
            this.data = data;
        }

        public void addData(List<T> data) {
            if (data == null || data.size() == 0) return;
            this.data.addAll(data);
        }

        @Override
        protected void convert(RecyclerCommonAdapter.ViewHolder holder, int position, int positionAtTotal) {
            convert(holder, position, positionAtTotal, data.get(position));
        }

        protected abstract void convert(RecyclerCommonAdapter.ViewHolder holder, int position, int positionAtTotal, T t);
    }

    //万能 ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> views = new SparseArray<>();

        private Object tag;

        public void setTag(Object object) {
            tag = object;
        }

        public Object getTag() {
            return tag;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public <E extends View> E getView(@IdRes int id) {
            View view = views.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                views.put(id, view);
            }
            return (E) view;
        }

        public ViewHolder setText(@IdRes int id, String charSequence) {
            TextView tv = getView(id);
            if (tv != null) {
                tv.setText(charSequence);
            }
            return this;
        }

        public ViewHolder setText(@IdRes int id, CharSequence charSequence) {
            TextView tv = getView(id);
            tv.setText(charSequence);
            return this;
        }

        public ViewHolder setText(@IdRes int id, Spanned spanned) {
            TextView tv = getView(id);
            tv.setText(spanned);
            return this;
        }

        public ViewHolder setImageRes(@IdRes int id, int imgResId) {
            ImageView imageView = getView(id);
            if (imageView != null) {
                imageView.setImageResource(imgResId);
            }
            return this;
        }

        public ViewHolder setImageBitMap(@IdRes int id, Bitmap bitmap) {
            ImageView imageView = getView(id);
            imageView.setImageBitmap(bitmap);
            return this;
        }

        public ViewHolder setViewVisible(@IdRes int id, int visible) {
            getView(id).setVisibility(visible);
            return this;
        }

        public TextView getTextView(@IdRes int id) {
            return getView(id);
        }

        public ViewHolder setSelected(@IdRes int id, boolean select) {
            getView(id).setSelected(select);
            return this;
        }


        public ViewHolder setBackGroundDrawable(@IdRes int id, @DrawableRes int drawableRes) {
            getView(id).setBackground(getView(id).getContext().getResources().getDrawable(drawableRes));
            return this;
        }
    }


}
