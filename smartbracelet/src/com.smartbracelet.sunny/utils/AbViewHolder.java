package com.smartbracelet.sunny.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * <p>描述：超简洁的ViewHolder.</p>
 * 代码更简单，性能略低，可以忽略
 *
 * @author ~若相惜
 * @version v1.0
 * @date 2015-8-8 下午2:39:25
 */
public class AbViewHolder {

    /**
     * ImageView view = AbViewHolder.get(convertView, R.id.imageView);
     *
     * @param view
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
