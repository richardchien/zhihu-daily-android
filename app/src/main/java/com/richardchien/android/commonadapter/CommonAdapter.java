package com.richardchien.android.commonadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * CommonAdapterTest
 * Created by richard on 16/1/8.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected List<T> mDataList;
    protected Context mContext;
    protected int mLayoutId;

    public CommonAdapter(Context context, List<T> dataList, int layoutId) {
        mContext = context;
        mDataList = dataList;
        mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
//    public abstract View getView(int position, View convertView, ViewGroup parent);
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.instanceOf(mContext, convertView, parent, mLayoutId, position);
        onPostCreateView(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void onPostCreateView(ViewHolder holder, T t);
}
