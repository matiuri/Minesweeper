package mati.minesweeper.gui

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import mati.advancedgdx.utils.createLabel
import mati.minesweeper.Game
import mati.minesweeper.board.Board

class FlagCounter(private val game: Game, private val board: Board, private val maxX: Float) {
    val label: Label = createLabel("Start the game opening a cell", game.astManager["TimerF", BitmapFont::class])
    var mines: Int = 0
    var flags: Int = 0

    fun init() {
        mines = board.mines
        label.setText("$flags Flags / $mines Mines")
        label.x = maxX - label.width
    }

    fun flag() {
        flags++
        label.setText("$flags Flags / $mines Mines")
        label.x = maxX - label.width
    }

    fun unflag() {
        flags--
        label.setText("$flags Flags / $mines Mines")
        label.x = maxX - label.width
    }
}