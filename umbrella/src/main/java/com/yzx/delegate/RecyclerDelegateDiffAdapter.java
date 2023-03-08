package com.yzx.delegate;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;

import com.yzx.delegate.items.DelegateItem;

/**
 * @author: yangzhenxiang@km.com
 * @data: 2023/3/7 9:41 PM
 * @descripton:
 * @version:
 */
public class RecyclerDelegateDiffAdapter extends RecyclerDelegateAdapter {

    private AsyncListDiffer<DelegateItem.DiffBean> mHelper;
    private DiffUtil.ItemCallback<DelegateItem.DiffBean> diffCallback;
    private AsyncDifferConfig<DelegateItem.DiffBean> config;

    public RecyclerDelegateDiffAdapter(Context context) {
        super(context);
    }

    public RecyclerDelegateDiffAdapter(Context context, @NonNull DiffUtil.ItemCallback<DelegateItem.DiffBean> diffCallback) {
        super(context);
        this.diffCallback = diffCallback;
    }

    public RecyclerDelegateDiffAdapter(Context context, @NonNull AsyncDifferConfig<DelegateItem.DiffBean> config) {
        super(context);
        this.config = config;
    }

    @Override
    public void submitList() {
        // 先调用一次，获取 position
        getmHelper().submitList(delegateManager.getItemList());
    }

    private AsyncListDiffer<DelegateItem.DiffBean> getmHelper() {
        if (mHelper == null) {
            if (config != null) {
                mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), config);
            } else if (diffCallback != null) {
                mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), new AsyncDifferConfig.Builder<>(diffCallback).build());
            } else {
                DiffUtil.ItemCallback<DelegateItem.DiffBean> diffCallback = new DiffUtil.ItemCallback<DelegateItem.DiffBean>() {
                    @Override
                    public boolean areItemsTheSame(@NonNull DelegateItem.DiffBean oldItem, @NonNull DelegateItem.DiffBean newItem) {
                        // 布局类型是否一样
                        return oldItem.areItemsTheSame(newItem);
                    }

                    @Override
                    public boolean areContentsTheSame(@NonNull DelegateItem.DiffBean oldItem, @NonNull DelegateItem.DiffBean newItem) {
                        // 数据是否一样
                        return oldItem.areContentsTheSame(newItem);
                    }
                };
                mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), new AsyncDifferConfig.Builder<>(diffCallback).build());
            }
        }
        return mHelper;
    }
}
