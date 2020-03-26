package me.jingbin.neteasemusicui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import me.jingbin.neteasemusicui.databinding.ActivityMainBinding;
import me.jingbin.neteasemusicui.ui.NeteaseDynamicDetailActivity;
import me.jingbin.neteasemusicui.ui.NeteasePlaylistActivity;
import me.jingbin.neteasemusicui.ui.NeteasePlaylistStickyActivity;
import me.jingbin.neteasemusicui.utils.CommonUtils;

/**
 * Link to: https://github.com/youlookwhat/NeteaseMusicUI
 * Contact me: http://www.jianshu.com/u/e43c6e979831
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setTitle("仿网易云音乐歌单详情页");
        Glide.with(this)
                .load(NeteasePlaylistActivity.IMAGE_URL_LARGE)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .override((int) CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R.dimen.movie_detail_height))
                .into(binding.ivSongList);

        // 一般文本显示
        binding.tvTxtShow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                NeteasePlaylistActivity.start(MainActivity.this, binding.ivSongList, true);
//                NeteasePlaylistActivity.start(MainActivity.this, binding.ivSongList, false);
            }
        });
        // RecyclerView
        binding.tvRecyclerview.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                NeteasePlaylistStickyActivity.start(MainActivity.this, binding.ivSongList, true);
            }
        });

        binding.tvSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NeteaseDynamicDetailActivity.class));
            }
        });
    }
}
