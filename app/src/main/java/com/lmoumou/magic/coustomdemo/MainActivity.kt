package com.lmoumou.magic.coustomdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val handler: Handler by lazy { Handler() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAnimationButton.setOnClickListener {
            mAnimationButton.startAnim()
            handler.postDelayed({
                MDActivity.startThis(this@MainActivity, mAnimationButton)
                mAnimationButton.reset()
            }, 2000)

        }
    }
}
