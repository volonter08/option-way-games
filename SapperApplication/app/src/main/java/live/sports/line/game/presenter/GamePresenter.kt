package com.goloviznin.eldar.minesweeper.scenes.game.presenter

import com.goloviznin.eldar.minesweeper.scenes.game.model.Cell
import com.goloviznin.eldar.minesweeper.scenes.game.model.GameState
import com.goloviznin.eldar.minesweeper.scenes.game.model.MineSweeper

interface GameView {
    fun gameStarted(fieldSize: Int, numberOfBombs: Int, field: Array<Cell>)
    fun fieldChanged(field: Array<Cell>)
    fun win()
    fun lose()
}

interface GamePresenter {

    var view: GameView?

    fun viewOnResume()
    fun viewOnPause()

    fun startNewGame(fieldSize: Int, numberOfBombs: Int)
    fun open(index: Int)
}

class GamePresenterDefault : GamePresenter {
    private var game: MineSweeper? = null

    private var currentGameDuration = 0
    private var currentGameFieldSize = 0
    private var currentGameBombsCount = 0


    override var view: GameView? = null

    override fun viewOnResume() {
    }

    override fun viewOnPause() {
    }

    override fun startNewGame(fieldSize: Int, numberOfBombs: Int) {
        currentGameDuration = 0
        currentGameFieldSize = fieldSize
        currentGameBombsCount = numberOfBombs

        game = MineSweeper(fieldSize, numberOfBombs)
        view?.gameStarted(game!!.size, game!!.bombsCount, game!!.openedField)
    }

    override fun open(index: Int) {
        game?.let { game ->
            if (game.open(index)) {
                view?.fieldChanged(game.openedField)
            }

            when (game.state) {
                GameState.WIN -> {
                    view?.win()
                }
                GameState.LOSE -> {
                    view?.lose()
                }
                GameState.ACTIVE -> return
            }
        }
    }

}