package com.example.jingbin.scrollshapeui.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingbin.scrollshapeui.R;
import com.example.jingbin.scrollshapeui.adapter.DynamicAdapter;
import com.example.jingbin.scrollshapeui.base.BaseFragment;
import com.example.jingbin.scrollshapeui.databinding.FragmentNeteaseDynamicBinding;
import com.example.jingbin.scrollshapeui.databinding.HeaderDynamicBinding;
import com.example.jingbin.scrollshapeui.impl.OnFragmentScrollInterface;
import com.example.jingbin.scrollshapeui.utils.DataUtil;
import com.example.jingbin.scrollshapeui.utils.DensityUtil;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

/**
 * @author jingbin
 */
public class NeteaseDynamicFragment extends BaseFragment<FragmentNeteaseDynamicBinding> implements View.OnTouchListener {

    private static final String TYPE = "mType";
    private String mType = "Android";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private DynamicAdapter mAdapter;
    private ByRecyclerView recyclerView;
    private int page = 1;
    private NeteaseDynamicDetailActivity activity;
    private HeaderDynamicBinding headerBinding;
    private int totalDy;
    private float y1;
    private float y2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (NeteaseDynamicDetailActivity) context;
    }

    public static NeteaseDynamicFragment newInstance(String type) {
        NeteaseDynamicFragment fragment = new NeteaseDynamicFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public int setContent() {
        return R.layout.fragment_netease_dynamic;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 准备就绪
        mIsPrepared = true;
        initAdapter();
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        initAdapter();
    }

    private void initAdapter() {
        headerBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.header_dynamic, (ViewGroup) bindingView.recyclerView.getParent(), false);
        recyclerView = getView(R.id.recyclerView);
        mAdapter = new DynamicAdapter(DataUtil.get(20));
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // 加了分割线，滚动条才会置顶
        recyclerView.addItemDecoration(new SpacesItemDecoration(recyclerView.getContext(), SpacesItemDecoration.VERTICAL, 1));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addHeaderView(headerBinding.getRoot());
        recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 4) {
                            recyclerView.loadMoreEnd();
                            return;
                        }
                        if (page == 2) {
                            page++;
                            recyclerView.loadMoreFail();
                            return;
                        }
                        page++;
                        mAdapter.addData(DataUtil.get(20));
                        recyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });

        DensityUtil.formatHeight(headerBinding.viewHeader, activity.getmHeaderHeight(), 1);

//        bindingView.recyclerView.setOnTouchListener(this);
        bindingView.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                // dy: 上拉为正，下滑为负
//                totalDy = Math.abs(totalDy) + dy;
                totalDy -= dy;
                if (scrollInterface != null) {
                    scrollInterface.onScrool(-totalDy);
                }
            }
        });

        mIsFirst = false;
    }

    private OnFragmentScrollInterface scrollInterface;

    public OnFragmentScrollInterface getScrollInterface() {
        return scrollInterface;
    }

    public void setScrollInterface(OnFragmentScrollInterface scrollInterface) {
        this.scrollInterface = scrollInterface;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //当手指移动的时候
            y2 = event.getY();
            if (y1 - y2 > 50) {
//                Toast.makeText(MyServiceTestActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
            } else if (y2 - y1 > 50) {
//                Toast.makeText(MyServiceTestActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.i("Lgq", "sssssssll离开了lllll==");
            y2 = event.getY();
            scrollInterface.onScrool((int) (y2 - y1));
        }
//        return super.onTouchEvent(event);
        return false;
    }
}
