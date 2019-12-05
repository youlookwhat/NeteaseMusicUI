package com.example.jingbin.scrollshapeui.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
    private int headerContentHeight;
    // 是否已置顶
    private boolean isTop = false;
    private boolean isPageScroll = false;

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
                isTop = false;
                onScrollY1 = y;
                if (y > headerContentHeight) {
                    isTop = true;
                    y = headerContentHeight;
                }
//                if (listener2 != null) {
//                    listener2.onScroll(y);
//                }
//                if (listener3 != null) {
//                    listener3.onScroll(y);
//                }
                Log.e("onScroll", "" + y);
                ViewUtil.setMarginHeight(binding.llHeaderContent, -y);
            }
        });
        fragment2.setScrollListener(new OnFragmentListener() {
            @Override
            public void onScroll(int y) {
                isTop = false;
                onScrollY2 = y;
                if (y > headerContentHeight) {
                    isTop = true;
                    y = headerContentHeight;
                }
//                if (listener1 != null) {
//                    listener1.onScroll(y);
//                }
//                if (listener3 != null) {
//                    listener3.onScroll(y);
//                }
                Log.e("onScroll", "" + y);
                ViewUtil.setMarginHeight(binding.llHeaderContent, -y);
            }
        });
        fragment3.setScrollListener(new OnFragmentListener() {
            @Override
            public void onScroll(int y) {
                isTop = false;
                onScrollY3 = y;
                if (y > headerContentHeight) {
                    isTop = true;
                    y = headerContentHeight;
                }
//                if (listener1 != null) {
//                    listener1.onScroll(y);
//                }
//                if (listener2 != null) {
//                    listener2.onScroll(y);
//                }
                Log.e("onScroll", "" + y);
                ViewUtil.setMarginHeight(binding.llHeaderContent, -y);
            }
        });

        /**从第一个tab滑到第二个tab时，会自动触发第二个tab的onPageScrolled事件，导致第三个tab也会置顶！*/
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.e("onPageScrolled", "i--" + i + ";i1---" + i1);
                isPageScroll = true;
                int currentItem = binding.viewPager.getCurrentItem();
                Log.e("currentItem", "currentItem--" + currentItem);

                if (currentItem == 0) {
                    if (!isHavedSc) {
                        if (onScrollY1 < headerContentHeight) {
                            isHavedSc = true;
                            int y = onScrollY1;
                            onScrollY2 = y;
                            if (listener2 != null) {
                                listener2.onScroll(y);
                            }
                            ViewUtil.setMarginHeight(binding.llHeaderContent, -y);
                        } else {
                            if (onScrollY2 <= headerContentHeight) {
                                onScrollY2 = headerContentHeight;
                                if (listener2 != null) {
                                    listener2.onScroll(onScrollY2);
                                }
                            }
                        }
                    }
                } else if (currentItem == 1) {
                    if (!isHavedSc && onScrollY2 < headerContentHeight) {
                        isHavedSc = true;
                        int y = onScrollY2;
                        onScrollY1 = y;
                        onScrollY3 = y;
                        if (listener1 != null) {
                            listener1.onScroll(y);
                        }
                        if (listener3 != null) {
                            listener3.onScroll(y);
                        }
                        ViewUtil.setMarginHeight(binding.llHeaderContent, -y);

                    } else if (!isHavedSc && onScrollY2 >= headerContentHeight) {

                        if (onScrollY1 <= headerContentHeight) {
                            onScrollY1 = headerContentHeight;
                            if (listener1 != null) {
                                listener1.onScroll(onScrollY1);
                            }
                        }
                        if (onScrollY3 <= headerContentHeight) {
                            onScrollY3 = headerContentHeight;
                            if (listener3 != null) {
                                listener3.onScroll(onScrollY3);
                            }
                        }
                    }
                } else if (currentItem == 2) {
                    if (!isHavedSc && onScrollY3 < headerContentHeight) {
                        isHavedSc = true;
                        int y = onScrollY3;
                        onScrollY2 = y;
                        if (listener2 != null) {
                            listener2.onScroll(y);
                        }
                        ViewUtil.setMarginHeight(binding.llHeaderContent, -y);

                    } else if (!isHavedSc && onScrollY3 >= headerContentHeight) {

                        if (onScrollY2 <= headerContentHeight) {
                            onScrollY2 = headerContentHeight;
                            if (listener2 != null) {
                                listener2.onScroll(onScrollY2);
                            }
                        }
                    }
                }

            }

            @Override
            public void onPageSelected(int i) {
                Log.e("onPageSelected", "--");
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.e("StateChanged", "--" + i);
                isHavedSc = false;
            }
        });
    }

    private boolean isHavedSc = false;
    private int onScrollY1;
    private int onScrollY2;
    private int onScrollY3;

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

    private OnActivityListener listener1;

    public OnActivityListener getListener1() {
        return listener1;
    }

    public void setListener1(OnActivityListener listener1) {
        this.listener1 = listener1;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}
