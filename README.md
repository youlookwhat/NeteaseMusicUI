# ScrollShapeUI
仿网易云音乐歌单详情页
> 在做[CloudReader](https://github.com/youlookwhat/CloudReader)时，为了模仿网易云音乐UI，其中类似歌单详情页很重要，在此记录，其中很多点还是值得关注的。

#### 涉及到的点：
 - 1.Activity设置自定义Shared Element切换动画
 - 2.透明状态栏
 - 3.Glide的拓展使用（高斯模糊、加载指定大小图片、加载状态监听） 
 - 4.NestedScrollView在Api23下的滑动兼容

#### 网易云音乐App原图
 ![网易云音乐App原图.gif](http://upload-images.jianshu.io/upload_images/1354448-761770bdf2e2ab04.gif?imageMogr2/auto-orient/strip)

#### 实现的效果图
 ![模仿的效果图.gif](http://upload-images.jianshu.io/upload_images/1354448-1c558a8f870ed615.gif?imageMogr2/auto-orient/strip) 
 
#### 细节点
 - 1.页面跳转共享元素曲线动画
 - 2.加载固定图片大小，解决切换页面时图片闪烁问题
 - 3.titlebar背景图显示布局高斯图底部的一部分
 - 4.滑动渐变toolbar背景图

详细文章请看：

> 如觉得可以继续优化，欢迎在Issues提出。