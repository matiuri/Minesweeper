package mati.minesweeper

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import mati.minesweeper.board.Board
import mati.minesweeper.board.Cell

class CellInputListenerA(private val cell: Cell) : InputListener() {
    private var checked: Boolean = false
    private val board: Board = cell.board

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if ((board.isOpenMode() && !cell.flagged && !cell.opened) ||
                (board.isFlagMode() && !cell.flagged && cell.opened)) {
            cell.selected = true
            checked = true
            return true
        } else if (board.isFlagMode() && !cell.opened) {
            cell.flag()
        }
        return false
    }

    override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
        if (checked) {
            if (board.isOpenMode() && cell.selected) {
                if (cell.board.first)
                    cell.board.openFirst(cell)
                else
                    cell.open()
                cell.selected = false
            } else if (board.isFlagMode() && cell.selected) {
                cell.openAround()
                cell.selected = false
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
