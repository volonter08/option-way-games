package pixel.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.testcaseapplication.databinding.GameActivityBinding

class GameActivity : AppCompatActivity() {
    lateinit var footballGameView: GameFootballView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameActivityBinding = GameActivityBinding.inflate(layoutInflater)
        footballGameView =  gameActivityBinding.footballGame
        footballGameView.textViewCounter = gameActivityBinding.counter
        setContentView(gameActivityBinding.root)
        gameActivityBinding.introductionButton.setOnClickListener{
            gameActivityBinding.introduction.visibility = if(gameActivityBinding.introduction.visibility== View.GONE) View.VISIBLE else View.GONE
        }
    }
    override fun onBackPressed() {
        setResult(RESULT_OK, Intent())
        finish()
    }
}