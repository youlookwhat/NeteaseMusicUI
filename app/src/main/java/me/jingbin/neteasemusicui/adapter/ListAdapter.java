package me.jingbin.neteasemusicui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.jingbin.neteasemusicui.R;
import me.jingbin.neteasemusicui.utils.CommonUtils;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.NormalTextViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private String[] mTitles = null;

    public ListAdapter(Context context) {
        mTitles = CommonUtils.getStringArray(R.array.books);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }

    class NormalTextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        NormalTextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_text);
        }
    }
}
