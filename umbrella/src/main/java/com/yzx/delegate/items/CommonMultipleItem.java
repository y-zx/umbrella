package com.yzx.delegate.items;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;

import com.yzx.delegate.DelegateManager;
import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.holder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangzhenxiang
 * @Time: 2018/5/15
 * @Description: item 个数根据 数据源个数确定，布局可以设置为多种，此为中转代理
 * @E-mail: yzxandroid981@163.com
 */

public class CommonMultipleItem<T> extends DelegateItem {

    public abstract class MultipleChildItem {

        protected RecyclerDelegateAdapter adapter;
        @LayoutRes
        private int layoutResId;
        private int spanSize;

        public MultipleChildItem(@LayoutRes int layoutResId) {
            this(layoutResId, 1);
        }

        public MultipleChildItem(@LayoutRes int layoutResId, int spanSize) {
            this.layoutResId = layoutResId;
            this.spanSize = spanSize;
        }

        protected int getLayoutResId() {
            return layoutResId;
        }

        public int getSpanSize() {
            return spanSize;
        }

        public void setSpanSize(int spanSize) {
            this.spanSize = spanSize;
        }


        public int getTotalCount() {
            return CommonMultipleItem.this.getCount();
        }

        public Context getContext() {
            return context;
        }

        public RecyclerDelegateAdapter getAdapter() {
            return adapter;
        }

        protected abstract boolean handleItem(T t);

        protected boolean handleItem(int position) {
            return handleItem(data.get(position - getScopeStartPosition()));
        }

        protected abstract void convert(ViewHolder holder, int position, int positionAtTotal, T t);

        protected void convert(ViewHolder holder, int position, int positionAtTotal) {
            convert(holder, position, positionAtTotal, data.get(position));
        }
    }

    public List<T> data = new ArrayList<>();

    public SparseArray<CommonMultipleItem.MultipleChildItem> multipleChildren = new SparseArray<>();

    public <E extends CommonMultipleItem.MultipleChildItem> CommonMultipleItem<T> registerMultileChildItem(E e) {

        e.adapter = getAdapter();
        multipleChildren.put(e.getLayoutResId(), e);
        return this;
    }

    public <E extends CommonMultipleItem.MultipleChildItem> CommonMultipleItem<T> unregisterMultipleChildItem(E e) {

        e.adapter = null;
        multipleChildren.remove(e.getLayoutResId());
        return this;
    }

    private static int MULTIPLE_LAYOUT_OFFSET = 1;

    // ++MULTIPLE_LAYOUT_OFFSET 防止同一个adapter 注册两个 CommonMultipleItem时 覆盖
    public CommonMultipleItem() {
        super(DelegateManager.NO_LAYOUT_RESOURCE_FLAG + (++MULTIPLE_LAYOUT_OFFSET));
    }

    public CommonMultipleItem(List<T> data) {
        this();
        setData(data);
    }

    @Override
    public int getLayoutResId(int position) {
        for (int i = 0; i < multipleChildren.size(); i++) {
            if (multipleChildren.get(multipleChildren.keyAt(i)).handleItem(position)) {
                return multipleChildren.get(multipleChildren.keyAt(i)).getLayoutResId();
            }
        }
        return DelegateManager.NO_LAYOUT_RESOURCE_FLAG;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getSpanSize(int position) {
        for (int i = 0; i < multipleChildren.size(); i++) {
            CommonMultipleItem.MultipleChildItem item = multipleChildren.get(multipleChildren.keyAt(i));
            if (item.handleItem(position)) {
                return item.getSpanSize();
            }
        }
        return 1;
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

    public void addData(T data) {
        if (this.data != null && data != null) {
            this.data.add(data);
        }
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public void convert(ViewHolder holder, int position, int positionAtTotal) {
        for (int i = 0; i < multipleChildren.size(); i++) {
            CommonMultipleItem.MultipleChildItem item = multipleChildren.get(multipleChildren.keyAt(i));
            if (item.handleItem(positionAtTotal)) {
                item.convert(holder, position, positionAtTotal);
            }
        }
    }

}
