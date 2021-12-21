package com.yzx.delegate.items;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.holder.ViewHolder;

/**
 * @Author: yangzhenxiang
 * @Time: 2018/5/8
 * @Description: 每种item的代理基类
 * @E-mail: yzxandroid981@163.com
 */

public abstract class DelegateItem {

    private RecyclerDelegateAdapter adapter;
    private int count;
    private int scopeStartPosition;
    private int spanSize = 1;
    protected Context context;

    public int getSpanSize(int position) {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public void setContext(@NonNull Context context) {
        this.context = context;
    }

    public DelegateItem(@LayoutRes int layoutResId) {
        this(layoutResId, 0);
    }

    public DelegateItem(@LayoutRes int layoutResId, int count) {
        this(layoutResId, count, 1);
    }

    public DelegateItem(@LayoutRes int layoutResId, int count, int spanSize) {
        this.layoutResId = layoutResId;
        this.count = count;
        this.spanSize = spanSize;
    }

    /****数据改变，adapter刷新****/
    // 绑定adapter
    public void setAdapter(@NonNull RecyclerDelegateAdapter adapter) {
        this.adapter = adapter;
    }

    public void setCount(@IntRange(from = 0) int count) {
        this.count = count;
    }

    public void registerCallBack(Context context) {

    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public void notifyRangeSetChanged() {
        adapter.notifyItemRangeChanged(getScopeStartPosition(), getCount());
    }

    public void notifyRangeSetChanged(int startPosition, int count) {
        adapter.notifyItemRangeChanged(getScopeStartPosition() + startPosition, count);
    }

    public void notifyItemSetChanged(int position) {
        adapter.notifyItemChanged(getScopeStartPosition() + position);
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

    //item layout资源
    public int getLayoutResId(int position) {
        return layoutResId;
    }

    /***RecyclerDelegateAdapter中调用判断是否处理对应position**/
    public boolean handleItem(int position) {
        return position >= getScopeStartPosition() && position < getScopeEndPosition();
    }

    /***RecyclerDelegateAdapter中调用设置起始position**/
    public void setScopeStartPosition(int position) {
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

    /***RecyclerDelegateAdapter中取某position 在recyclerView中的绝对position**/
    public int getAbsolutePosition(int position) {
        return getScopeStartPosition() + position;
    }

    /***获取结束position**/
    public int getScopeEndPosition() {
        return getScopeStartPosition() + getCount();
    }

    /**
     * 数据绑定，界面处理
     *
     * @param holder           ViewHolder  所有View 在holder中通过id获取
     * @param relativePosition 相对于起始位置
     * @param absolutePosition 该item在Recycler中position
     */
    public abstract void convert(ViewHolder holder, int relativePosition, int absolutePosition);

    public void convert(ViewHolder holder, int absolutePosition) {
        convert(holder, absolutePosition - scopeStartPosition, absolutePosition);
    }

    public Context getContext() {
        return context;
    }

    public int findLayoutHelperByPosition(int position) {
        return 0;
    }
}