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

    /*缓存每个部局文件对应的DelegateItem， 提升onCreateCommonViewHolder效率*/
    private SparseArray<DelegateItem> sparseArray = new SparseArray<>();

    public RecyclerDelegateAdapter(Context context) {
        this.context = context;
        factory = LayoutInflater.from(context);
        statusHandleItems.put(NORMAL_STATUS, new LinkedHashMap<Integer, DelegateItem>());
    }

    private SparseArray<LinkedHashMap<Integer, DelegateItem>> statusHandleItems = new SparseArray<>();

    public static final int NORMAL_STATUS = 0;

    private int currentStatus = NORMAL_STATUS;

    public <T extends DelegateItem> T getItemByTag(int resId) {
        return (T) statusHandleItems.get(currentStatus).get(resId);
    }

    public static final int NO_LAYOUT_RESOURCE_FLAG = -432432424;  //最好定义为别人不知道的，负责，


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType != NO_LAYOUT_RESOURCE_FLAG) {
            CommonViewHolder CommonViewHolder =
                    new CommonViewHolder(factory.inflate(viewType, parent, false));
            DelegateItem item = statusHandleItems.get(currentStatus).get(viewType);
            if (item == null) {
                item = sparseArray.get(viewType);
            }
            CommonViewHolder.setTag(item);
            return CommonViewHolder;
        } else {
            //没有布局则填充一个 view
            return new CommonViewHolder(new View(context));
        }

    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        if (holder.getTag() != null) {
            DelegateItem item = (DelegateItem) holder.getTag();
            item.convert(holder, position - item.getScopeStartPosition(), position);
        }
    }
    

    @Override
    public int getItemViewType(int position) {
        for (Integer integer : statusHandleItems.get(currentStatus).keySet()) {
            DelegateItem item = statusHandleItems.get(currentStatus).get(integer);
            if (item.handleItem(position)) {
                if (item instanceof CommonMultipleItem) {
                    CommonMultipleItem commonMultipleItem = (CommonMultipleItem) item;
                    return commonMultipleItem.getLayoutResId(position);
                }
                return integer;
            }
        }

        return NO_LAYOUT_RESOURCE_FLAG;
    }


    @Override
    public int getItemCount() {
        int count = 0;
        for (Integer integer : statusHandleItems.get(currentStatus).keySet()) {
            statusHandleItems.get(currentStatus).get(integer).setScopeStartPosition(count);
            count += statusHandleItems.get(currentStatus).get(integer).getCount();
        }
        return count;
    }


    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m) {
        if (m.getLayoutResId() == 0)
            throw new IllegalArgumentException("DelegateItem's layout resource can't be null");
        m.setAdapter(this);
        m.setContext(context);
        if (m instanceof FooterItem) {
            footerResId = m.getLayoutResId();
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    sparseArray.put(item.multipleChildren.keyAt(i), m);
                }
            }
        }
        statusHandleItems.get(currentStatus).put(m.getLayoutResId(), m);
        return this;
    }


    public <M extends DelegateItem> RecyclerDelegateAdapter registerItem(@NonNull M m, int location) {
        if (m.getLayoutResId() == 0)
            throw new IllegalArgumentException("DelegateItem's layout resource can't be null");
        if (m instanceof FooterItem) {
            footerResId = m.getLayoutResId();
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    sparseArray.put(item.multipleChildren.keyAt(i), m);
                }
            }
        }
        m.setAdapter(this);
        m.setContext(context);
        List<DelegateItem> list = new ArrayList<>();
        Set<Integer> sets = statusHandleItems.get(currentStatus).keySet();
        for (Integer set : sets) {
            if (set.intValue() != m.getLayoutResId()) {
                list.add(statusHandleItems.get(currentStatus).get(set));
            }
        }
        list.add(location, m);
        statusHandleItems.get(currentStatus).clear();
        for (DelegateItem item : list) {
            statusHandleItems.get(currentStatus).put(item.getLayoutResId(), item);
        }
        return this;
    }

    public <M extends DelegateItem> RecyclerDelegateAdapter unregisterItem(@NonNull M m) {
        m.setAdapter(null);
        m.setContext(null);
        if (m instanceof FooterItem) {
            footerResId = 0;
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    sparseArray.remove(item.multipleChildren.keyAt(i));
                }
            }
        }
        statusHandleItems.get(currentStatus).remove(m.getLayoutResId());
        return this;
    }

    public <M extends DelegateItem> RecyclerDelegateAdapter unregisterItem(@NonNull M m, int statusValue) {
        m.setAdapter(null);
        m.setContext(null);
        if (m instanceof FooterItem) {
            footerResId = 0;
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    sparseArray.remove(item.multipleChildren.keyAt(i));
                }
            }
        }
        statusHandleItems.get(statusValue).remove(m.getLayoutResId());
        return this;
    }

    public void clearMultiItem() {
        statusHandleItems.get(currentStatus).clear();
        sparseArray.clear();
    }

    public void clearAllStatusMultiItems(){
        statusHandleItems.clear();
        sparseArray.clear();
    }

    public void clearPointStatusMultiItem(int value){
        statusHandleItems.get(value).clear();
        sparseArray.clear();
    }

    public void setCurrentStatus(int statusValue){
        currentStatus = statusValue;
    }

    public void setDifferentStatus(@IntRange(from = 1, to = Integer.MAX_VALUE) int statusValue){
        currentStatus = statusValue;
        statusHandleItems.put(statusValue, new LinkedHashMap<Integer, DelegateItem>());
    }

    private int footerResId;


    public void setFooterStatusLoading() {
        FooterItem item = (FooterItem) statusHandleItems.get(currentStatus).get(footerResId);
        if (item != null) {
            item.setFooterLoadingStatus();
        }
    }

    public void setFooterStatusLoadMore() {
        FooterItem item = (FooterItem) statusHandleItems.get(currentStatus).get(footerResId);
        if (item != null) {
            item.setFooterStatusLoadNoMore();
        }
    }

    public void setFooterStatusLoadNoMore() {
        FooterItem item = (FooterItem) statusHandleItems.get(currentStatus).get(footerResId);
        if (item != null) {
            item.setFooterStatusLoadMore();
        }
    }

    public void setFooterStatusLoadError() {
        FooterItem item = (FooterItem) statusHandleItems.get(currentStatus).get(footerResId);
        if (item != null) {
            item.setFooterStatusLoadError();
        }
    }

    public void setFooterStatusGone(){
        FooterItem item = (FooterItem) statusHandleItems.get(currentStatus).get(footerResId);
        if (item != null) {
            item.setFooterStatusGone();
        }
    }

    public int getFooterStatus() {
        FooterItem item = (FooterItem) statusHandleItems.get(currentStatus).get(footerResId);
        if (item != null){
            return item.getFooterStatus();
        }
        return 0;
    }

}
