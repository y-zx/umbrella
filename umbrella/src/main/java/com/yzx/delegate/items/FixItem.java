package com.yzx.delegate.items;

import androidx.annotation.NonNull;

import com.yzx.delegate.holder.ViewHolder;

/**
 * @Author: yangzhenxiang
 * @Time: 2018/5/15
 * @Description: 固定布局, 不需要数据源
 * @E-mail: yzxandroid981@163.com
 */

public class FixItem extends DelegateItem {

    public FixItem(int layoutResId) {
        this(layoutResId, 0);
    }

    public FixItem(int layoutResId, int count) {
        this(layoutResId, count, 1);
    }

    public FixItem(int layoutResId, int count, int spanSize) {
        super(layoutResId, count, spanSize);
    }

    @Override
    public void convert(@NonNull ViewHolder holder, int position, int positionAtTotal) {

    }

    @Override
    protected <T> T getItem(int position) {
        return null;
    }

}
