package com.lmoumou.magic.coustomdemo

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import kotlinx.android.synthetic.main.activity_md.*

/**
 * Author: Lmoumou
 * Date: 2018-07-17 10:46
 * 文件名: MDActivity
 * 描述:
 */
class MDActivity : AppCompatActivity() {

    companion object {
        fun startThis(activity: AppCompatActivity, abt: AnimationButton) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, abt, abt.transitionName)
            val intent = Intent(activity.applicationContext, MDActivity::class.java)
            activity.startActivity(intent, options.toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_md)
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            animateRevealShow(mImageView.left + mImageView.width / 2, mImageView.top + mImageView.height / 2)
        }
    }

    private fun animateRevealShow(centerX: Int, centerY: Int) {
        val mAnimator = ViewAnimationUtils.createCircularReveal(mLinearLayout, centerX, centerY,
                (mImageView.width / 2).toFloat(), mLinearLayout.height.toFloat())

        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.start()
    }
}