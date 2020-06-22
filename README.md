# ParallelView
自定义平行控件时差动画

此项目模仿小红书引导页效果
采用Kotlin编写，核心原理是通过自定义布局，解析我们给系统控件的添加的一些自定义属性
然后在ViewPager的滚动监听中根据自定义属性修改view的属性。从而达到动画的效果

简单实现了view的位置和透明度的变化，理论上可以修改view的全部属性值，有需要可以自定添加自定义属性即可


效果图如下：



![预览效果](https://github.com/vampire184/ParallelView/blob/master/ScreenRecord.gif)

