package com.yzx.delegate;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.yzx.delegate.holder.ViewHolder;
import com.yzx.delegate.items.CommonMultipleItem;
import com.yzx.delegate.items.DelegateItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


/**
 * @Author: yangzhenxiang
 * @Time: 2018/5/8
 * @Description: 代理adapter的中枢逻辑处理 管理类
 * @E-mail: yzxandroid981@163.com
 */
public class DelegateManager {

    /**
     * 缓存每个部局文件对应的DelegateItem， 提升onCreateCommonViewHolder效率
     */
    private SparseArray<DelegateItem> delegateArray;

    private LinkedHashMap<Integer, DelegateItem> statusHandleItems;

    public DelegateManager(Context ctx, LayoutInflater inflater) {
        context = ctx;
        factory = inflater;
        delegateArray = new SparseArray<>();
        statusHandleItems = new LinkedHashMap<>();
    }

    public SparseArray<DelegateItem> getDelegateArray() {
        return delegateArray;
    }

    public LinkedHashMap<Integer, DelegateItem> getStatusHandleItems() {
        return statusHandleItems;
    }

    public <T extends DelegateItem> T getItemByTag(int resId) {
        return (T) getStatusHandleItems().get(resId);
    }

    //最好定义为别人不知道的
    public static final int NO_LAYOUT_RESOURCE_FLAG = -432432424;


    protected Context context;

    private LayoutInflater factory;

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != NO_LAYOUT_RESOURCE_FLAG) {
            ViewHolder ViewHolder = new ViewHolder(factory.inflate(viewType, parent, false));
            DelegateItem item = getStatusHandleItems().get(viewType);
            if (item == null) {
                item = getDelegateArray().get(viewType);
            }
            ViewHolder.setTag(item);
            return ViewHolder;
        } else {
            //没有布局则填充一个 view
            return new ViewHolder(new View(context));
        }
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getTag() != null) {
            DelegateItem item = (DelegateItem) holder.getTag();
            item.convert(holder, position);
        }
    }

    public int getItemViewType(int position) {
        for (Integer integer : getStatusHandleItems().keySet()) {
            DelegateItem item = getStatusHandleItems().get(integer);
            if (item.handleItem(position)) {
                return item.getLayoutResId(position);
            }
        }

        return NO_LAYOUT_RESOURCE_FLAG;
    }

    public int getItemCount() {
        int count = 0;
        for (Integer integer : getStatusHandleItems().keySet()) {
            getStatusHandleItems().get(integer).setScopeStartPosition(count);
            count += getStatusHandleItems().get(integer).getCount();
        }
        return count;
    }

    private List<DelegateItem.DiffBean> itemList = new ArrayList<>();

    public List<DelegateItem.DiffBean> getItemList() {
        itemList.clear();
        for (Integer integer : getStatusHandleItems().keySet()) {
            List<DelegateItem.DiffBean> itemList = getStatusHandleItems().get(integer).getItemList();
            if (itemList != null && itemList.size() > 0) {
                this.itemList.addAll(itemList);
            }
        }
        return itemList;
    }

    public long getItemId(int position) {
        for (Integer integer : getStatusHandleItems().keySet()) {
            DelegateItem<?> item = getStatusHandleItems().get(integer);
            if (item.handleItem(position)) {
                return item.getItemId(position);
            }
        }
        return position;
    }

    /**
     * 2.0.0 新功能，支持GridLayoutManager
     */
    public int getSpanSize(int position) {
        for (Integer integer : getStatusHandleItems().keySet()) {
            DelegateItem item = getStatusHandleItems().get(integer);
            if (item.handleItem(position)) {
                return item.getSpanSize(position);
            }
        }
        //默认返回1
        return 1;
    }

    public <M extends DelegateItem> void registerItem(@NonNull M m) {
        m.setContext(context);
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    getDelegateArray().put(item.multipleChildren.keyAt(i), m);
                }
            }
        }
        m.registerCallBack(context);
        getStatusHandleItems().put(m.getLayoutResId(), m);
    }

    public <M extends DelegateItem> void registerItem(@NonNull M m, int location) {
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
        m.registerCallBack(context);
        List<DelegateItem> list = new ArrayList<>();
        Set<Integer> sets = getStatusHandleItems().keySet();
        for (Integer set : sets) {
            if (set != m.getLayoutResId()) {
                list.add(getStatusHandleItems().get(set));
            }
        }
        list.add(location, m);
        getStatusHandleItems().clear();
        for (DelegateItem item : list) {
            getStatusHandleItems().put(item.getLayoutResId(), item);
        }

    }

    public <M extends DelegateItem> void unregisterItem(@NonNull M m) {
        m.setAdapter(null);
        m.setContext(null);
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    getDelegateArray().remove(item.multipleChildren.keyAt(i));
                }
            }
        }
        getStatusHandleItems().remove(m.getLayoutResId());
    }

    public <M extends DelegateItem> void unregisterItem(@NonNull M m, int statusValue) {
        m.setAdapter(null);
        m.setContext(null);
        if (m instanceof CommonMultipleItem) {
            CommonMultipleItem item = (CommonMultipleItem) m;
            int size = item.multipleChildren.size();
            for (int i = 0; i < size; i++) {
                if (item.multipleChildren.keyAt(i) != 0) {
                    getDelegateArray().remove(item.multipleChildren.keyAt(i));
                }
            }
        }
        getStatusHandleItems().remove(m.getLayoutResId());
    }
}
