package hit.big.gold

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.scale
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random


class MineralsSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attributeSet, defStyleAttr), SurfaceHolder.Callback {
    companion object {
        const val TIME_BETWEEN_DRAWING = 20
        const val KOEF_SPEED_TRANSLATING_MINERAL_BETWEEN_DRAWING = 0.001f * TIME_BETWEEN_DRAWING
        const val TIME_BETWEEN_ADDING_MINERAL = 2_000
    }
    private lateinit var thread: Thread
    private var currentTime = 0L
    private var prevDrawTime = 0L
    private var prevTimeAdding = 0L
    private var flagRunning: Boolean = false

    private var point = AtomicInteger(0)
    private var health = AtomicInteger(3)

    val listMinerals = LinkedList<Mineral>()
    val removeListMinerals = LinkedList<Mineral>()
    //for drawing
    private val paint = Paint()
    private val textPaint = Paint().apply {
        color = Color.rgb(255,165,0)
        textAlign = Paint.Align.LEFT
        typeface = ResourcesCompat.getFont(context,R.font.kenney_blocks)
        textSize = 120F
    }
    private val healthPaint = Paint().apply {
        color= Color.GREEN
    }
    private val backGroundBitmap: Bitmap by lazy {
        BitmapFactory.decodeStream(context.assets.open("background.jpg")).scale(width, height)
    }
    private val stoneBitmap =
        BitmapFactory.decodeStream(context.assets.open("minerals/stone.png"))
    private val diamondBitmap =
        BitmapFactory.decodeStream(context.assets.open("minerals/diamond.png"))

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
                if(currentTime - prevDrawTime <= TIME_BETWEEN_DRAWING)
                    continue
                val canvas = holder.lockCanvas()
                try {
                    canvas?.let {
                        drawBackground(it)
                        drawMinerals(it)
                        it.drawText(""+ point.get(),20f,120f,textPaint)
                        it.drawRect(width-200f,30f,width-200 + health.get()* 60f, 80f,healthPaint)
                    }
                } catch (e: IllegalMonitorStateException) {
                    e.printStackTrace()
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas)
                    }
                    listMinerals.removeAll(removeListMinerals)
                }
                if(currentTime - prevTimeAdding>= TIME_BETWEEN_ADDING_MINERAL){
                    listMinerals.add(if(Random.nextInt(2)==0) Diamond() else Stone())
                    prevTimeAdding = currentTime
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
        if(event!=null){
            val touchX = event.x
            val touchY = event.y
            listMinerals.forEach {
                when(it){
                    is Diamond -> {
                        if(touchX in (width/2f- diamondBitmap.width/2f ..width/2f+ diamondBitmap.width/2f ) &&
                            touchY in (it.position.y ..it.position.y + diamondBitmap.height )   ) {
                            point.incrementAndGet()
                            removeListMinerals.add(it)
                        }
                    }
                    is Stone -> {
                        if(touchX in (width/2f- stoneBitmap.width/2f ..width/2f+ stoneBitmap.width/2f ) &&
                            touchY in (it.position.y ..it.position.y + stoneBitmap.height )   ) {
                            health.decrementAndGet()
                            when(health.get()){
                                2-> healthPaint.setColor(Color.YELLOW)
                                1-> healthPaint.setColor(Color.RED)
                                0-> onFinishGame()
                            }
                            removeListMinerals.add(it)
                        }
                    }
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
    }
    fun drawMinerals(canvas:Canvas){
        listMinerals.forEach {
            val drawingBitmap = when (it) {
                is Diamond -> diamondBitmap
                is Stone -> stoneBitmap
                else -> stoneBitmap
            }
            if(it.position.y< canvas.height+ drawingBitmap.height) {
                canvas.drawBitmap(
                    drawingBitmap,
                    (canvas.width - drawingBitmap.width) / 2f,
                    it.position.y,
                    paint
                )
                it.position.apply {
                    y += KOEF_SPEED_TRANSLATING_MINERAL_BETWEEN_DRAWING * canvas.height
                }
            }
            else
                removeListMinerals.add(it)
        }
    }
    private fun onFinishGame() {
        context.startActivity(Intent(context, ResultGameActivity::class.java).apply {
            putExtra("my_count", point)
        })
    }
}