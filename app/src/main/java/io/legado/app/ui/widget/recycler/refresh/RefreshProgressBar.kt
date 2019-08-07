package io.legado.app.ui.widget.recycler.refresh

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import io.legado.app.R

class RefreshProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    internal var a = 1
    var maxProgress = 100
    private var durProgress = 0
    var secondMaxProgress = 100
    private var secondDurProgress = 0
    var bgColor = 0x00000000
    var secondColor = -0x3e3e3f
    var fontColor = -0xc9c9ca
    var speed = 1
    var secondFinalProgress = 0
        private set
    private var paint: Paint = Paint()
    private var rect = Rect()
    private var rectF = RectF()
    var isAutoLoading: Boolean = false
        set(loading) {
            if (loading && visibility != VISIBLE) {
                visibility = VISIBLE
            }
            field = loading
            if ((!this.isAutoLoading)) {
                secondDurProgress = 0
                secondFinalProgress = 0
            }
            maxProgress = 0

            invalidate()
        }

    init {
        paint.style = Paint.Style.FILL

        val a = context.obtainStyledAttributes(attrs, R.styleable.RefreshProgressBar)
        speed = a.getDimensionPixelSize(R.styleable.RefreshProgressBar_speed, speed)
        maxProgress = a.getInt(R.styleable.RefreshProgressBar_max_progress, maxProgress)
        durProgress = a.getInt(R.styleable.RefreshProgressBar_dur_progress, durProgress)
        secondDurProgress =
            a.getDimensionPixelSize(R.styleable.RefreshProgressBar_second_dur_progress, secondDurProgress)
        secondFinalProgress = secondDurProgress
        secondMaxProgress =
            a.getDimensionPixelSize(R.styleable.RefreshProgressBar_second_max_progress, secondMaxProgress)
        bgColor = a.getColor(R.styleable.RefreshProgressBar_bg_color, bgColor)
        secondColor = a.getColor(R.styleable.RefreshProgressBar_second_color, secondColor)
        fontColor = a.getColor(R.styleable.RefreshProgressBar_font_color, fontColor)
        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = bgColor
        rect.set(0, 0, measuredWidth, measuredHeight)
        canvas.drawRect(rect, paint)

        if (secondDurProgress > 0 && secondMaxProgress > 0) {
            var secondDur = secondDurProgress
            if (secondDur < 0) {
                secondDur = 0
            }
            if (secondDur > secondMaxProgress) {
                secondDur = secondMaxProgress
            }
            paint.color = secondColor
            val tempW = (measuredWidth.toFloat() * 1.0f * (secondDur * 1.0f / secondMaxProgress)).toInt()
            rect.set(measuredWidth / 2 - tempW / 2, 0, measuredWidth / 2 + tempW / 2, measuredHeight)
            canvas.drawRect(rect, paint)
        }

        if (durProgress > 0 && maxProgress > 0) {
            paint.color = fontColor
            rectF.set(
                0f,
                0f,
                measuredWidth.toFloat() * 1.0f * (durProgress * 1.0f / maxProgress),
                measuredHeight.toFloat()
            )
            canvas.drawRect(rectF, paint)
        }

        if (this.isAutoLoading) {
            if (secondDurProgress >= secondMaxProgress) {
                a = -1
            } else if (secondDurProgress <= 0) {
                a = 1
            }
            secondDurProgress += a * speed
            if (secondDurProgress < 0)
                secondDurProgress = 0
            else if (secondDurProgress > secondMaxProgress)
                secondDurProgress = secondMaxProgress
            secondFinalProgress = secondDurProgress
            invalidate()
        } else {
            if (secondDurProgress != secondFinalProgress) {
                if (secondDurProgress > secondFinalProgress) {
                    secondDurProgress -= speed
                    if (secondDurProgress < secondFinalProgress) {
                        secondDurProgress = secondFinalProgress
                    }
                } else {
                    secondDurProgress += speed
                    if (secondDurProgress > secondFinalProgress) {
                        secondDurProgress = secondFinalProgress
                    }
                }
                this.invalidate()
            }
            if (secondDurProgress == 0 && durProgress == 0 && secondFinalProgress == 0 && visibility == VISIBLE) {
                visibility = View.INVISIBLE
            }
        }
    }

    fun getDurProgress(): Int {
        return durProgress
    }

    fun setDurProgress(durProgress: Int) {
        var durProgress = durProgress
        if (durProgress < 0) {
            durProgress = 0
        }
        if (durProgress > maxProgress) {
            durProgress = maxProgress
        }
        this.durProgress = durProgress
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.invalidate()
        } else {
            this.postInvalidate()
        }
    }

    fun getSecondDurProgress(): Int {
        return secondDurProgress
    }

    fun setSecondDurProgress(secondDur: Int) {
        this.secondDurProgress = secondDur
        this.secondFinalProgress = secondDurProgress
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.invalidate()
        } else {
            this.postInvalidate()
        }
    }

    fun setSecondDurProgressWithAnim(secondDur: Int) {
        var secondDur = secondDur
        if (secondDur < 0) {
            secondDur = 0
        }
        if (secondDur > secondMaxProgress) {
            secondDur = secondMaxProgress
        }
        this.secondFinalProgress = secondDur
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.invalidate()
        } else {
            this.postInvalidate()
        }
    }

    fun clean() {
        durProgress = 0
        secondDurProgress = 0
        secondFinalProgress = 0
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.invalidate()
        } else {
            this.postInvalidate()
        }
    }
}
