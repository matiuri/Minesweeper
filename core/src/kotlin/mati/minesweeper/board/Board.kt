package mati.minesweeper.board

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import mati.advancedgdx.utils.isDesktop
import kotlin.properties.Delegates

class Board() : Group() {
    var cells: Array<Array<Cell>> by Delegates.notNull<Array<Array<Cell>>>()
    var size: Int
    var wh: Int = 10
    var mines: Int = 0
    var first: Boolean = true

    init {
        if (isDesktop()) size = 64
        else size = 32
    }

    init {
        cells = Array(wh) { x ->
            Array(wh) { y ->
                val mined = MathUtils.randomBoolean(.25f)
                val cell: Cell = Cell(x, y, size, mined)
                if (mined) mines++
                addActor(cell)
                cell.addListener()
                cell
            }
        }
    }

    fun openFirst(cell: Cell) {
        for (x in (cell.x - 2)..(cell.x + 2)) {
            if (x < 0 || x >= cells.size) continue
            for (y in(cell.y - 2)..(cell.y + 2)) {
                if (y < 0 || y >= cells[x].size) continue
                cells[x][y].mined = false
            }
        }
        for (c1 in cells) {
            for (c in c1) {
                c.setAroundMines()
            }
        }
        cell.open()
        first = false
    }

    fun openMined() {
        for (c1 in cells) {
            for (c in c1) {
                if (c.mined) {
                    c.opened = true
                    c.flagged = false
                }
            }
        }
    }
}
