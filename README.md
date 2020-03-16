# NeteaseMusicUI
仿网易云歌单详情页【已全新改写】

### 知识点
 - 1、Activity页面跳转共享元素曲线动画
 - 2、Glide加载固定图片大小，解决切换页面时图片闪烁问题；加载状态监听等
 - 3、Toolbar背景图显示布局高斯模糊图底部的一部分
 - 5、RecyclerView滑动渐变Toolbar背景图、改变覆盖View的marginTop
 
### 实现思路：
- 1、透明状态栏
- 2、Activity设置自定义Shared Element切换动画
- 3、Toolbar底部为高斯模糊图，并上移图片（为了使背景图的底部作为Toolbar的背景）
- 4、上下滑动，通过RecyclerView拿到移动的高度，同时调整Toolbar的背景图透明度

### 效果图
|网易云音乐App原图|实现的效果图|
|:--:|:--:|
| ![网易云音乐App原图.gif](https://github.com/youlookwhat/NeteaseMusicUI/blob/master/pic/yuan.gif)| ![模仿的效果图.gif](https://github.com/youlookwhat/ScrollShapeUI/blob/master/pic/scrollshapeui.gif) |


