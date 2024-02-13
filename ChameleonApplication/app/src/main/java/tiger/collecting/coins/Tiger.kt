package tiger.collecting.coins

import java.util.concurrent.atomic.AtomicBoolean

class Tiger(positionX:Float = 0f, var positionY:Float = 0f,var limitPosition:Float = 0f, var direction:DirectionTiger = DirectionTiger.RIGHT, var isRunning: AtomicBoolean = AtomicBoolean(false) ){
    var positionX:Float = positionX
        set(value) {
            when(value){
                in (0f..limitPosition)->
                    field= value
            }
        }
}