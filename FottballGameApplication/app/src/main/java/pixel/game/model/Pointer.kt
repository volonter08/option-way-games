package pixel.game.model

import android.graphics.Bitmap

class Pointer(val bitmap:Bitmap,val limitTranslating:Float) {
    var position:Float = limitTranslating/2
    var directionTranslating = DirectionTranslating.RIGHT
    var value = ValuePointer.GOOD
    fun translate(dx:Float){
        synchronized(this) {
            when (directionTranslating) {
                DirectionTranslating.RIGHT -> {
                    if (position < limitTranslating)
                        position += dx
                    else {
                        directionTranslating = DirectionTranslating.LEFT
                        position = limitTranslating
                    }
                }

                DirectionTranslating.LEFT -> {
                    if (position > 0f)
                        position -= dx
                    else {
                        directionTranslating = DirectionTranslating.RIGHT
                        position = 0f
                    }
                }
            }
            value = if((position) in ((1/4f) *(limitTranslating)..(3/4f) * ((limitTranslating)) )){
                ValuePointer.GOOD
            } else{
                ValuePointer.BAD
            }
        }
    }
}