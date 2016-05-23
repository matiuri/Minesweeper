package mati.minesweeper.desktop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import mati.minesweeper.board.Cell

class CellInputListenerD(private val cell: Cell) : InputListener() {
    private var checked: Boolean = false

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if (button == 0 && !cell.flagged) {
            cell.selected = true
            checked = true
            return true
        } else if (button == 1 && !cell.opened) {
            cell.flagged = !cell.flagged
        }
        return false
    }

    override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
        if (checked) {
            if (cell.selected) cell.opened = true //FIXME: Implement Cell's Open method
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
