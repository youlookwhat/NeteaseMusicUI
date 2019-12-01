package com.example.jingbin.scrollshapeui.adapter;

import com.example.jingbin.scrollshapeui.R;
import com.example.jingbin.scrollshapeui.bean.DynamicBean;

import java.util.List;

import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

/**
 * @author jingbin
 */
public class DynamicAdapter extends BaseRecyclerAdapter<DynamicBean> {

    public DynamicAdapter(int layoutId) {
        super(layoutId);
    }

    public DynamicAdapter(List<DynamicBean> data) {
        super(R.layout.item_dynamic, data);
    }

    @Override
    protected void bindView(BaseByViewHolder<DynamicBean> holder, DynamicBean bean, int position) {
        holder.setText(R.id.tv_text, bean.getContent() + "" + position);
    }
}
