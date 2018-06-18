package com.yzx.delegate;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yzx.delegate.holder.CommonViewHolder;
import com.yzx.delegate.items.CommonMultipleItem;
import com.yzx.delegate.items.DelegateItem;
import com.yzx.delegate.items.FooterItem;

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

public class RecyclerDelegateAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    public static final String TAG = "RecyclerDelegateAdapter";

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
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegateManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
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


    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m) {
        if (m.getLayoutResId() == 0)
            throw new IllegalArgumentException("DelegateItem's layout resource can't be null");
        m.setAdapter(this);
        delegateManager.registerItem(m);
        return this;
    }


    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m, int location) {
        if (m.getLayoutResId() == 0)
            throw new IllegalArgumentException("DelegateItem's layout resource can't be null");
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
        delegateManager.getStatusHandleItems().get(delegateManager.getCurrentStatus()).clear();
        delegateManager.getDelegateArray().clear();
    }

    public void clearAllStatusMultiItems() {
        delegateManager.getStatusHandleItems().clear();
        delegateManager.getDelegateArray().clear();
    }

    public void clearPointStatusMultiItem(int value) {
        delegateManager.getStatusHandleItems().get(value).clear();
        delegateManager.getDelegateArray().clear();
    }

    public void setCurrentStatus(int statusValue) {
        delegateManager.setCurrentStatus(statusValue);
    }

    public void setDifferentStatus(@IntRange(from = 1, to = Integer.MAX_VALUE) int statusValue) {
        delegateManager.setCurrentStatus(statusValue);
        delegateManager.getStatusHandleItems().put(statusValue, new LinkedHashMap<Integer, DelegateItem>());
    }


    public void setFooterStatusLoading() {
        FooterItem item = (FooterItem) delegateManager.getCurrentFooterItem();
        if (item != null) {
            item.setFooterLoadingStatus();
        }
    }

    public void setFooterStatusLoadMore() {
        FooterItem item = (FooterItem) delegateManager.getCurrentFooterItem();
        if (item != null) {
            item.setFooterStatusLoadNoMore();
        }
    }

    public void setFooterStatusLoadNoMore() {
        FooterItem item = (FooterItem) delegateManager.getCurrentFooterItem();
        if (item != null) {
            item.setFooterStatusLoadMore();
        }
    }

    public void setFooterStatusLoadError() {
        FooterItem item = (FooterItem) delegateManager.getCurrentFooterItem();
        if (item != null) {
            item.setFooterStatusLoadError();
        }
    }

    public void setFooterStatusGone() {
        FooterItem item = (FooterItem) delegateManager.getCurrentFooterItem();
        if (item != null) {
            item.setFooterStatusGone();
        }
    }

    public int getFooterStatus() {
        FooterItem item = (FooterItem) delegateManager.getCurrentFooterItem();
        if (item != null) {
            return item.getFooterStatus();
        }
        return 0;
    }

}
