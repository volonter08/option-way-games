package pixel.game.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.res.ResourcesCompat

class BitmapHandler {
    companion object{
        fun getBitmapFromXml(context: Context, drawableRes: Int):Bitmap{
            val drawable = ResourcesCompat.getDrawable(context.resources,drawableRes,null)!!
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
    }
}