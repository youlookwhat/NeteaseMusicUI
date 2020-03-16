package me.jingbin.neteasemusicui.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ArcMotion;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import me.jingbin.neteasemusicui.databinding.ActivityNeteasePlayBinding;
import me.jingbin.neteasemusicui.databinding.HeaderDynamicBinding;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.neteasemusicui.R;
import me.jingbin.neteasemusicui.bean.DynamicBean;
import me.jingbin.neteasemusicui.utils.CommonUtils;
import me.jingbin.neteasemusicui.utils.CustomChangeBounds;
import me.jingbin.neteasemusicui.utils.DataUtil;
import me.jingbin.neteasemusicui.utils.DensityUtil;
import me.jingbin.neteasemusicui.utils.StatusBarUtil;
import me.jingbin.neteasemusicui.utils.ViewUtil;

import static me.jingbin.neteasemusicui.utils.StatusBarUtil.getStatusBarHeight;


/**
 * Created by jingbin on 2020/3/15.
 * 使用的是 RV上面覆盖一层布局，然后给RV添加HeaderView，
 * HeaderView目的纯粹是让RV下移[覆盖布局]的高度，然后滑动RV的时候让[覆盖布局]也跟着滑动。
 * PS:
 * 这样处理纯粹是为了使用 转场动画。
 * 因为如果把[覆盖布局]直接作为HeaderView的话，转场动画不生效，不然直接作为HeaderView添加效果最好。
 */
public class NeteasePlaylistActivity extends AppCompatActivity {

    public final static String IMAGE_URL_LARGE = "https://upload-images.jianshu.io/upload_images/1354448-49d5a95614966128.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
    public final static String PARAM = "isRecyclerView";
    private ActivityNeteasePlayBinding binding;
    private BaseRecyclerAdapter<DynamicBean> adapter;
    private boolean isRecyclerView = true;
    // HeaderView的高度
    private int headerHeight;
    // 在多大范围内变色
    private int slidingDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_netease_play);
        // 状态栏透明
        StatusBarUtil.setTranslucentImageHeader(this, 0, binding.titleToolBar);
        isRecyclerView = getIntent().getBooleanExtra(PARAM, true);
        binding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                headerHeight = binding.include.getRoot().getHeight();

                setMotion();
                setTitleBar();
                setHeaderPicture();
                handleTitleLayout();
                setData();
            }
        });
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
     * 高斯背景图和一般图片
     */
    private void setHeaderPicture() {
        Glide.with(this)
                .load(IMAGE_URL_LARGE)
                .override((int) CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R.dimen.movie_detail_height))
                .into(binding.include.ivOnePhoto);

        DensityUtil.formatHeight(binding.include.imgItemBg, headerHeight);
        // "14":模糊度；"3":图片缩放3倍后再进行模糊
        Glide.with(this)
                .load(IMAGE_URL_LARGE)
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                // 设置高斯模糊
                .transform(new BlurTransformation(40, 8))
                .into(binding.include.imgItemBg);
    }

    /**
     * 处理TitleBar布局
     */
    private void handleTitleLayout() {
        // 标题栏高斯背景高度
        DensityUtil.formatHeight(binding.ivTitleHeadBg, headerHeight);
        setImgHeaderBg();

        // toolbar的高度
        int toolbarHeight = binding.titleToolBar.getLayoutParams().height;
        // toolbar+状态栏的高度
        final int headerBgHeight = toolbarHeight + getStatusBarHeight(this);

        // 使背景图向上移动到图片的最底端，保留toolbar+状态栏的高度
        binding.ivTitleHeadBg.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = binding.ivTitleHeadBg.getLayoutParams();
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams) binding.ivTitleHeadBg.getLayoutParams();
        int marginTop = params.height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);
        binding.ivTitleHeadBg.setImageAlpha(0);

        int height58 = DensityUtil.dip2px(this, 58);
        int height10 = DensityUtil.dip2px(this, 10);
        int titleBarAndStatusHeight = (height58 + getStatusBarHeight(this));
        slidingDistance = headerHeight - titleBarAndStatusHeight - height10;
    }

    /**
     * TitleBar高斯背景模糊图，成功后背景设为透明
     */
    private void setImgHeaderBg() {
        Glide.with(this)
                .load(NeteasePlaylistActivity.IMAGE_URL_LARGE)
                .error(R.drawable.stackblur_default)
                .transform(new BlurTransformation(40, 8))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.titleToolBar.setBackgroundColor(Color.TRANSPARENT);
                        binding.ivTitleHeadBg.setImageAlpha(0);
                        binding.ivTitleHeadBg.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(binding.ivTitleHeadBg);
    }

    private void setData() {
        binding.recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setAdapter();
            }
        }, 600);
    }

    /**
     * 设置RecyclerView
     */
    private void setAdapter() {
        HeaderDynamicBinding headerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.header_dynamic, (ViewGroup) binding.recyclerView.getParent(), false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        adapter = new BaseRecyclerAdapter<DynamicBean>(R.layout.item_list) {
            @Override
            protected void bindView(BaseByViewHolder<DynamicBean> holder, DynamicBean bean, int position) {
                holder.setText(R.id.tv_text, !isRecyclerView ? bean.getContent() : position + "、" + bean.getContent());
            }
        };
        binding.recyclerView.setAdapter(adapter);
        DensityUtil.formatHeight(headerBinding.viewHeader, headerHeight);
        binding.recyclerView.addHeaderView(headerBinding.getRoot());

        if (isRecyclerView) {
            adapter.setNewData(DataUtil.get(20));
            binding.recyclerView.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (adapter.getData().size() == 40) {
                        binding.recyclerView.loadMoreEnd();
                    } else {
                        adapter.addData(DataUtil.get(20));
                        binding.recyclerView.loadMoreComplete();
                    }
                }
            }, 1000);
        } else {
            adapter.setNewData(DataUtil.getContent());
        }

        // 监听改变透明度
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalDy;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // dy: 上拉为正，下滑为负
                totalDy -= dy;
                ViewUtil.setMarginHeight(binding.include.getRoot(), totalDy);
                scrollChangeHeader(-totalDy);
            }
        });
    }

    /**
     * 根据页面滑动距离改变Header透明度方法
     */
    private void scrollChangeHeader(int scrolledY) {
//        Log.e("---scrolledY:  ", scrolledY + "");
//        Log.e("-----slidingDistance: ", slidingDistance + "");
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);
        Drawable drawable = binding.ivTitleHeadBg.getDrawable();
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

    public static void start(Activity context, ImageView imageView, boolean isRV) {
        Intent intent = new Intent(context, NeteasePlaylistActivity.class);
        intent.putExtra(PARAM, isRV);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                imageView,
                //与xml文件对应
                CommonUtils.getString(R.string.transition_book_img));
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
