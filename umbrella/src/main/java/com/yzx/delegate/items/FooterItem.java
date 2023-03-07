package com.yzx.delegate.items;

import android.view.View;

import androidx.annotation.NonNull;

import com.yzx.delegate.holder.ViewHolder;

/**
 * @Author: yangzhenxiang
 * @Time: 2018/5/15
 * @Description:足部局，最低下的足部局，固定为1个，如果需要设置4种不同状态的的UI， 请重写   @Method setFooterStatusChangedListener
 * @E-mail: yzxandroid981@163.com
 */

public abstract class FooterItem extends DelegateItem<Void> {

    public static final int FOOTER_STATUS_LOADING_COMPLETE = 1;
    public static final int FOOTER_STATUS_LOADING = 2;
    public static final int FOOTER_STATUS_LOADING_ERROR = 3;
    public static final int FOOTER_STATUS_NO_MORE = 4;
    public static final int FOOTER_STATUS_GONE = 5;
    public int footerStatus;

    ViewHolder holder;
    FooterStatusChangedListener listener;

    public FooterItem(int layoutResId) {
        this(layoutResId, 1);
    }

    public FooterItem(int layoutResId, int spanSize) {
        super(layoutResId, 1, spanSize);
        listener = setFooterStatusChangedListener();
    }

    @Override
    public void setCount(int count) {
        super.setCount(count);
        getAdapter().submitList();
    }

    @Override
    protected Void getItem(int position) {
        return null;
    }

    @Override
    public void convert(@NonNull ViewHolder holder, int relativePosition, int positionAtTotal) {
        this.holder = holder;
        convert(holder);
    }

    protected abstract void convert(@NonNull ViewHolder holder);

    public FooterStatusChangedListener setFooterStatusChangedListener() {
        return null;
    }

    public void setFooterStatus(int footerStatus) {
        this.footerStatus = footerStatus;
        if (listener != null && holder != null) {
            switch (footerStatus) {
                case FOOTER_STATUS_LOADING_COMPLETE:
                    holder.itemView.setVisibility(View.VISIBLE);
                    listener.loadComplete(holder);
                    break;
                case FOOTER_STATUS_LOADING:
                    holder.itemView.setVisibility(View.VISIBLE);
                    listener.loading(holder);
                    break;
                case FOOTER_STATUS_LOADING_ERROR:
                    holder.itemView.setVisibility(View.VISIBLE);
                    listener.loadError(holder);
                    break;
                case FOOTER_STATUS_NO_MORE:
                    holder.itemView.setVisibility(View.VISIBLE);
                    listener.noMore(holder);
                    break;
                case FOOTER_STATUS_GONE:
                default:
                    holder.itemView.setVisibility(View.GONE);
                    break;
            }
        }

    }

    public void setFooterLoadingStatus() {
        setFooterStatus(FOOTER_STATUS_LOADING);
    }

    public void setFooterStatusNoMore() {
        setFooterStatus(FOOTER_STATUS_NO_MORE);
    }

    public void setFooterStatusLoadMore() {
        setFooterStatus(FOOTER_STATUS_LOADING_COMPLETE);
    }

    public void setFooterStatusLoadError() {
        setFooterStatus(FOOTER_STATUS_LOADING_ERROR);
    }

    public void setFooterStatusGone() {
        setFooterStatus(FOOTER_STATUS_GONE);
    }

    public int getFooterStatus() {
        return footerStatus;
    }

    public interface FooterStatusChangedListener {

        void loadComplete(@NonNull ViewHolder holder);

        void loading(@NonNull ViewHolder holder);

        void loadError(@NonNull ViewHolder holder);

        void noMore(@NonNull ViewHolder holder);
    }
}
