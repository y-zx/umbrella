package com.yzx.delegate.holder;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Author: yangzhenxiang
 * @Time: 2018/5/15
 * @Description: 布局中的控件通过 getView方法中获取
 * @E-mail: yzxandroid981@163.com
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views = new SparseArray<>();

    private Object tag;

    public void setTag(Object object) {
        tag = object;
    }

    public Object getTag() {
        return tag;
    }

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public <E extends View> E getView(@IdRes int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (E) view;
    }

    public ViewHolder setText(@IdRes int id, String charSequence) {
        TextView tv = getView(id);
        if (tv != null) {
            tv.setText(charSequence);
        }
        return this;
    }

    public ViewHolder setText(@IdRes int id, CharSequence charSequence) {
        TextView tv = getView(id);
        tv.setText(charSequence);
        return this;
    }

    public ViewHolder setText(@IdRes int id, Spanned spanned) {
        TextView tv = getView(id);
        tv.setText(spanned);
        return this;
    }

    public ViewHolder setImageRes(@IdRes int id, int imgResId) {
        ImageView imageView = getView(id);
        if (imageView != null) {
            imageView.setImageResource(imgResId);
        }
        return this;
    }

    public ImageView getImageView(@IdRes int id) {
        return getView(id);
    }

    public ViewHolder setImageBitMap(@IdRes int id, Bitmap bitmap) {
        ImageView imageView = getView(id);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setViewVisible(@IdRes int id, int visible) {
        getView(id).setVisibility(visible);
        return this;
    }

    public ViewHolder setOnClickListener(@IdRes int id, View.OnClickListener l) {
        getView(id).setOnClickListener(l);
        return this;
    }

    public TextView getTextView(@IdRes int id) {
        return getView(id);
    }

    public ViewHolder setSelected(@IdRes int id, boolean select) {
        getView(id).setSelected(select);
        return this;
    }


    public ViewHolder setBackGroundDrawable(@IdRes int id, @DrawableRes int drawableRes) {
        getView(id).setBackground(getView(id).getContext().getResources().getDrawable(drawableRes));
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
