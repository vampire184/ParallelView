package com.yangzhenyu.parallelview.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.yangzhenyu.parallelview.R

/**
 * 自定义容器持有ViewPager
 * 然后对外暴露一个setUp方法，用来初始化ViewPager
 * setUp方法中传递的是每个页面的布局ID
 */
class ParallelContainer : FrameLayout, ViewPager.OnPageChangeListener {

    private lateinit var mViewPager: ViewPager
    private lateinit var mWoman: ImageView
    private lateinit var mAdapter: ParallelAdapter
    private var mList: ArrayList<ParallelFragment> = ArrayList()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setUp(vararg ids: Int) {
        mViewPager = ViewPager(context)
        mViewPager.id = R.id.parallel_view_pager

        //遍历布局ID，创建Fragment
        for (id in ids) {
            val fragment = ParallelFragment()
            val bundle = Bundle()
            bundle.putInt("layoutId", id)
            fragment.arguments = bundle
            mList.add(fragment)
        }

        //初始化Adapter
        mAdapter = ParallelAdapter((context as AppCompatActivity).supportFragmentManager)
        mAdapter.setUpFragments(mList)
        mViewPager.adapter = mAdapter
        mViewPager.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        //给ViewPager增加滑动监听
        mViewPager.addOnPageChangeListener(this)

        addView(mViewPager)
    }

    fun setWoman(image: ImageView) {
        mWoman = image
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //position源码中的含义是ViewPager第一个可见Fragment的下标，因此我们主要是对第一个可见的Fragment做动画
        var outFragment: ParallelFragment? = mList.getOrNull(position)

        if (outFragment !=null){
            val views = outFragment.getParallelViews()
            for (view in views){
                val tag:ParallelViewTag? = view.getTag(R.id.parallel_view_tag) as ParallelViewTag?
                if (tag != null){
                    //根据滑动的距离乘以相应的系数，动态改变view的属性（位置、透明度）等
                    view.translationX = 0-positionOffsetPixels*tag.xOut
                    view.translationY = 0-positionOffsetPixels*tag.yOut
                    val alpha = positionOffsetPixels*tag.alphaOut/width
                    view.alpha = if (alpha>1) 0.0f else 1.0f-alpha
                }
            }
        }

    }

    override fun onPageSelected(position: Int) {
        //当滑动到最后一个Fragment的时候隐藏小人
        if (position == mAdapter.count - 1) mWoman.visibility =
            View.INVISIBLE else mWoman.visibility = View.VISIBLE
    }

    override fun onPageScrollStateChanged(state: Int) {
        val animationDrawable:AnimationDrawable = mWoman.background as AnimationDrawable
        when (state){
            //拖动屏幕的时候，执行人物的帧动画，让人物动起来
            ViewPager.SCROLL_STATE_DRAGGING -> {
                animationDrawable.start()
            }
            //停止拖动的时候，暂停帧动画
            ViewPager.SCROLL_STATE_IDLE -> {
                animationDrawable.stop()
            }
        }
    }
}