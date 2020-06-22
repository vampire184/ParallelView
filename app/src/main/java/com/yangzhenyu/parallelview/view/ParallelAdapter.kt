package com.yangzhenyu.parallelview.view

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ParallelAdapter: FragmentPagerAdapter {

    private lateinit var mList: ArrayList<ParallelFragment>

    constructor(fm:FragmentManager):super(fm)

    fun setUpFragments(list:ArrayList<ParallelFragment>){
        mList = list
    }

    override fun getItem(position: Int): ParallelFragment {
        return mList[position]
    }

    override fun getCount(): Int {
        return mList.size
    }
}