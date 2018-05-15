package com.yzx.delegate;

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
 * Author: yangzhenxiang
 * Time: 2018/5/8
 * Description:
 * E-mail: yangzhenxiang@chelun.com
 */

public class RecyclerDelegateAdapter extends RecyclerView.Adapter<RecyclerDelegateAdapter.ViewHolder> {

    public static final String TAG = "RecyclerDelegateAdapter";

    protected Context context;

    private LayoutInflater factory;

    /*缓存每个部局文件对应的DelegateItem， 提升onCreateViewHolder效率*/
    private SparseArray<DelegateItem> sparseArray = new SparseArray<>();

    public RecyclerDelegateAdapter(Context context) {
        this.context = context;
        factory = LayoutInflater.from(context);
    }

    private LinkedHashMap<Integer, DelegateItem> multiHandleItems = new LinkedHashMap<>();

    public <T extends DelegateItem> T getItemByTag(int resId) {
        return (T) multiHandleItems.get(resId);
    }

    public static final int NO_LAYOUT_RESOURCE_FLAG = -432432424;  //最好定义为别人不知道的，负责，


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType != NO_LAYOUT_RESOURCE_FLAG) {
            ViewHolder viewHolder =
                    new ViewHolder(factory.inflate(viewType, parent, false));
            DelegateItem item = multiHandleItems.get(viewType);
            if (item == null) {
                item = sparseArray.get(viewType);
            }
            viewHolder.setTag(item);
            return viewHolder;
        } else {
            //没有布局则填充一个 view
            return new ViewHolder(new View(context));
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getTag() != null) {
            DelegateItem item = (DelegateItem) holder.getTag();
            item.convert(holder, position - item.getScopeStartPosition(), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        for (Integer integer : multiHandleItems.keySet()) {
            DelegateItem item = multiHandleItems.get(integer);
            if (item.handleItem(position)) {
                if (item instanceof CommonMultipleItem) {
                    CommonMultipleItem commonMultipleItem = (CommonMultipleItem) item;
                    return commonMultipleItem.getLayoutResId(position);
                }
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


    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m) {
        if (m.getLayoutResId() == 0)
            throw new IllegalArgumentException("DelegateItem's layout resource can't be null");
        m.setAdapter(this);
        m.setContext(context);
        if (m instanceof FooterItem) {
            footerResId = m.getLayoutResId();
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    sparseArray.put(item.multipleChildren.keyAt(i), m);
                }
            }
        }
        multiHandleItems.put(m.getLayoutResId(), m);
        return this;
    }


    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m, int location) {
        if (m.getLayoutResId() == 0)
            throw new IllegalArgumentException("DelegateItem's layout resource can't be null");
        if (m instanceof FooterItem) {
            footerResId = m.getLayoutResId();
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    sparseArray.put(item.multipleChildren.keyAt(i), m);
                }
            }
        }
        m.setAdapter(this);
        m.setContext(context);
        List<DelegateItem> list = new ArrayList<>();
        Set<Integer> sets = multiHandleItems.keySet();
        for (Integer set : sets) {
            if (set.intValue() != m.getLayoutResId()) {
                list.add(multiHandleItems.get(set));
            }
        }
        list.add(location, m);
        multiHandleItems.clear();
        for (DelegateItem item : list) {
            multiHandleItems.put(item.getLayoutResId(), item);
        }
        return this;
    }

    public <M extends DelegateItem> RecyclerDelegateAdapter unregisterItem(@NonNull M m) {
        m.setAdapter(null);
        m.setContext(null);
        if (m instanceof FooterItem) {
            footerResId = 0;
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    sparseArray.remove(item.multipleChildren.keyAt(i));
                }
            }
        }
        multiHandleItems.remove(m.getLayoutResId());
        return this;
    }

    public void clearMultiItem() {
        multiHandleItems.clear();
        sparseArray.clear();
    }

    /**
     * 一般用于固定布局
     */
    private static abstract class DelegateItem {

        private RecyclerDelegateAdapter adapter;
        private int count;
        private int scopeStartPosition;
        protected Context context;


        public void setContext(@NonNull Context context) {
            this.context = context;
        }

        public DelegateItem(@LayoutRes int layoutResId) {
            this(layoutResId, 0);
        }

        public DelegateItem(@LayoutRes int layoutResId, int count) {

            this.layoutResId = layoutResId;
            this.count = count;
        }

        /****数据改变，adapter刷新****/
        // 绑定adapter
        public void setAdapter(@NonNull RecyclerDelegateAdapter adapter) {
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

        public RecyclerDelegateAdapter getAdapter() {
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

        /***RecyclerDelegateAdapter中调用判断是否处理对应position**/
        private boolean handleItem(int position) {
            return position >= getScopeStartPosition() && position < getScopeEndPosition();
        }

        /***RecyclerDelegateAdapter中调用设置起始position**/
        private void setScopeStartPosition(int position) {
            scopeStartPosition = position;
        }

        /***获取处理该item的个数**/
        public int getCount() {
            return count;
        }

        /***RecyclerDelegateAdapter中调用获取起始position**/
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
         * @Param RecyclerDelegateAdapter.ViewHolder holder  ViewHolder
         * @Param int relativePosition 相对于起始位置
         * @Param int positionAtTotal 该item在Recycler中position
         */
        protected abstract void convert(ViewHolder holder, int relativePosition, int positionAtTotal);

        public Context getContext() {
            return context;
        }
    }

    /**
     * 固定布局, 不需要数据源的那种
     */
    public static class FixItem extends DelegateItem {

        public FixItem(int layoutResId) {
            super(layoutResId);
        }

        public FixItem(int layoutResId, int count) {
            super(layoutResId, count);
        }

        @Override
        protected void convert(ViewHolder holder, int position, int positionAtTotal) {

        }
    }

    /**
     * 固定布局，需要数据源的，比如永远只有一个头部banner 而且需要 数据源给 Viewpager设置数据
     */
    public abstract static class FixItem2<T> extends DelegateItem {

        private T t;

        public FixItem2(int layoutResId) {
            super(layoutResId);
        }

        public FixItem2(int layoutResId, int count) {
            super(layoutResId, count);
        }

        public void setData(T t) {
            this.t = t;
        }

        @Override
        protected void convert(ViewHolder holder, int position, int positionAtTotal) {
            convert(holder, position, positionAtTotal, t);
        }

        protected abstract void convert(ViewHolder holder, int position, int positionAtTotal, T t);
    }

    /**
     * item 个数根据数据源个数确定，但是布局为固定一种
     */
    public abstract static class CommonItem<T> extends DelegateItem {

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
            if (data != null) {
                this.data = data;
            } else {
                this.data = new ArrayList<>();
            }
        }

        public void addData(List<T> data) {
            if (data != null && data.size() > 0) {
                this.data.addAll(data);
            }
        }

        @Override
        protected void convert(ViewHolder holder, int position, int positionAtTotal) {
            convert(holder, position, positionAtTotal, data.get(position));
        }

        protected abstract void convert(ViewHolder holder, int position, int positionAtTotal, T t);
    }

    /**
     * item 个数根据 数据源个数确定，布局可以设置为多中，此为中转代理
     */
    public static class CommonMultipleItem<T> extends DelegateItem {

        public abstract class MultipleChildItem {

            protected RecyclerDelegateAdapter adapter;
            @LayoutRes
            private int layoutResId;

            public MultipleChildItem(@LayoutRes int layoutResId) {
                this.layoutResId = layoutResId;
            }

            protected int getLayoutResId() {

                return layoutResId;
            }


            public int getTotalCount(){
                return CommonMultipleItem.this.getCount();
            }

            public Context getContext() {
                return context;
            }

            public RecyclerDelegateAdapter getAdapter() {
                return adapter;
            }

            protected abstract boolean handleItem(T t);

            protected abstract void convert(ViewHolder holder, int position, int positionAtTotal, T t);
        }

        public List<T> data = new ArrayList<>();

        public SparseArray<MultipleChildItem> multipleChildren = new SparseArray<>();

        public <E extends MultipleChildItem> CommonMultipleItem<T> registerMultileChildItem(E e) {

            e.adapter = getAdapter();
            multipleChildren.put(e.getLayoutResId(), e);
            return this;
        }

        public <E extends MultipleChildItem> CommonMultipleItem<T> unregisterMultipleChildItem(E e) {

            e.adapter = null;
            multipleChildren.remove(e.getLayoutResId());
            return this;
        }

        public CommonMultipleItem() {
            super(NO_LAYOUT_RESOURCE_FLAG + 1);
        }

        public CommonMultipleItem(List<T> data) {
            this();
            setData(data);
        }

        public int getLayoutResId(int position) {
            for (int i = 0; i < multipleChildren.size(); i++) {
                if (multipleChildren.get(multipleChildren.keyAt(i)).handleItem(data.get(position - getScopeStartPosition()))) {
                    return multipleChildren.get(multipleChildren.keyAt(i)).getLayoutResId();
                }
            }
            return NO_LAYOUT_RESOURCE_FLAG;
        }

        @Override
        public int getCount() {
            return data.size();
        }


        public void setData(List<T> data) {
            if (data != null) {
                this.data = data;
            } else {
                this.data = new ArrayList<>();
            }
        }

        public void addData(List<T> data) {
            if (data != null && data.size() > 0) {
                this.data.addAll(data);
            }
        }

        @Override
        protected void convert(ViewHolder holder, int position, int positionAtTotal) {
            for (int i = 0; i < multipleChildren.size(); i++) {
                MultipleChildItem item = multipleChildren.get(multipleChildren.keyAt(i));
                if (item.handleItem(data.get(position))) {
                    item.convert(holder, position, positionAtTotal, data.get(position));
                }
            }
        }


    }

    /**
     * 足部局，最低下的足部局，固定为1个，如果需要设置4种不同状态的的UI，
     * 请重写   @Method setFooterStatusChangedListener
     */
    public abstract static class FooterItem extends DelegateItem {

        ViewHolder holder;
        FooterStatusChangedListener listener;

        public FooterItem(int layoutResId) {
            super(layoutResId, 1);
            listener = setFooterStatusChangedListener();
        }

        @Override
        protected void convert(ViewHolder holder, int relativePosition, int positionAtTotal) {
            this.holder = holder;
            convert(holder);
        }

        protected abstract void convert(ViewHolder holder);

        public FooterStatusChangedListener setFooterStatusChangedListener() {
            return null;
        }

        private void setFooterStatus(int footerStatus) {
            if (listener != null) {
                switch (footerStatus) {
                    case FOOTER_STATUS_LOADING_COMPLETE:
                        listener.loadComplete(holder);
                        break;
                    case FOOTER_STATUS_LOADING:
                        listener.loading(holder);
                        break;
                    case FOOTER_STATUS_LOADING_ERROR:
                        listener.loadError(holder);
                        break;
                    case FOOTER_STATUS_NO_MORE:
                        listener.noMore(holder);
                        break;
                }
            }

        }
    }


    private int footerResId;
    public static final int FOOTER_STATUS_LOADING_COMPLETE = 1;
    public static final int FOOTER_STATUS_LOADING = 2;
    public static final int FOOTER_STATUS_LOADING_ERROR = 3;
    public static final int FOOTER_STATUS_NO_MORE = 4;
    public int footerStatus;

    public void setFooterStatusLoading() {
        footerStatus = FOOTER_STATUS_LOADING;
        FooterItem item = (FooterItem) multiHandleItems.get(footerResId);
        if (item != null) {
            item.setFooterStatus(footerStatus);
        }
    }

    public void setFooterStatusLoadMore() {
        footerStatus = FOOTER_STATUS_LOADING_COMPLETE;
        FooterItem item = (FooterItem) multiHandleItems.get(footerResId);
        if (item != null) {
            item.setFooterStatus(footerStatus);
        }
    }

    public void setFooterStatusLoadNoMore() {
        footerStatus = FOOTER_STATUS_NO_MORE;
        FooterItem item = (FooterItem) multiHandleItems.get(footerResId);
        if (item != null) {
            item.setFooterStatus(footerStatus);
        }
    }

    public void setFooterStatusLoadError() {
        footerStatus = FOOTER_STATUS_LOADING_ERROR;
        FooterItem item = (FooterItem) multiHandleItems.get(footerResId);
        if (item != null) {
            item.setFooterStatus(footerStatus);
        }

    }

    public int getFooterStatus() {
        return footerStatus;
    }

    public interface FooterStatusChangedListener {

        void loadComplete(ViewHolder holder);

        void loading(ViewHolder holder);

        void loadError(ViewHolder holder);

        void noMore(ViewHolder holder);
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

        public ImageView getImageView(@IdRes int id) {
            return getView(id);
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
