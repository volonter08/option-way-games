package ladonce.pinfistakcooperse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ladonce.pinfistakcooperse.databinding.ActivityRunnerGameBinding

class RunnerGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameActivityBinding = ActivityRunnerGameBinding.inflate(layoutInflater)
        val runnerGameView =  gameActivityBinding.runnerGame
        runnerGameView.textViewCounter = gameActivityBinding.counter
        setContentView(gameActivityBinding.root)
    }
}