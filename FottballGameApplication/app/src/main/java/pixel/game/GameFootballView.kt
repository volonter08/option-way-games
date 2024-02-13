package pixel.game

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.testcaseapplication.R
import pixel.game.model.BitmapHandler
import pixel.game.model.Pointer
import pixel.game.model.ValuePointer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.properties.Delegates


class GameFootballView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attributeSet, defStyleAttr), SurfaceHolder.Callback {
    companion object {
        const val TIME_BETWEEN_DRAWING = 10
    }
    lateinit var thread: Thread
    var canvas: Canvas? = null
    val mPaint = Paint()
    var textViewCounter: TextView? = null
    var mPrevDrawTime = 0L
    var flagRunning: Boolean = false
    lateinit var footballField: Bitmap
    val ballBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeStream(context.assets.open("ball.png")),
        100,
        100,
        true
    )
    val scaleBitmap = BitmapHandler.getBitmapFromXml(context,R.drawable.scale)
    val pointer: Pointer = Pointer(BitmapHandler.getBitmapFromXml(context,R.drawable.pointer),(2/3f)* scaleBitmap.width)
    lateinit var penaltyPoint: PointF
    lateinit var scalePosition: PointF
    var leftTouchLimit by Delegates.notNull<Float>()
    var rightTouchLimit by Delegates.notNull<Float>()
    var topTouchLimit by Delegates.notNull<Float>()
    var bottomTouchLimit by Delegates.notNull<Float>()
    var leftLimitGates by Delegates.notNull<Float>()
    var rightLimitGates by Delegates.notNull<Float>()
    var topLimitGates by Delegates.notNull<Float>()
    var bottomLimitGates by Delegates.notNull<Float>()
    val positionBallPostShot = ConcurrentLinkedQueue<PointF>()
    var isShot = AtomicBoolean(false)
    var isDrawShot = AtomicBoolean(false)
    var isPointerTranslate = AtomicBoolean(false)
    var count = 0
    var speedTranslatingPointer = 0.2f

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        flagRunning = true
        textViewCounter?.text = "GOLES:\n ${count}"
        thread = Thread {
            mPrevDrawTime = getTime()
            while (flagRunning) {
                canvas = holder.lockCanvas()
                val currentTime = getTime()
                try {
                    canvas?.let {
                        synchronized(this) {
                            drawFootballField(it)
                            if (currentTime - mPrevDrawTime < TIME_BETWEEN_DRAWING) {
                                return@let
                            }
                            if (isDrawShot.get()) {
                                drawShot(canvas!!, positionBallPostShot.poll())
                            }
                            if (isPointerTranslate.get()) {
                                drawTranslatePointer()
                                pointer.translate((speedTranslatingPointer * TIME_BETWEEN_DRAWING))
                            }
                        }
                    }
                } catch (e: IllegalMonitorStateException) {
                    e.printStackTrace()
                } finally {
                    if (canvas != null)
                        holder.unlockCanvasAndPost(canvas)
                }
                mPrevDrawTime = currentTime
            }
        }
        thread.start()
    }
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        synchronized(this) {
            footballField = Bitmap.createScaledBitmap(
                BitmapFactory.decodeStream(context.assets.open(if (height > width) "football_field.jpg" else "football_field_horizontal_orientation.jpg")),
                width,
                height,
                true
            )
            penaltyPoint = PointF(
                (width - ballBitmap.width) / 2f,
                if (height > width) ((33 / 48f) * height - ballBitmap.height / 2f) else ((42 / 48f) * height - ballBitmap.height / 2f)
            )
            scalePosition = PointF(
                if (height > width) (width - scaleBitmap.width) / 2f else width * 3 / 4f,
                (7 / 8f) * height - scaleBitmap.height / 2f
            )
            leftTouchLimit = penaltyPoint.x
            rightTouchLimit = penaltyPoint.x + ballBitmap.width
            topTouchLimit = penaltyPoint.y
            bottomTouchLimit = penaltyPoint.y + ballBitmap.height
            leftLimitGates = if(height>width) 0.225f * width + ballBitmap.width / 2f else 0.3795f * width + ballBitmap.width/2f
            rightLimitGates = if(height>width) (1 - 0.225f) * width + ballBitmap.width / 2f else( 1- 0.3795f) * width - ballBitmap.width/2f
            topLimitGates = if(height>width)0.325f * height + ballBitmap.height/2f else 0.39f * height + ballBitmap.height/2f
            bottomLimitGates = if(height>width) (0.48f) * height - ballBitmap.height / 2f else (0.61f) * height- ballBitmap.height/2f
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        count= 0
        speedTranslatingPointer= 0.1f
        pointer.apply {
            position= limitTranslating/2
        }
        flagRunning = false
        thread.join()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                ACTION_DOWN -> {
                    if (!isPointerTranslate.compareAndSet(true, false)) {
                        if (event.x in (leftTouchLimit..rightTouchLimit) && event.y in (topTouchLimit..bottomTouchLimit)) {
                            isShot.compareAndSet(false, true)
                        }
                    } else {
                        isDrawShot.compareAndSet(false, true)
                    }
                }

                ACTION_UP -> {
                    if (isShot.compareAndSet(true, false)) {
                        isPointerTranslate.compareAndSet(false, true)
                    }
                }

                ACTION_MOVE -> {
                    if (isShot.get()) {
                        positionBallPostShot.add(PointF(event.x, event.y))
                    }
                }
            }
        }
        return true
    }

    private fun getTime(): Long {
        return System.nanoTime() / 1_000_000
    }

    private fun drawFootballField(canvas: Canvas) {
        canvas.drawBitmap(
            footballField,
            Matrix(),
            mPaint
        )
        canvas.drawBitmap(
            scaleBitmap,
            Matrix().apply {
                setTranslate(scalePosition.x, scalePosition.y)
            },
            mPaint
        )
        if (!isDrawShot.get()) {
            canvas.drawBitmap(
                ballBitmap,
                Matrix().apply {
                    setTranslate(
                        penaltyPoint.x,penaltyPoint.y
                    )
                },
                mPaint
            )
        }
        if (!isPointerTranslate.get()) {
            canvas.drawBitmap(
                pointer.bitmap,
                Matrix().apply {
                    setTranslate(
                        scalePosition.x + scaleBitmap.width/6f - pointer.bitmap.width/2f + pointer.position ,scalePosition.y - pointer.bitmap.height
                    )
                },
                mPaint
            )
        }
    }

    private fun drawShot(canvas: Canvas, positionBall: PointF?) {
        if (positionBall != null) {
            canvas.drawBitmap(
                ballBitmap,
                Matrix().apply {
                    setTranslate(
                        positionBall.x - ballBitmap.width / 2f,
                        positionBall.y - ballBitmap.height / 2f
                    )
                },
                mPaint
            )
            if (positionBallPostShot.isEmpty()) {
                if (positionBall.x in (leftLimitGates..rightLimitGates) && positionBall.y in (topLimitGates..bottomLimitGates) && pointer.value == ValuePointer.GOOD) {
                    CoroutineScope(Dispatchers.Main).launch {
                        textViewCounter?.text = "GOLES:\n ${++count}"
                    }
                    speedTranslatingPointer += 0.1f
                } else {
                    onFinishGame()
                }
            }
        } else {
            isDrawShot.set(false)
        }
    }

    fun drawTranslatePointer() {
        canvas!!.drawBitmap(
            pointer.bitmap,
            Matrix().apply {
                setTranslate(scalePosition.x + scaleBitmap.width/6f - pointer.bitmap.width/2f + pointer.position ,scalePosition.y - pointer.bitmap.height)
            },
            mPaint
        )
    }

    private fun getBitmap(drawableRes: Int): Bitmap {
        val drawable = ResourcesCompat.getDrawable(resources, drawableRes, null)!!
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    private fun onFinishGame() {
        context.startActivity(Intent(context, ResultGameActivity::class.java).apply {
            putExtra("my_count", count)
        })
    }
}
