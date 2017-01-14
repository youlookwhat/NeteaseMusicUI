package com.example.jingbin.scrollshapeui.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.jingbin.scrollshapeui.R;
import com.example.jingbin.scrollshapeui.adapter.ListAdapter;
import com.example.jingbin.scrollshapeui.databinding.ActivityMovieDetailBinding;
import com.example.jingbin.scrollshapeui.utils.CommonUtils;
import com.example.jingbin.scrollshapeui.utils.CustomChangeBounds;
import com.example.jingbin.scrollshapeui.utils.StatusBarUtil;
import com.example.jingbin.scrollshapeui.view.MyNestedScrollView;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.example.jingbin.scrollshapeui.utils.StatusBarUtil.getStatusBarHeight;

/**
 * Created by jingbin on 2017/1/9.
 * 高仿网易云音乐歌单详情页
 * 实现思路：
 * 1、Activity设置自定义Shared Element切换动画
 * 2、透明状态栏（透明Toolbar,使背景图上移）
 * 3、Toolbar底部增加和背景一样的高斯模糊图，并上移图片（为了使背景图的底部作为Toolbar的背景）
 * 4、上下滑动，通过NestedScrollView拿到移动的高度，同时调整Toolbar的背景图透明度
 */

public class NeteasePlaylistActivity extends AppCompatActivity {

    public final static String IMAGE_URL_LARGE = "https://img5.doubanio.com/lpic/s4477716.jpg";
    public final static String IMAGE_URL_SMALL = "https://img5.doubanio.com/spic/s4477716.jpg";
    public final static String IMAGE_URL_MEDIUM = "https://img5.doubanio.com/mpic/s4477716.jpg";
    public final static String PARAM = "isRecyclerView";
    private ActivityMovieDetailBinding binding;
    // 这个是高斯图背景的高度
    private int imageBgHeight;
    // 在多大范围内变色
    private int slidingDistance;
    private boolean isRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        if (getIntent() != null) {
            isRecyclerView = getIntent().getBooleanExtra(PARAM, true);
        }

        setMotion();
        setTitleBar();
        setPicture();
        initSlideShapeTheme();

        // RecyclerView列表显示
        if (isRecyclerView) {
            setAdapter();
        } else {// 显示一般文本
            setText();
        }
    }

    /**
     * 设置自定义 Shared Element切换动画
     */
    private void setMotion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //定义ArcMotion
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumHorizontalAngle(50f);
            arcMotion.setMinimumVerticalAngle(50f);
            //插值器，控制速度
            Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

            //实例化自定义的ChangeBounds
            CustomChangeBounds changeBounds = new CustomChangeBounds();
            changeBounds.setPathMotion(arcMotion);
            changeBounds.setInterpolator(interpolator);
            changeBounds.addTarget(binding.include.ivOnePhoto);
            //将切换动画应用到当前的Activity的进入和返回
            getWindow().setSharedElementEnterTransition(changeBounds);
            getWindow().setSharedElementReturnTransition(changeBounds);
        }
    }

    /**
     * 高斯背景图和一般图片
     */
    private void setPicture() {
        Glide.with(this)
                .load(IMAGE_URL_LARGE)
                .override((int) CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R.dimen.movie_detail_height))
                .into(binding.include.ivOnePhoto);

        // "14":模糊度；"3":图片缩放3倍后再进行模糊
        Glide.with(this)
                .load(IMAGE_URL_MEDIUM)
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(this, 14, 3))
                .into(binding.include.imgItemBg);
    }

    /**
     * 显示文本
     */
    private void setText() {
        binding.tvTxt.setVisibility(View.VISIBLE);
        binding.xrvList.setVisibility(View.GONE);
    }

    /**
     * 设置RecyclerView
     */
    private void setAdapter() {
        binding.tvTxt.setVisibility(View.GONE);
        binding.xrvList.setVisibility(View.VISIBLE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.xrvList.setLayoutManager(mLayoutManager);
        // 需加，不然滑动不流畅
        binding.xrvList.setNestedScrollingEnabled(false);
        binding.xrvList.setHasFixedSize(false);
        final ListAdapter adapter = new ListAdapter(this);
        adapter.notifyDataSetChanged();
        binding.xrvList.setAdapter(adapter);
    }

    /**
     * toolbar设置
     */
    private void setTitleBar() {
        setSupportActionBar(binding.titleToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        binding.titleToolBar.setTitle("1988：我想和这个世界谈谈");
        binding.titleToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    /**
     * 初始化滑动渐变
     */
    private void initSlideShapeTheme() {
        setImgHeaderBg();

        // toolbar的高度
        int toolbarHeight = binding.titleToolBar.getLayoutParams().height;
        // toolbar+状态栏的高度
        final int headerBgHeight = toolbarHeight + StatusBarUtil.getStatusBarHeight(this);

        // 使背景图向上移动到图片的最底端，保留toolbar+状态栏的高度
        binding.ivTitleHeadBg.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = binding.ivTitleHeadBg.getLayoutParams();
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams) binding.ivTitleHeadBg.getLayoutParams();
        int marginTop = params.height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);
        binding.ivTitleHeadBg.setImageAlpha(0);

        // 为头部是View的界面设置状态栏透明
        StatusBarUtil.setTranslucentImageHeader(this, 0, binding.titleToolBar);

        ViewGroup.LayoutParams imgItemBgparams = binding.include.imgItemBg.getLayoutParams();
        // 获得高斯图背景的高度
        imageBgHeight = imgItemBgparams.height;

        // 监听改变透明度
        initScrollViewListener();
    }


    /**
     * 加载titlebar背景,加载后将背景设为透明
     */
    private void setImgHeaderBg() {
        Glide.with(this).load(NeteasePlaylistActivity.IMAGE_URL_MEDIUM)
//                .placeholder(R.drawable.stackblur_default)
                .error(R.drawable.stackblur_default)
                .bitmapTransform(new BlurTransformation(this, 14, 3))// 设置高斯模糊
                .listener(new RequestListener<String, GlideDrawable>() {//监听加载状态
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        binding.titleToolBar.setBackgroundColor(Color.TRANSPARENT);
                        binding.ivTitleHeadBg.setImageAlpha(0);
                        binding.ivTitleHeadBg.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(binding.ivTitleHeadBg);
    }

    private void initScrollViewListener() {
        // 为了兼容api23以下
        binding.nsvScrollview.setOnMyScrollChangeListener(new MyNestedScrollView.ScrollInterface() {
            @Override
            public void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChangeHeader(scrollY);
            }
        });

        int titleBarAndStatusHeight = (int) (CommonUtils.getDimens(R.dimen.nav_bar_height) + getStatusBarHeight(this));
        slidingDistance = imageBgHeight - titleBarAndStatusHeight - (int) (CommonUtils.getDimens(R.dimen.nav_bar_height_more));
    }

    /**
     * 根据页面滑动距离改变Header透明度方法
     */
    private void scrollChangeHeader(int scrolledY) {

//        DebugUtil.error("---scrolledY:  " + scrolledY);
//        DebugUtil.error("-----slidingDistance: " + slidingDistance);

        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);
        Drawable drawable = binding.ivTitleHeadBg.getDrawable();
//        DebugUtil.error("----alpha:  " + alpha);

        if (drawable != null) {
            if (scrolledY <= slidingDistance) {
                // title部分的渐变
                drawable.mutate().setAlpha((int) (alpha * 255));
                binding.ivTitleHeadBg.setImageDrawable(drawable);
            } else {
                drawable.mutate().setAlpha(255);
                binding.ivTitleHeadBg.setImageDrawable(drawable);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.xrvList.setFocusable(false);
    }

    /**
     * @param context        activity
     * @param imageView      imageView
     * @param isRecyclerView 是否为列表
     */
    public static void start(Activity context, ImageView imageView, boolean isRecyclerView) {
        Intent intent = new Intent(context, NeteasePlaylistActivity.class);
        intent.putExtra(PARAM, isRecyclerView);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                imageView, CommonUtils.getString(R.string.transition_book_img));//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
