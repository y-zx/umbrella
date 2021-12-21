package com.yzx.delegate.layoutmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yzx.delegate.layoutmanager.layout.LinearLayoutHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PositionLayoutHelperFinder extends LayoutHelperFinder {

    @NonNull
    private List<LayoutHelper> mLayoutHelpers = new LinkedList<>();

    @NonNull
    private Comparator<LayoutHelper> mLayoutHelperItemComparator = new Comparator<LayoutHelper>() {
        @Override
        public int compare(LayoutHelper lhs, LayoutHelper rhs) {
            return lhs.hashCode() - rhs.hashCode();
        }
    };

    @Override
    void setLayouts(@Nullable List<LayoutHelper> layouts) {
        mLayoutHelpers.clear();
        mReverseLayoutHelpers.clear();
        if (layouts != null) {
            ListIterator<LayoutHelper> iterator = layouts.listIterator();
            LayoutHelper helper = null;
            while (iterator.hasNext()) {
                helper = iterator.next();
                mLayoutHelpers.add(helper);
            }

            while (iterator.hasPrevious()) {
                mReverseLayoutHelpers.add(iterator.previous());
            }
            // Collections.sort(mLayoutHelperItems, mLayoutHelperItemComparator);
            Collections.sort(mLayoutHelpers, mLayoutHelperItemComparator);
        }
    }

    @Nullable
    @Override
    public LayoutHelper getLayoutHelper(int position) {
        if (finderListener != null) {
            int layoutHelperHashCode = finderListener.getLayoutHelper(position);
            for (LayoutHelper mLayoutHelper : mLayoutHelpers) {
                if (mLayoutHelper.hashCode() == layoutHelperHashCode) {
                    return mLayoutHelper;
                }
                if (layoutHelperHashCode == 0) {
                    // 默认LinearLayoutHelper
                    if (LinearLayoutHelper.class.getName().equals(mLayoutHelper.getClass().getName())) {
                        return mLayoutHelper;
                    }
                }
            }
        }
        return null;
    }

    @NonNull
    @Override
    protected List<LayoutHelper> getLayoutHelpers() {
        return mLayoutHelpers;
    }


    @NonNull
    private List<LayoutHelper> mReverseLayoutHelpers = new LinkedList<>();

    @Override
    protected List<LayoutHelper> reverse() {
        return mReverseLayoutHelpers;
    }

    private LayoutHelperFinderListener finderListener;

    public void setFinderListener(LayoutHelperFinderListener finderListener) {
        this.finderListener = finderListener;
    }

    public interface LayoutHelperFinderListener {
        int getLayoutHelper(int position);
    }
}
