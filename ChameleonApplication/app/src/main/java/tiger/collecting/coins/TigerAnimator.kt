package tiger.collecting.coins

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.core.graphics.scale
import kotlin.properties.Delegates

class TigerAnimator(context: Context) {
    private val tigerPositionSprite: Bitmap =
        BitmapFactory.decodeStream(context.assets.open("minerals/tiger_position.png")).let {
            it.scale(it.width * 4, it.height * 4)
        }
    private val numberFrameLeftPosition: Int = 1
    private val numberFrameRightPosition: Int = 3
    private val tigerRunningLeftSprite: Bitmap =
        BitmapFactory.decodeStream(context.assets.open("minerals/tiger_running_left.png")).let {
            it.scale(it.width * 4, it.height * 4)
        }
    private val tigerRunningRightSprite: Bitmap =
        BitmapFactory.decodeStream(context.assets.open("minerals/tiger_running_right.png")).let {
            it.scale(it.width * 4, it.height * 4)
        }
    val frameWidthTigerPosition = tigerPositionSprite.width / 4
    val frameHeightTigerPosition = tigerPositionSprite.height
    private val frameWidthTigerRunning = tigerRunningLeftSprite.width / 3
    private val frameHeightTigerRunning = tigerRunningLeftSprite.height
    private val frameToDrawPosition = Rect(0, 0, frameWidthTigerPosition, frameHeightTigerPosition)
    private val frameToDrawRunning = Rect(0, 0, frameWidthTigerRunning, frameHeightTigerRunning)
    private var currentFrameRunning by Delegates.observable(0) { _, oldValue: Int, newValue: Int ->
        frameToDrawRunning.apply {
            left = newValue * frameWidthTigerRunning
            right = left + frameWidthTigerRunning
        }
    }
    private var currentFramePosition by Delegates.observable(0) { _, oldValue: Int, newValue: Int ->
        frameToDrawPosition.apply {
            left = newValue * frameWidthTigerPosition
            right = left + frameWidthTigerPosition
        }
    }
    private var prevDrawTimeRunning = 0L
    private val paint = Paint()
    fun draw(canvas: Canvas, tiger: Tiger) {
        val currentTime = System.nanoTime() / 1_000_000
        if (!tiger.isRunning.get()) {
            currentFramePosition = when (tiger.direction) {
                DirectionTiger.RIGHT -> {
                    numberFrameRightPosition
                }

                else -> {
                    numberFrameLeftPosition
                }
            }
            canvas.drawBitmap(
                tigerPositionSprite,
                frameToDrawPosition,
                RectF().apply {
                    left = tiger.positionX
                    top = canvas.height * 15 / 32f
                    right = left + frameWidthTigerPosition.toFloat()
                    bottom = top + frameHeightTigerPosition.toFloat()
                },
                paint
            )
            return
        } else {
            when (tiger.direction) {
                DirectionTiger.RIGHT -> {
                    canvas.drawBitmap(
                        tigerRunningRightSprite,
                        frameToDrawRunning,
                        RectF().apply {
                            left = tiger.positionX
                            top = canvas.height * 15 / 32f
                            right = left + frameWidthTigerRunning.toFloat()
                            bottom = top + frameHeightTigerRunning.toFloat()
                        },
                        paint
                    )
                }

                else -> {
                    canvas.drawBitmap(
                        tigerRunningLeftSprite,
                        frameToDrawRunning,
                        RectF().apply {
                            left = tiger.positionX
                            top = tiger.positionY
                            right = left + frameWidthTigerRunning.toFloat()
                            bottom = top + frameHeightTigerRunning.toFloat()
                        },
                        paint
                    )
                }
            }
            if (currentTime - prevDrawTimeRunning > TigerSurfaceView.TIME_BETWEEN_DRAWING_TIGER) {
                if (++currentFrameRunning == 3)
                    currentFrameRunning = 0
                prevDrawTimeRunning = currentTime
            }
        }
    }
}