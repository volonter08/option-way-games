package hit.big.gold

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import hit.big.gold.MineralsSurfaceView.Companion.KOEF_SPEED_TRANSLATING_MINERAL_BETWEEN_DRAWING
import java.util.LinkedList

class MineralsPainter(val diamondBitmap: Bitmap, val stoneBitmap: Bitmap,val paint:Paint) {
    val listMinerals = LinkedList<Mineral>()
    fun drawMinerals(canvas:Canvas){
        listMinerals.forEach {
            val drawingBitmap = when (it) {
                is Diamond -> diamondBitmap
                is Stone -> stoneBitmap
                else -> stoneBitmap
            }
            canvas.drawBitmap(drawingBitmap,(canvas.width - drawingBitmap.width)/2f,it.position.y,paint)
            it.position.apply {
                y+= KOEF_SPEED_TRANSLATING_MINERAL_BETWEEN_DRAWING * canvas.height
            }
            if(it.position.y>= canvas.height+ drawingBitmap.height)
                listMinerals.remove(it)
        }
    }
    fun increasePosition(){
    }

}