package com.yzx.delegate;

import android.content.Context;
import android.support.annotation.NonNull;
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
 * Created by yangzhenxiang on 2018/6/18.
 */

public class DelegateManager {

    public static final int NORMAL_STATUS = 0;

    private int currentStatus = NORMAL_STATUS;


    /*缓存每个部局文件对应的DelegateItem， 提升onCreateCommonViewHolder效率*/
    private SparseArray<DelegateItem> delegateArray;

    private SparseArray<LinkedHashMap<Integer, DelegateItem>> statusHandleItems;

    private SparseArray<Integer> footers;

    public DelegateManager(Context ctx, LayoutInflater inflater){
        context = ctx;
        factory = inflater;
        delegateArray = new SparseArray<>();
        statusHandleItems = new SparseArray<>();
        footers = new SparseArray<>();
        statusHandleItems.put(DelegateManager.NORMAL_STATUS,
                new LinkedHashMap<Integer, DelegateItem>());

    }

    public SparseArray<DelegateItem> getDelegateArray() {
        return delegateArray;
    }

    public SparseArray<LinkedHashMap<Integer, DelegateItem>> getStatusHandleItems() {
        return statusHandleItems;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public <T extends DelegateItem> T getItemByTag(int resId) {
        return (T) getStatusHandleItems().get(getCurrentStatus()).get(resId);
    }

    public static final int NO_LAYOUT_RESOURCE_FLAG = -432432424;  //最好定义为别人不知道的，负责，


    protected Context context;

    private LayoutInflater factory;

    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType != NO_LAYOUT_RESOURCE_FLAG) {
            CommonViewHolder CommonViewHolder =
                    new CommonViewHolder(factory.inflate(viewType, parent, false));
            DelegateItem item = getStatusHandleItems().get(getCurrentStatus()).get(viewType);
            if (item == null) {
                item = getDelegateArray().get(viewType);
            }
            CommonViewHolder.setTag(item);
            return CommonViewHolder;
        } else {
            //没有布局则填充一个 view
            return new CommonViewHolder(new View(context));
        }

    }

    public void onBindViewHolder(CommonViewHolder holder, int position) {
        if (holder.getTag() != null) {
            DelegateItem item = (DelegateItem) holder.getTag();
            item.convert(holder, position - item.getScopeStartPosition(), position);
        }
    }

    public int getItemViewType(int position) {
        for (Integer integer : getStatusHandleItems().get(getCurrentStatus()).keySet()) {
            DelegateItem item = getStatusHandleItems().get(getCurrentStatus()).get(integer);
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

    public int getItemCount() {
        int count = 0;
        for (Integer integer : getStatusHandleItems().get(getCurrentStatus()).keySet()) {
            getStatusHandleItems().get(getCurrentStatus()).get(integer).setScopeStartPosition(count);
            count += getStatusHandleItems().get(getCurrentStatus()).get(integer).getCount();
        }
        return count;
    }



    public int getFooterResId() {
        return footers.get(currentStatus);
    }

    public DelegateItem getCurrentFooterItem(){
        return getStatusHandleItems().get(getCurrentStatus()).get(getFooterResId());
    }

    public <M extends DelegateItem> void registerItem(@NonNull M m) {
        m.setContext(context);
        if (m instanceof FooterItem) {
            footers.put(currentStatus, m.getLayoutResId());
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    getDelegateArray().put(item.multipleChildren.keyAt(i), m);
                }
            }
        }
        getStatusHandleItems().get(getCurrentStatus()).put(m.getLayoutResId(), m);
    }

    public <M extends DelegateItem> void registerItem(@NonNull M m, int location) {
         if (m instanceof FooterItem) {
             footers.put(currentStatus, m.getLayoutResId());
         }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    getDelegateArray().put(item.multipleChildren.keyAt(i), m);
                }
            }
        }
        m.setContext(context);
        List<DelegateItem> list = new ArrayList<>();
        Set<Integer> sets = getStatusHandleItems().get(getCurrentStatus()).keySet();
        for (Integer set : sets) {
            if (set.intValue() != m.getLayoutResId()) {
                list.add(getStatusHandleItems().get(getCurrentStatus()).get(set));
            }
        }
        list.add(location, m);
        getStatusHandleItems().get(getCurrentStatus()).clear();
        for (DelegateItem item : list) {
            getStatusHandleItems().get(getCurrentStatus()).put(item.getLayoutResId(), item);
        }

    }

    public <M extends DelegateItem> void unregisterItem(@NonNull M m) {
        m.setAdapter(null);
        m.setContext(null);
        if (m instanceof FooterItem) {
            footers.remove(currentStatus);
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    getDelegateArray().remove(item.multipleChildren.keyAt(i));
                }
            }
        }
        getStatusHandleItems().get(getCurrentStatus()).remove(m.getLayoutResId());
    }

    public <M extends DelegateItem> void unregisterItem(@NonNull M m, int statusValue) {
        m.setAdapter(null);
        m.setContext(null);
        if (m instanceof FooterItem) {
            footers.remove(currentStatus);
        }
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    getDelegateArray().remove(item.multipleChildren.keyAt(i));
                }
            }
        }
        getStatusHandleItems().get(statusValue).remove(m.getLayoutResId());

    }


}
