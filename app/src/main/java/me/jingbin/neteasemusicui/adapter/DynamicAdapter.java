package me.jingbin.neteasemusicui.adapter;

import java.util.List;

import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.neteasemusicui.R;
import me.jingbin.neteasemusicui.bean.DynamicBean;

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
