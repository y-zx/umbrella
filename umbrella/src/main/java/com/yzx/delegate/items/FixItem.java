package com.yzx.delegate.items;

import com.yzx.delegate.holder.CommonViewHolder;

/**
 * Author: yangzhenxiang
 * Time: 2018/5/15
 * Description: 固定布局, 不需要数据源
 * E-mail: yangzhenxiang@chelun.com
 */

public class FixItem  extends DelegateItem {

    public FixItem(int layoutResId) {
        super(layoutResId);
    }

    public FixItem(int layoutResId, int count) {
        super(layoutResId, count);
    }

    @Override
    public void convert(CommonViewHolder holder, int position, int positionAtTotal) {

    }
}
