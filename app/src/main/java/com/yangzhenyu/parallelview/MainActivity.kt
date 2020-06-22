package com.yangzhenyu.parallelview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.yangzhenyu.parallelview.view.ParallelContainer

class MainActivity : AppCompatActivity() {

    private var mIds:IntArray = intArrayOf(
        R.layout.view_intro_1,
        R.layout.view_intro_2,
        R.layout.view_intro_3,
        R.layout.view_intro_4,
        R.layout.view_intro_5,
        R.layout.view_login
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val parallelContainer = findViewById<ParallelContainer>(R.id.parallel_container)
        val woman = findViewById<ImageView>(R.id.iv_man)
        parallelContainer.apply {
            this.setUp(*mIds)
            this.setWoman(woman)
        }
    }
}
