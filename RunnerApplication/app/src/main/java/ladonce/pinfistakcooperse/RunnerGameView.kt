package ladonce.pinfistakcooperse

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.absoluteValue
import kotlin.properties.Delegates

class RunnerGameView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attributeSet, defStyleAttr), SurfaceHolder.Callback {
    companion object {
        const val TIME_BETWEEN_DRAWING = 10
        const val TIME_BETWEEN_DRAWING_PLAYER = 100
        const val SPEED_RUN = 12
    }

    var textViewCounter: TextView? = null
    private var count = 0
        set(value) {
            field = value
            GlobalScope.launch(Dispatchers.Main) {
                textViewCounter?.text = "POINTS:\n ${value}"
            }

        }
    lateinit var thread: Thread
    private val mPaint = Paint()
    private var mPrevDrawTime = 0L
    private var mPrevDrawPlayer = 0L
    var currentTime = 0L
    var flagRunning: Boolean = false
    private val backgroundBitmap by lazy {
        BitmapFactory.decodeStream(context.assets.open("background.png")).scale(
            width * 4, height, true
        )
    }
    private var backGroundLeftPosition = 0f
    private val playerRunningSprite: Bitmap =
        BitmapFactory.decodeStream(context.assets.open("running_sprite.png"))
    private val playerJumpingSprite: Bitmap =
        BitmapFactory.decodeStream(context.assets.open("jumping_sprite.png"))
    private val obstacleBitmap =
        BitmapFactory.decodeStream(context.assets.open("obstacle.png")).run {
            scale(this.width * 3 / 2, this.height * 3 / 2, true)
        }
    private val frameWidth = playerRunningSprite.width / 3
    private val frameHeight = playerRunningSprite.height / 2

    private var currentFrameRunning by Delegates.observable(0) { _, oldValue: Int, newValue: Int ->
        frameToDrawRunning.apply {
            left = newValue % 3 * frameWidth
            top = newValue / 3 * frameHeight
            right = left + frameWidth
            bottom = top + frameHeight
        }
    }
    private val frameToDrawRunning = Rect(0, 0, frameWidth, frameHeight)
    private var currentFrameJumping by Delegates.observable(0) { _, oldValue: Int, newValue: Int ->
        frameToDrawJumping.apply {
            left = newValue % 3 * frameWidth
            top = newValue / 3 * frameHeight
            right = left + frameWidth
            bottom = top + frameHeight
        }
    }
    private val frameToDrawJumping = Rect(0, 0, frameWidth, frameHeight)

    private var isRunning = true
    private var isJumping = AtomicBoolean(false)
    private var isFlying = false
    private var jumpDirection = JumpDirection.UP

    private val positionPlayer: PointF = PointF(100f + frameWidth / 2f, height * 5 / 8f + frameHeight / 2f)
    private var positionObstacle: Float = 0f
        set(value) {
            field =
                if (value <= -obstacleBitmap.width.toFloat())
                    width.toFloat()
                else
                    value
        }

    private var heightJumping = 0f
        set(value) {
            field = when {
                value >= 600f -> {
                    jumpDirection = JumpDirection.DOWN
                    600f
                }
                value <= 0f -> {
                    jumpDirection = JumpDirection.UP
                    isFlying = false
                    currentFrameJumping++
                    0f
                }
                else -> value
            }
            whereToDraw.apply {
                top = height * 5 / 8f - field
                positionPlayer.y = top + frameHeight / 2f
                bottom = top + frameHeight.toFloat()
            }
        }
    val whereToDraw: RectF = RectF().apply {
        left = 100f
        top = height * 5 / 8f
        right = left+ frameWidth.toFloat()
        bottom = top + frameHeight.toFloat()
    }

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        flagRunning = true
        thread = Thread {
            mPrevDrawPlayer = mPrevDrawTime
            while (flagRunning) {
                currentTime = getTime()
                if (currentTime - mPrevDrawTime < TIME_BETWEEN_DRAWING) {
                    continue
                }
                val canvas = holder.lockCanvas()
                try {
                    canvas?.let {
                            drawBackground(it)
                            drawObstacle(it)
                            if (isRunning)
                                drawRunning(it)
                            if (isJumping.get()) {
                                drawJumping(it)
                            }
                            if (isLose()) {
                                onFinishGame()
                                flagRunning = false

                            }
                    }
                } catch (e: IllegalMonitorStateException) {
                    e.printStackTrace()
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas)
                    }
                }
                mPrevDrawTime = currentTime
            }
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        count = 0
        isRunning = true
        isJumping = AtomicBoolean(false)
        heightJumping = 0f
        currentFrameRunning = 0
        currentFrameJumping = 0
        backGroundLeftPosition = 0f
        jumpDirection = JumpDirection.UP
        positionObstacle = width.toFloat()*2
        thread.start()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        flagRunning = false
        thread.join()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    isRunning = false
                    isJumping.compareAndSet(false, true)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getTime(): Long {
        return System.nanoTime() / 1_000_000
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawBitmap(
            backgroundBitmap,
            backGroundLeftPosition,
            0f,
            mPaint
        )
        backGroundLeftPosition -= SPEED_RUN
        if (backGroundLeftPosition.absoluteValue > backgroundBitmap.width - width)
            backGroundLeftPosition = -width.toFloat()
    }

    private fun drawRunning(canvas: Canvas) {
        if (currentTime - mPrevDrawPlayer > TIME_BETWEEN_DRAWING_PLAYER) {
            if (++currentFrameRunning == 6)
                currentFrameRunning = 0
            mPrevDrawPlayer = currentTime
        }
        canvas.drawBitmap(
            playerRunningSprite,
            frameToDrawRunning,
            whereToDraw,
            mPaint
        )
    }

    private fun drawJumping(canvas: Canvas) {
        canvas.drawBitmap(
            playerJumpingSprite,
            frameToDrawJumping,
            whereToDraw,
            mPaint
        )
        if (currentTime - mPrevDrawPlayer > TIME_BETWEEN_DRAWING_PLAYER) {
            if (currentFrameJumping < 3)
                currentFrameJumping++
            else if (currentFrameJumping == 3) {
                isFlying = true
            } else {
                if (++currentFrameJumping == 5) {
                    isJumping.set(false)
                    isRunning = true
                    currentFrameJumping = 0
                    count++

                }
            }
            mPrevDrawPlayer = currentTime
        }
        if (isFlying) {
            when (jumpDirection) {
                JumpDirection.UP -> {
                    heightJumping += 20
                }
                JumpDirection.DOWN -> heightJumping -= 20
            }
        }

    }

    private fun drawObstacle(canvas: Canvas) {
        canvas.drawBitmap(obstacleBitmap, positionObstacle, height * 11 / 16f, mPaint)
        positionObstacle -= SPEED_RUN
    }

    private fun onFinishGame() {
        context.startActivity(Intent(context, ResultGameActivity::class.java).apply {
            putExtra("my_count", count)
        })
    }

    private fun isLose(): Boolean {
        if (positionPlayer.x in (positionObstacle + 20f..positionObstacle + obstacleBitmap.width - 20f) && positionPlayer.y in (height * 11 / 16f..height * 11 / 16f + obstacleBitmap.height))
            return true
        return false

    }
}