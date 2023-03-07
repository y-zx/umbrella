package com.yzx.delegate;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.yzx.delegate.RecyclerDelegateAdapter;
import com.yzx.delegate.holder.ViewHolder;
import com.yzx.delegate.items.DelegateItem;

import java.util.List;

/**
 * @author: yangzhenxiang@km.com
 * @data: 2023/3/7 9:41 PM
 * @descripton:
 * @version:
 */
public class RecyclerDelegateDiffAdapter extends RecyclerDelegateAdapter {

    private final AsyncListDiffer<DelegateItem.DiffBean> mHelper;

    public RecyclerDelegateDiffAdapter(Context context, @NonNull DiffUtil.ItemCallback<DelegateItem.DiffBean> diffCallback) {
        super(context);
        mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), new AsyncDifferConfig.Builder<>(diffCallback).build());
    }
    public RecyclerDelegateDiffAdapter(Context context, @NonNull AsyncDifferConfig<DelegateItem.DiffBean> config) {
        super(context);
        mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), config);
    }

    @Override
    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m, int location) {
        return super.registerItem(m, location);
    }

    @Override
    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m) {
        return super.registerItem(m);
    }

    @Override
    public void submitList() {
        super.submitList();
        // 先调用一次，获取 position
        delegateManager.getItemCount();
        mHelper.submitList(delegateManager.getItemList());
    }
}
