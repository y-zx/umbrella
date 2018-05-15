package com.example.user.recycleradaptertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private SparseArray<Item> multiHandleItems = new SparseArray<>();

    private HashMap<Item, Integer> scopeRecord = new HashMap<>();

    public static final int NO_LAYOUT_RESOURCE_FLAG = -1;

    @Override
    public RecyclerCommonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != NO_LAYOUT_RESOURCE_FLAG) {
            RecyclerCommonAdapter.ViewHolder viewHolder =
                    new RecyclerCommonAdapter.ViewHolder(factory.inflate(viewType, parent, false));
            if (multiHandleItems.get(viewType) != null) {
                viewHolder.setTag(multiHandleItems.get(viewType));
            }
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
        int size = multiHandleItems.size();
        for (int i = 0; i < size; i++) {
            int key = multiHandleItems.keyAt(i);
            if (multiHandleItems.get(key).handleItem(position)) {
                return key;
            }
        }
        return NO_LAYOUT_RESOURCE_FLAG;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        int size = multiHandleItems.size();
        for (int i = 0; i < size; i++) {
            int key = multiHandleItems.keyAt(i);
            multiHandleItems.get(key).setScopeStartPosition(count);
            scopeRecord.put(multiHandleItems.get(key), count);
            count += multiHandleItems.get(key).getCount();

        }
        return count;
    }

    public <M extends Item> RecyclerCommonAdapter registerItem(@NonNull M m) {
        m.setAdapter(this);
        m.setContext(context);
        if (m.getLayoutResId() != 0) multiHandleItems.put(m.getLayoutResId(), m);
        return this;
    }

    public <M extends Item> void removeItem(M m) {
        multiHandleItems.remove(m.getLayoutResId());
    }

    public void clearMultiIem() {
        multiHandleItems.clear();
    }

    private static abstract class Item {

        private RecyclerCommonAdapter adapter;
        private int count;
        private int scopeStartPosition;
        protected Context context;

        public void setContext(@NonNull Context context) {
            this.context = context;
        }

        public Item(@LayoutRes int layoutResId) {
            this(layoutResId, 0);
        }

        public Item(@LayoutRes int layoutResId, int count) {
            this.layoutResId = layoutResId;
            this.count = count;
        }


        /****数据改变，adapter刷新****/
        // 绑定adapter
        public void setAdapter(@NonNull RecyclerCommonAdapter adapter) {
            this.adapter = adapter;
        }

        public void setCount(@NonNull int count) {
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

        public FixItem(int layoutResId) {
            super(layoutResId);
        }

        public FixItem(int layoutResId, int count) {
            super(layoutResId, count);
        }

        @Override
        protected void convert(RecyclerCommonAdapter.ViewHolder holder, int position, int positionAtTotal) {

        }
    }

    public abstract static class CommonItem<T> extends Item {

        public List<T> data = new ArrayList<>();

        public CommonItem(int layoutResId) {
            super(layoutResId);
        }

        public CommonItem(int layoutResId, List<T> data) {
            super(layoutResId);
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
            if (data == null) return;
            this.data.addAll(data);
        }

        @Override
        protected void convert(RecyclerCommonAdapter.ViewHolder holder, int position, int positionAtTotal) {
            convert(holder, position, positionAtTotal, data.get(position));
        }

        protected abstract void convert(RecyclerCommonAdapter.ViewHolder holder, int position,int positionAtTotal, T t);
    }

    //common ViewHolder
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
            tv.setText(charSequence);
            return this;
        }

        public ViewHolder setText(@IdRes int id, Spannable spannable) {
            TextView tv = getView(id);
            tv.setText(spannable);
            return this;
        }

        public ViewHolder setImageRes(@IdRes int id, int imgResId) {
            ImageView imageView = getView(id);
            imageView.setImageResource(imgResId);
            return this;
        }

        public ViewHolder setImageBitMap(@IdRes int id, Bitmap bitmap) {
            ImageView imageView = getView(id);
            imageView.setImageBitmap(bitmap);
            return this;
        }

    }

}
