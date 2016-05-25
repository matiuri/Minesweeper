package mati.minesweeper.board

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import mati.advancedgdx.utils.isDesktop
import mati.minesweeper.Game
import mati.minesweeper.gui.FlagCounter
import mati.minesweeper.gui.Timer
import mati.minesweeper.screens.GameS
import kotlin.properties.Delegates

class Board(val gameS: GameS, val game: Game, val timer: Timer) : Group() {
    var cells: Array<Array<Cell>> by Delegates.notNull<Array<Array<Cell>>>()
    var size: Int = if (isDesktop()) 64 else 32
    var wh: Int = 50
    var mines: Int = 0
    var first: Boolean = true
    var totalClean: Int = 0
    var opened: Int = 0
    var fCounter: FlagCounter by Delegates.notNull<FlagCounter>()

    init {
        cells = Array(wh) { x ->
            Array(wh) { y ->
                val mined = MathUtils.randomBoolean(.25f)
                val cell: Cell = Cell(x, y, size, mined, this)
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
                if (cells[x][y].mined) mines--
                cells[x][y].mined = false
            }
        }
        totalClean = (wh * wh) - mines
        for (c1 in cells) {
            for (c in c1) {
                c.setAroundMines()
            }
        }
        cell.open()
        fCounter.init()
        first = false
        timer.start()
    }

    fun openMined() {
        for (c1 in cells) {
            for (c in c1) {
                if (c.mined) {
                    c.opened = true
                }
            }
        }
    }
}
