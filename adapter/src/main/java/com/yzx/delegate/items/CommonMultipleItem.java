package com.yzx.delegate.items;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;

import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.holder.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yangzhenxiang
 * Time: 2018/5/15
 * Description: item 个数根据 数据源个数确定，布局可以设置为多中，此为中转代理
 * E-mail: yangzhenxiang@chelun.com
 */

public class CommonMultipleItem <T> extends DelegateItem {

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

        protected abstract void convert(CommonViewHolder holder, int position, int positionAtTotal, T t);
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

    public CommonMultipleItem() {
        super(RecyclerDelegateAdapter.NO_LAYOUT_RESOURCE_FLAG + 1);
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
        return RecyclerDelegateAdapter.NO_LAYOUT_RESOURCE_FLAG;
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
    public void convert(CommonViewHolder holder, int position, int positionAtTotal) {
        for (int i = 0; i < multipleChildren.size(); i++) {
            CommonMultipleItem.MultipleChildItem item = multipleChildren.get(multipleChildren.keyAt(i));
            if (item.handleItem(data.get(position))) {
                item.convert(holder, position, positionAtTotal, data.get(position));
            }
        }
    }

}
