package com.lmoumou.magic.coustomdemo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator

/**
 * Author: Lmoumou
 * Date: 2018-07-16 18:04
 * 文件名: AnimationButton2
 * 描述:
 */
class AnimationButton(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    constructor(context: Context) : this(context, null)

    /**
     * 空间宽，高
     * */
    private var abWith: Int = 0
    private var abHeight: Int = 0

    /**
     * 背景画笔
     * */
    private val bgPaint: Paint by lazy { Paint() }

    /**
     * 背景颜色
     * */
    private var bgColor = Color.parseColor("#FFB6C1")

    /**
     * 背景圆角矩形大小
     * */
    private val rectf: RectF by lazy { RectF() }

    /**
     * 按钮文字内容
     * */
    private var buttonStr: String = ""

    /**
     * 文字画笔
     * */
    private val textPaint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    /**
     * 文字颜色
     * */
    private var textColor = Color.parseColor("#FFFFFF")

    /**
     * 文字大小
     * */
    private var textSize = 40F

    /**
     * 文字绘制所在的矩形
     * */
    private val textRect: Rect by lazy { Rect() }

    /**
     * 矩形到正方形的过度动画
     * */
    private var animatorRectToSquare: ValueAnimator? = null

    /**
     * 默认两圆心之间的距离
     * */
    private var defaultTwoCircleDistance: Int = 0

    /**
     * 两圆心间的距离
     * */
    private var twoCircleDistance: Int = 0

    /**
     * 动画集合
     * */
    private val animatorSet: AnimatorSet by lazy { AnimatorSet() }

    /**
     * 圆弧Paint
     * */
    private val arcPaint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    /**
     * 圆弧颜色
     * */
    private var arcColor = Color.parseColor("#FFB6C1")

    /**
     * 是否画圆弧
     * */
    private var isArc = false

    /**
     * 扇形所在矩形
     */
    private val rectfArc by lazy { RectF() }

    /**
     * 圆的半径
     */
    private var radius: Float = 0F


    /**
     * 圆弧角度
     * */
    private var startAngle = 0F

    /**
     * 是否是初始状态
     * */
    //private var isInit = true

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacksAndMessages(null)
    }

    init {

        val t = context.obtainStyledAttributes(attrs, R.styleable.AnimationButton)
        bgColor = t.getColor(R.styleable.AnimationButton_bgColor, Color.parseColor("#FFB6C1"))
        textColor = t.getColor(R.styleable.AnimationButton_textColor, Color.parseColor("#FFFFFF"))
        arcColor = t.getColor(R.styleable.AnimationButton_arcColor, Color.parseColor("#FFB6C1"))
        textSize = t.getDimension(R.styleable.AnimationButton_textSize, 40F)
        buttonStr = t.getString(R.styleable.AnimationButton_contentText)

        initBgPaint()

        initTextPaint()

        t.recycle()
    }

    /**
     * 初始化圆弧Pain
     * */
    private fun initArcPaint() {
        arcPaint.strokeWidth = radius / 10
        arcPaint.isAntiAlias = true
        arcPaint.strokeCap = Paint.Cap.ROUND
        arcPaint.style = Paint.Style.STROKE
        arcPaint.color = arcColor
    }

    /**
     * 初始化文字画笔
     * */
    private fun initTextPaint() {
        textPaint.textSize = textSize
        textPaint.color = textColor
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isAntiAlias = true

        textPaint.getTextBounds(buttonStr, 0, buttonStr.length, textRect)
    }

    /**
     * 初始化背景画笔
     * */
    private fun initBgPaint() {
        bgPaint.color = bgColor
        bgPaint.strokeWidth = 4F
        bgPaint.style = Paint.Style.FILL
        bgPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthModel = MeasureSpec.getMode(widthMeasureSpec)
        val heightModel = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)


        abHeight = if (heightModel == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            val textHeight = textRect.height()//文本高度
            paddingTop + textHeight + paddingBottom
        }

        abWith = if (widthModel == MeasureSpec.EXACTLY) {
            widthSize
        } else {
            val textWidth: Int = textRect.width()
            paddingLeft + textWidth + paddingRight
        }

        initArcPaint()

        defaultTwoCircleDistance = (abWith - abHeight) / 2
        radius = (Math.min(abHeight, abWith) / 2).toFloat()
        rectfArc.set(abWith / 2 - radius + arcPaint.strokeWidth,
                abHeight / 2 - radius + arcPaint.strokeWidth,
                abWith / 2 + radius - arcPaint.strokeWidth,
                abHeight / 2 + radius - arcPaint.strokeWidth)



        initAnimation()

        setMeasuredDimension(abWith, abHeight)
    }

    private fun initAnimation() {
        setRectToCircleAnimation()

        animatorSet.play(animatorRectToSquare)

        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                isArc = true
                handler.post(object : Runnable {
                    override fun run() {
                        startAngle += 5
                        if (startAngle == 360F) {
                            startAngle = 0F
                        }
                        postInvalidate()
                        handler.postDelayed(this, 10)
                    }
                })
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {
                //isInit = false
            }
        })
    }


    /**
     * 矩形到正方形
     * */
    private fun setRectToCircleAnimation() {
        animatorRectToSquare = ValueAnimator.ofInt(0, defaultTwoCircleDistance)
        animatorRectToSquare?.duration = 500
        animatorRectToSquare?.interpolator = AccelerateInterpolator()
        animatorRectToSquare?.addUpdateListener {
            twoCircleDistance = it.animatedValue as Int
            //Log.e("AnimationButton2","twoCircleDistance->$twoCircleDistance")
            var alpha = 255 - (twoCircleDistance * 255) / defaultTwoCircleDistance
            textPaint.alpha = alpha
            if (alpha < 255 / 2) alpha = 255 / 2
            bgPaint.alpha = alpha
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawBg(it)
            drawText(it)

            if (isArc) {
                drawArc(it)
            }
        }
    }

    /**
     * 画圆弧
     * */
    private fun drawArc(canvas: Canvas) {
        canvas.drawArc(rectfArc, startAngle, 200F, false, arcPaint)
    }

    /**
     * 画文字
     * */
    private fun drawText(canvas: Canvas) {
        textRect.left = 0
        textRect.top = 0
        textRect.right = abWith
        textRect.bottom = abHeight
        val fontMetrics = textPaint.fontMetricsInt
        val baseLine: Int = (textRect.top + textRect.bottom - fontMetrics.bottom - fontMetrics.top) / 2
        canvas.drawText(buttonStr, textRect.centerX().toFloat(), baseLine.toFloat(), textPaint)
    }

    /**
     * 画背景圆角矩形
     * */
    private fun drawBg(canvas: Canvas) {
        rectf.left = twoCircleDistance.toFloat()
        rectf.top = 0F
        rectf.right = (abWith - twoCircleDistance).toFloat()
        rectf.bottom = abHeight.toFloat()
        canvas.drawRoundRect(rectf, (abHeight / 2).toFloat(), (abHeight / 2).toFloat(), bgPaint)
    }

    /**
     * 开始动画
     * */
    fun startAnim() {
        animatorSet.start()

    }


    /**
     * 停止动画
     * */
    fun stop() {
        animatorSet.end()
    }


    /**
     * 重置
     * */
    fun reset() {
        handler.postDelayed({
            startAngle = 0F
            isArc = false
            textPaint.alpha = 255
            bgPaint.alpha = 255
            twoCircleDistance = 0
            defaultTwoCircleDistance = (abWith - abHeight) / 2
            postInvalidate()
            handler?.removeCallbacksAndMessages(null)
        }, 500)

    }


}