package com.yzx.delegate.items;

import androidx.annotation.NonNull;

import com.yzx.delegate.holder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangzhenxiang
 * @Time: 2018/5/15
 * @Description: item 个数根据数据源个数确定，但是布局为固定一种
 * @E-mail: yzxandroid981@163.com
 */

public abstract class CommonItem<T> extends DelegateItem<T> {

    public List<T> data = new ArrayList<>();

    public CommonItem(int layoutResId) {
        this(layoutResId, 1);
    }

    public CommonItem(int layoutResId, int spanSize) {
        super(layoutResId, 0, spanSize);
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
        getAdapter().submitList();
    }

    public void addData(T data) {
        if (this.data != null && data != null) {
            this.data.add(data);
        }
        getAdapter().submitList();
    }

    public List<T> getData() {
        return data;
    }

    @Override
    protected T getItem(int position) {
        return getData().get(position);
    }

    @Override
    public void convert(@NonNull ViewHolder holder, int position, int positionAtTotal) {
        convert(holder, position, positionAtTotal, data.get(position));
    }

    protected abstract void convert(@NonNull ViewHolder holder, int position, int positionAtTotal, T t);

}
