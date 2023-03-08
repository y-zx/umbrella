package com.yzx.delegate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yzx.delegate.holder.ViewHolder;
import com.yzx.delegate.items.DelegateItem;

/**
 * @Author: yangzhenxiang
 * @Time: 2018/5/8
 * @Description: recyclerView 代理适配器，实现复杂多Item，注意：每个不同的item 的布局资源文件 必须唯一
 * v2.0.0 开始支持GridLayoutManager的SpanSize。
 * @E-mail: yzxandroid981@163.com
 */

public class RecyclerDelegateAdapter extends RecyclerView.Adapter<ViewHolder> {

    protected Context context;

    private LayoutInflater factory;

    DelegateManager delegateManager;

    public RecyclerDelegateAdapter(Context context) {
        this.context = context;
        factory = LayoutInflater.from(context);
        delegateManager = new DelegateManager(context, factory);
    }

    public <T extends DelegateItem> T getItemByTag(int resId) {
        return delegateManager.getItemByTag(resId);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegateManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        delegateManager.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return delegateManager.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return delegateManager.getItemCount();
    }

    @Override
    public long getItemId(int position) {
        return delegateManager.getItemId(position);
    }

    public int getSpanSize(int position) {
        return delegateManager.getSpanSize(position);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }

    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m) {
        m.setAdapter(this);
        delegateManager.registerItem(m);
        return this;
    }

    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m, int location) {
        m.setAdapter(this);
        delegateManager.registerItem(m, location);
        return this;
    }

    public <M extends DelegateItem> RecyclerDelegateAdapter unregisterItem(@NonNull M m) {
        delegateManager.unregisterItem(m);
        return this;
    }

    public <M extends DelegateItem> RecyclerDelegateAdapter unregisterItem(@NonNull M m, int statusValue) {
        delegateManager.unregisterItem(m, statusValue);
        return this;
    }

    public void clearMultiItem() {
        delegateManager.getStatusHandleItems().clear();
        delegateManager.getDelegateArray().clear();
    }

    public void submitList() {
        notifyDataSetChanged();
    }

}
