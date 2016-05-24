package mati.minesweeper.desktop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import mati.minesweeper.board.Cell

class CellInputListenerD(private val cell: Cell) : InputListener() {
    private var checked: Boolean = false

    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if ((button == 0 && !cell.flagged && !cell.opened) || (button == 2 && !cell.flagged && cell.opened)) {
            cell.selected = true
            checked = true
//            log.d("${this.javaClass.simpleName}:${Thread.currentThread().stackTrace[1].methodName}",
//                    "Button=$button, Flagged=${cell.flagged}, Opened=${cell.opened}")
            return true
        } else if (button == 1 && !cell.opened) {
            cell.flag()
        }
        return false
    }

    override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
        if (checked) {
//            log.d("${this.javaClass.simpleName}:${Thread.currentThread().stackTrace[1].methodName}",
//                    "Button=$button, Flagged=${cell.flagged}, Opened=${cell.opened}")
            if (button == 0 && cell.selected) {
                if (cell.board.first)
                    cell.board.openFirst(cell)
                else
                    cell.open()
                cell.selected = false
            } else if (button == 2 && cell.selected) {
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
