package hit.big.gold

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hit.big.gold.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(gameBinding.root)
    }
}