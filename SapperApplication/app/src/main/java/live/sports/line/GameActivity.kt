package live.sports.line

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.goloviznin.eldar.minesweeper.scenes.game.model.Cell
import com.goloviznin.eldar.minesweeper.scenes.game.presenter.GamePresenter
import com.goloviznin.eldar.minesweeper.scenes.game.presenter.GamePresenterDefault
import com.goloviznin.eldar.minesweeper.scenes.game.presenter.GameView
import live.sports.line.databinding.ActivityGameBinding
import live.sports.line.game.view.GameViewDelegate

class GameActivity : AppCompatActivity(), GameViewDelegate, SeekBar.OnSeekBarChangeListener,
    GameView {

    private val minFieldSize = 4
    private val maxFieldSize = 20
    private val minBombsCount = 2
    private val fieldSizeToBombsCountRate = 4

    private var presenter: GamePresenter? = null
    lateinit var gameBinding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameBinding= ActivityGameBinding.inflate(layoutInflater)
        setContentView(gameBinding.root)

        gameBinding.gameView.delegate = this

        presenter = GamePresenterDefault()
        presenter?.view = this

        gameBinding.fieldSizeSeekBar.max = maxFieldSize - minFieldSize
        gameBinding.fieldSizeSeekBar.progress = 0
        handleFieldSizeSeekBarProgressChange()

        gameBinding.bombsCountSeekBar.progress = 0
        handleBombsCountSeekBarProgressChange()

        gameBinding.fieldSizeSeekBar.setOnSeekBarChangeListener(this)
        gameBinding.bombsCountSeekBar.setOnSeekBarChangeListener(this)
    }

    override fun onResume() {
        super.onResume()

        presenter?.viewOnResume()
    }

    override fun onPause() {
        super.onPause()

        presenter?.viewOnPause()
    }

    override fun didTapOnCellWithId(id: Int) {
        presenter?.open(id)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar == gameBinding.fieldSizeSeekBar) {
            handleFieldSizeSeekBarProgressChange()
        } else if (seekBar == gameBinding.bombsCountSeekBar) {
            handleBombsCountSeekBarProgressChange()
        }
    }

    private fun handleFieldSizeSeekBarProgressChange() {
        val newSize = gameBinding.fieldSizeSeekBar.progress + minFieldSize
        gameBinding.fieldSizeTextView.text = String.format(resources.getString(R.string.fieldSizeTitle), newSize)
        gameBinding.bombsCountSeekBar.max = newSize * newSize / fieldSizeToBombsCountRate - minBombsCount
    }

    private fun handleBombsCountSeekBarProgressChange() {
        val newCount = gameBinding.bombsCountSeekBar.progress + minBombsCount
        gameBinding.bombsCountTextView.text = String.format(resources.getString(R.string.bombsCountTitle), newCount)
    }

    fun newGame(view: View? = null) {
        presenter?.startNewGame(gameBinding.fieldSizeSeekBar.progress + minFieldSize, gameBinding.bombsCountSeekBar.progress + minBombsCount)
    }

    override fun gameStarted(fieldSize: Int, numberOfBombs: Int, field: Array<Cell>) {
        gameBinding.gameView.fieldSize = fieldSize
        gameBinding.gameView.field = field
        gameBinding.actionButton.text = resources.getString(R.string.restartButtonTitle)
    }

    override fun fieldChanged(field: Array<Cell>) {
        gameBinding.gameView.field = field
    }

    override fun win() {
        gameBinding.actionButton.text = resources.getString(R.string.winButtonTitle)
    }

    override fun lose() {
        gameBinding.actionButton.text = resources.getString(R.string.loseButtonTitle)
    }

}