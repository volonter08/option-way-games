package tiger.collecting.coins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tiger.collecting.coins.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(gameBinding.root)
    }
}