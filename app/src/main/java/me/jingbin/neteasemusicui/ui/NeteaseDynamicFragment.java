package me.jingbin.neteasemusicui.ui;

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

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;
import me.jingbin.neteasemusicui.R;
import me.jingbin.neteasemusicui.adapter.DynamicAdapter;
import me.jingbin.neteasemusicui.base.BaseFragment;
import me.jingbin.neteasemusicui.databinding.FragmentNeteaseDynamicBinding;
import me.jingbin.neteasemusicui.databinding.HeaderDynamicBinding;
import me.jingbin.neteasemusicui.impl.OnActivityListener;
import me.jingbin.neteasemusicui.impl.OnFragmentListener;
import me.jingbin.neteasemusicui.utils.DataUtil;
import me.jingbin.neteasemusicui.utils.DensityUtil;

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
    private LinearLayoutManager layoutManager;

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
        Log.e("mType111---mIsVisible", "" + mType + "---" + mIsVisible);
        initAdapter();
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        Log.e("mType222---mIsVisible", "" + mType + "---" + mIsVisible);
        initAdapter();
    }

    private void initAdapter() {
        headerBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.header_dynamic, (ViewGroup) bindingView.recyclerView.getParent(), false);
        recyclerView = getView(R.id.recyclerView);
        mAdapter = new DynamicAdapter(DataUtil.get(20));
        layoutManager = new LinearLayoutManager(activity);
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

        DensityUtil.formatHeight(headerBinding.viewHeader, activity.getmHeaderHeight());

        bindingView.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // dy: 上拉为正，下滑为负
//                totalDy = Math.abs(totalDy) + dy;
                if (mIsVisible) {
                    totalDy -= dy;
                    if (scrollInterface != null) {
                        scrollInterface.onScroll(-totalDy);
                    }
                }
            }
        });

        if ("PL".equals(mType)) {
            activity.setListener1(listener);
        } else if ("ZF".equals(mType)) {
            activity.setListener2(listener);
        } else {
            activity.setListener3(listener);
        }
        mIsFirst = false;
    }

    private OnActivityListener listener = new OnActivityListener() {
        @Override
        public void onScroll(int y) {
            Log.e("mType---mIsVisible", "" + mType + "---" + mIsVisible);
            if (!mIsVisible) {
                totalDy = -y;
                layoutManager.scrollToPositionWithOffset(0, -y);
            }
        }
    };

    private OnFragmentListener scrollInterface;

    public OnFragmentListener getScrollInterface() {
        return scrollInterface;
    }

    public void setScrollListener(OnFragmentListener scrollInterface) {
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
            scrollInterface.onScroll((int) (y2 - y1));
        }
//        return super.onTouchEvent(event);
        return false;
    }
}
