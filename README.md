# ScrollShapeUI
> 在做[CloudReader](https://github.com/youlookwhat/CloudReader)时，为了模仿网易云音乐UI，其中类似歌单详情页很重要，在此记录，其中很多点还是值得关注的，如果觉得对你有用，请Star支持一下。

### 涉及到的点：
 - 1、Activity页面跳转共享元素曲线动画
 - 2、Glide加载固定图片大小，解决切换页面时图片闪烁问题；加载状态监听等
 - 3、Toolbar背景图显示布局高斯模糊图底部的一部分
 - 4、滑动渐变Toolbar背景图
 - 5、NestedScrollView在Api23下的滑动兼容
 
### 实现思路：
- 1、Activity设置自定义Shared Element切换动画
- 2、透明状态栏（透明Toolbar,使背景图上移）
- 3、Toolbar底部增加和背景一样的高斯模糊图，并上移图片（为了使背景图的底部作为Toolbar的背景）
- 4、上下滑动，通过NestedScrollView拿到移动的高度，同时调整Toolbar的背景图透明度

### 效果图
|网易云音乐App原图|实现的效果图|
|:--:|:--:|
| ![网易云音乐App原图.gif](https://github.com/youlookwhat/ScrollShapeUI/blob/master/pic/yuan.gif)| ![模仿的效果图.gif](https://github.com/youlookwhat/ScrollShapeUI/blob/master/pic/scrollshapeui.gif) |


> 详细说明文章请看：[http://www.jianshu.com/p/1995b7135073](http://www.jianshu.com/p/1995b7135073)

