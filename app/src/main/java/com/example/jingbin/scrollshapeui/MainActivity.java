package com.example.jingbin.scrollshapeui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.jingbin.scrollshapeui.databinding.ActivityMainBinding;
import com.example.jingbin.scrollshapeui.ui.NeteasePlaylistActivity;
import com.example.jingbin.scrollshapeui.utils.CommonUtils;

/**
 * Link to: https://github.com/youlookwhat/ScrollShapeUI
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
                .crossFade(500)
                .override((int) CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R.dimen.movie_detail_height))
                .into(binding.ivSongList);

        // RecyclerView列表显示
        binding.tvRecyclerview.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                NeteasePlaylistActivity.start(MainActivity.this, binding.ivSongList, true);
            }
        });
        // 一般文本显示
        binding.tvTxtShow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                NeteasePlaylistActivity.start(MainActivity.this, binding.ivSongList, false);
            }
        });
    }
}
