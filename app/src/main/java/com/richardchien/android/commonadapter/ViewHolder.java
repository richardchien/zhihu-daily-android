package com.richardchien.android.commonadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * CommonAdapterTest
 * Created by richard on 16/1/8.
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public static ViewHolder instanceOf(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public View getConvertView() {
        return mConvertView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);

        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T) view;
    }

    public int getPosition() {
        return mPosition;
    }

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        mPosition = position;
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public ViewHolder setViewText(int viewId, CharSequence text) {
        ReflectHelper.invokeMethodIfExists("setText", getView(viewId), new Class[]{CharSequence.class}, new Object[]{text});
        return this;
    }

    public ViewHolder setViewImageResource(int viewId, int resId) {
        ReflectHelper.invokeMethodIfExists("setImageResource", getView(viewId), new Class[]{int.class}, new Object[]{resId});
        return this;
    }

    public ViewHolder setViewImageBitmap(int viewId, Bitmap bitmap) {
        ReflectHelper.invokeMethodIfExists("setImageBitmap", getView(viewId), new Class[]{Bitmap.class}, new Object[]{bitmap});
        return this;
    }

    public ViewHolder setViewChecked(int viewId, boolean checked) {
        ReflectHelper.invokeMethodIfExists("setChecked", getView(viewId), new Class[]{boolean.class}, new Object[]{checked});
        return this;
    }

    public ViewHolder setViewOnClickListener(int viewId, View.OnClickListener listener) {
        ReflectHelper.invokeMethodIfExists("setOnClickListener", getView(viewId), new Class[]{View.OnClickListener.class}, new Object[]{listener});
        return this;
    }

    public ViewHolder setViewVisibility(int viewId, int visibility) {
        ReflectHelper.invokeMethodIfExists("setVisibility", getView(viewId), new Class[]{int.class}, new Object[]{visibility});
        return this;
    }

    public ViewHolder setViewTextColor(int viewId, int color) {
        ReflectHelper.invokeMethodIfExists("setTextColor", getView(viewId), new Class[]{int.class}, new Object[]{color});
        return this;
    }
}
