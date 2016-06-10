package mati.minesweeper.desktop

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import mati.minesweeper.Game
import mati.minesweeper.board.Cell

class CellInputListenerD(private val cell: Cell) : InputListener() {
    private var checked: Boolean = false

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if ((button == 0 && !cell.flagged && !cell.opened) || (button == 2 && !cell.flagged && cell.opened)) {
            cell.selected = true
            checked = true
            return true
        } else if (button == 1 && !cell.opened) {
            cell.flag()
            Game.game.astManager["OpenS", Sound::class].play(0.5f)
        }
        return false
    }

    override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
        if (checked) {
            if (button == 0 && cell.selected) {
                if (cell.board.first)
                    cell.board.openFirst(cell)
                else
                    cell.open()
                Game.game.astManager["OpenS", Sound::class].play(0.5f)
                cell.selected = false
            } else if (button == 2 && cell.selected) {
                cell.openAround()
                cell.selected = false
                Game.game.astManager["OpenS", Sound::class].play(0.5f)
            }
            checked = false
        }
    }

    override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
        if (checked && pointer != -1) cell.selected = true
    }

    override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
        if (checked && pointer != -1) cell.selected = false
    }
}
