package com.example.jingbin.scrollshapeui.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.jingbin.scrollshapeui.R;
import com.example.jingbin.scrollshapeui.databinding.ActivityDynamicDetailBinding;
import com.example.jingbin.scrollshapeui.impl.OnActivityListener;
import com.example.jingbin.scrollshapeui.impl.OnFragmentListener;
import com.example.jingbin.scrollshapeui.utils.CommonUtils;
import com.example.jingbin.scrollshapeui.utils.DensityUtil;
import com.example.jingbin.scrollshapeui.utils.ViewUtil;
import com.example.jingbin.scrollshapeui.view.MyFragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by jingbin on 2019/12/1.
 * 高仿网易云音乐动态详情页
 * 实现思路：
 * 1.整体页面 全部内容是一个ViewPager 上面覆盖一层布局
 * 2.每一个fragment里都是一个RV，给RV加一个头布局，高度是[上面覆盖一层布局]的高度
 * 3.滑动RV，对应移动覆盖布局的高度，最高为0到TabLayout的高度
 * 4.RV下滑时，覆盖布局的高度变化会影响其他Fragment的RV的头布局的高度
 */
public class NeteaseDynamicDetailActivity extends AppCompatActivity {

    private ArrayList<String> mTitleList = new ArrayList<>(3);
    private ArrayList<Fragment> mFragments = new ArrayList<>(3);
    private int headerContentHeight ;

    public final static String IMAGE_URL_LARGE = "https://img3.doubanio.com/view/subject/l/public/s4477716.jpg";
    public final static String IMAGE_URL_MEDIUM = "https://img3.doubanio.com/view/subject/m/public/s4477716.jpg";
    private ActivityDynamicDetailBinding binding;
    private int mHeaderHeight;
    private NeteaseDynamicFragment fragment1;
    private NeteaseDynamicFragment fragment2;
    private NeteaseDynamicFragment fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dynamic_detail);
        initHeaderView();

    }

    private void initView() {
        initFragmentList();
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        binding.viewPager.setAdapter(myAdapter);
        binding.viewPager.setOffscreenPageLimit(2);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        myAdapter.notifyDataSetChanged();
        initScrollListener();
    }

    private void initScrollListener() {
        headerContentHeight = mHeaderHeight - DensityUtil.dip2px(this, 40);
        Log.e("headerContentHeight", "" + headerContentHeight);
        fragment1.setScrollListener(new OnFragmentListener() {
            @Override
            public void onScroll(int y) {
                if (y > headerContentHeight) {
                    y = headerContentHeight;
                }
                if (listener2 != null) {
                    listener2.onScroll(y);
                }
                if (listener3 != null) {
                    listener3.onScroll(y);
                }
                Log.e("onScroll", "" + y);
                ViewUtil.setMarginHeight(binding.llHeaderContent, -y);
            }
        });
        fragment2.setScrollListener(new OnFragmentListener() {
            @Override
            public void onScroll(int y) {
                if (y > headerContentHeight) {
                    y = headerContentHeight;
                }
                if (listener != null) {
                    listener.onScroll(y);
                }
                if (listener3 != null) {
                    listener3.onScroll(y);
                }
                Log.e("onScroll", "" + y);
                ViewUtil.setMarginHeight(binding.llHeaderContent, -y);
            }
        });
        fragment3.setScrollListener(new OnFragmentListener() {
            @Override
            public void onScroll(int y) {
                if (y > headerContentHeight) {
                    y = headerContentHeight;
                }
                if (listener != null) {
                    listener.onScroll(y);
                }
                if (listener2 != null) {
                    listener2.onScroll(y);
                }
                Log.e("onScroll", "" + y);
                ViewUtil.setMarginHeight(binding.llHeaderContent, -y);
            }
        });
    }

    private void initHeaderView() {
        binding.tvDetailContent.setText("我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容");
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Glide.with(this)
                .load(IMAGE_URL_LARGE)
                .override((int) CommonUtils.getDimens(R.dimen.height_image),
                        (int) CommonUtils.getDimens(R.dimen.height_image))
                .into(binding.ivDynamic);

        binding.toolBar.post(new Runnable() {
            @Override
            public void run() {
                mHeaderHeight = binding.llHeader.getHeight();
                Log.e("mHeaderHeight:", "" + mHeaderHeight);
                initView();
            }
        });
    }

    public int getmHeaderHeight() {
        return mHeaderHeight;
    }

    public int getHeaderContentHeight() {
        return headerContentHeight;
    }

    public void setHeaderContentHeight(int headerContentHeight) {
        this.headerContentHeight = headerContentHeight;
    }

    public void setmHeaderHeight(int mHeaderHeight) {
        this.mHeaderHeight = mHeaderHeight;
    }

    private void initFragmentList() {
        mTitleList.clear();
        mTitleList.add("评论");
        mTitleList.add("转发");
        mTitleList.add("赞");
        fragment1 = NeteaseDynamicFragment.newInstance("PL");
        fragment2 = NeteaseDynamicFragment.newInstance("ZF");
        fragment3 = NeteaseDynamicFragment.newInstance("Z");
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
    }

    private OnActivityListener listener2;

    public OnActivityListener getListener2() {
        return listener2;
    }

    public void setListener2(OnActivityListener listener) {
        this.listener2 = listener;
    }

    private OnActivityListener listener3;

    public OnActivityListener getListener3() {
        return listener3;
    }

    public void setListener3(OnActivityListener listener) {
        this.listener3 = listener;
    }

    private OnActivityListener listener;

    public OnActivityListener getListener() {
        return listener;
    }

    public void setListener(OnActivityListener listener) {
        this.listener = listener;
    }
}
