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
 * Author: yangzhenxiang
 * Time: 2018/5/15
 * Description:
 * E-mail: yangzhenxiang@chelun.com
 */

public class CommonViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views = new SparseArray<>();

    private Object tag;

    public void setTag(Object object) {
        tag = object;
    }

    public Object getTag() {
        return tag;
    }

    public CommonViewHolder(View itemView) {
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

    public CommonViewHolder setText(@IdRes int id, String charSequence) {
        TextView tv = getView(id);
        if (tv != null) {
            tv.setText(charSequence);
        }
        return this;
    }

    public CommonViewHolder setText(@IdRes int id, CharSequence charSequence) {
        TextView tv = getView(id);
        tv.setText(charSequence);
        return this;
    }

    public CommonViewHolder setText(@IdRes int id, Spanned spanned) {
        TextView tv = getView(id);
        tv.setText(spanned);
        return this;
    }

    public CommonViewHolder setImageRes(@IdRes int id, int imgResId) {
        ImageView imageView = getView(id);
        if (imageView != null) {
            imageView.setImageResource(imgResId);
        }
        return this;
    }

    public ImageView getImageView(@IdRes int id) {
        return getView(id);
    }

    public CommonViewHolder setImageBitMap(@IdRes int id, Bitmap bitmap) {
        ImageView imageView = getView(id);
        imageView.setImageBitmap(bitmap);
        return this;
    }

   public CommonViewHolder setViewVisible(@IdRes int id, int visible) {
        getView(id).setVisibility(visible);
        return this;
    }

    public TextView getTextView(@IdRes int id) {
        return getView(id);
    }

   public CommonViewHolder setSelected(@IdRes int id, boolean select) {
        getView(id).setSelected(select);
        return this;
    }


   public CommonViewHolder setBackGroundDrawable(@IdRes int id, @DrawableRes int drawableRes) {
        getView(id).setBackground(getView(id).getContext().getResources().getDrawable(drawableRes));
        return this;
    }
}
