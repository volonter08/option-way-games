package tiger.collecting.coins

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.scale
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random


class TigerSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attributeSet, defStyleAttr), SurfaceHolder.Callback {
    companion object {
        const val TIME_BETWEEN_DRAWING = 20
        const val KOEF_SPEED_TRANSLATING_MINERAL_BETWEEN_DRAWING = 0.001f * TIME_BETWEEN_DRAWING
        const val TIME_BETWEEN_DRAWING_TIGER = 100
    }

    private lateinit var thread: Thread
    private var currentTime = 0L
    private var prevDrawTime = 0L
    private var prevDrawChameleonTime = 0L
    private var prevTimeAdding = 0L
    private var flagRunning: Boolean = false

    private var point = AtomicInteger(0)
    private var health = AtomicInteger(3)

    val listMinerals = LinkedList<Mineral>()
    val removeListMinerals = LinkedList<Mineral>()

    //for drawing
    private val paint = Paint()
    private val textPaint = Paint().apply {
        color = Color.rgb(255, 165, 0)
        textAlign = Paint.Align.LEFT
        typeface = ResourcesCompat.getFont(context, R.font.kenney_blocks)
        textSize = 120F
    }
    private val healthPaint = Paint().apply {
        color = Color.GREEN
    }
    private val tiger: Tiger = Tiger().apply {
        post {
            positionY = height * 15 / 32f
            limitPosition = width.toFloat() - tigerAnimator.frameWidthTigerPosition
        }
    }
    private var flyingMineral: Mineral = if (Random.nextInt(2) == 0) Diamond() else Stone()
    private var isMineralHide: Boolean = false
    private val tigerAnimator = TigerAnimator(context)
    private val backGroundBitmap: Bitmap by lazy {
        BitmapFactory.decodeStream(context.assets.open("background.jpg")).scale(width, height)
    }
    private val stoneBitmap =
        BitmapFactory.decodeStream(context.assets.open("minerals/stone.png")).let {
            it.scale(it.width, it.height)
        }
    private val diamondBitmap =
        BitmapFactory.decodeStream(context.assets.open("minerals/diamond.png")).let {
            it.scale(it.width, it.height)
        }
    private val platformBitmap: Bitmap by lazy {
        BitmapFactory.decodeStream(context.assets.open("minerals/platform.png"))
            .scale(width, height / 2)
    }
    private val startTouch = PointF()

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        point.set(0)
        health.set(3)
        healthPaint.color = Color.GREEN
        listMinerals.clear()
        removeListMinerals.clear()
        flagRunning = true
        thread = Thread {
            while (flagRunning) {
                currentTime = getTime()
                if (currentTime - prevDrawTime <= TIME_BETWEEN_DRAWING)
                    continue
                val canvas = holder.lockCanvas()
                try {
                    canvas?.let {
                        drawBackground(it)
                        drawMinerals(it)
                        drawTiger(it)
                        checkGameStatus()
                        it.drawText("" + point.get(), 20f, 120f, textPaint)
                        it.drawRect(
                            width - 200f,
                            30f,
                            width - 200 + health.get() * 60f,
                            80f,
                            healthPaint
                        )
                    }
                } catch (e: IllegalMonitorStateException) {
                    e.printStackTrace()
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas)
                    }
                    listMinerals.removeAll(removeListMinerals)
                }
                prevDrawTime = currentTime
            }
        }
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        flagRunning = false
        thread.join()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startTouch.apply {
                        x = event.x
                        y = event.y
                    }
                    tiger.isRunning.compareAndSet(false, true)
                }

                MotionEvent.ACTION_MOVE -> {
                    val translateX = event.x - startTouch.x
                    tiger.direction =
                        if (translateX > 0) DirectionTiger.RIGHT else DirectionTiger.LEFT
                    startTouch.x = event.x
                    tiger.positionX += translateX
                }

                MotionEvent.ACTION_UP -> {
                    tiger.isRunning.compareAndSet(true, false)
                }
            }
            return true
        }
        return true
    }

    private fun getTime(): Long {
        return System.nanoTime() / 1_000_000
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawBitmap(
            backGroundBitmap, 0f, 0f, paint
        )
        canvas.drawBitmap(
            platformBitmap, 0f, height / 2f, paint
        )
    }

    fun drawMinerals(canvas: Canvas) {
        flyingMineral.let {
            val drawingBitmap = when (it) {
                is Diamond -> diamondBitmap
                is Stone -> stoneBitmap
                else -> stoneBitmap
            }
            if (it.position.y < canvas.height + drawingBitmap.height) {
                if (!isMineralHide) {
                    canvas.drawBitmap(
                        drawingBitmap,
                        (canvas.width - drawingBitmap.width) / 2f,
                        it.position.y,
                        paint
                    )
                }
                it.position.apply {
                    y += KOEF_SPEED_TRANSLATING_MINERAL_BETWEEN_DRAWING * canvas.height
                }
            } else {
                isMineralHide = false
                flyingMineral = if (Random.nextInt(2) == 0) Diamond() else Stone()
            }
        }
    }

    private fun drawTiger(canvas: Canvas) {
        tigerAnimator.draw(canvas, tiger)
    }

    private fun checkGameStatus() {
        val drawingBitmap = when (flyingMineral) {
            is Diamond -> diamondBitmap
            is Stone -> stoneBitmap
            else -> stoneBitmap
        }
        if (!isMineralHide && (tiger.positionX in (width / 2f - drawingBitmap.width / 2f..width / 2f + drawingBitmap.width / 2f) || tiger.positionX + tigerAnimator.frameWidthTigerPosition in (width / 2f - drawingBitmap.width / 2f..width / 2f + drawingBitmap.width / 2f) || tiger.positionX < width / 2f - drawingBitmap.width / 2f && tiger.positionX+ tigerAnimator.frameWidthTigerPosition > width / 2f + drawingBitmap.width / 2f) && tiger.positionY in (flyingMineral.position.y..flyingMineral.position.y + drawingBitmap.height)
        ) {
            when (flyingMineral) {
                is Diamond -> {
                    point.incrementAndGet()
                    isMineralHide = true
                }

                is Stone -> {
                    health.decrementAndGet()
                    when (health.get()) {
                        2 -> healthPaint.setColor(Color.YELLOW)
                        1 -> healthPaint.setColor(Color.RED)
                        0 -> onFinishGame()
                    }
                    isMineralHide = true
                }
            }


        }
    }

    private fun onFinishGame() {
        context.startActivity(Intent(context, ResultGameActivity::class.java).apply {
            putExtra("my_count", point.get())
        })
    }
}