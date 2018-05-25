package com.yzx.delegate.items;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.holder.CommonViewHolder;

/**
 * Author: yangzhenxiang
 * Time: 2018/5/15
 * Description:
 * E-mail: yangzhenxiang@chelun.com
 */

public abstract class DelegateItem {

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

    public void notifyItemSetChanged(int position){
        adapter.notifyItemChanged(getScopeStartPosition()+position);
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

    /***获取结束position**/
    public int getScopeEndPosition() {
        return getScopeStartPosition() + getCount();
    }

    /**
     * 数据绑定，界面处理
     *
     * @Param RecyclerDelegateAdapter.CommonViewHolder holder  CommonViewHolder
     * @Param int relativePosition 相对于起始位置
     * @Param int positionAtTotal 该item在Recycler中position
     */
    public abstract void convert(CommonViewHolder holder, int relativePosition, int positionAtTotal);

    public Context getContext() {
        return context;
    }
}