package mati.minesweeper.board

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import mati.minesweeper.Game
import mati.minesweeper.board.Board.AndroidMode.*
import mati.minesweeper.gui.FlagCounter
import mati.minesweeper.gui.Timer
import mati.minesweeper.screens.GameS
import mati.minesweeper.screens.NewGame
import mati.minesweeper.screens.NewGame.Properties
import kotlin.properties.Delegates

class Board() : Group() {
    var cells: Array<Array<Cell>> by Delegates.notNull<Array<Array<Cell>>>()
    var size: Int = 64
    var wh: Int = Properties.size.wh
    var mineDensity: Float = Properties.difficulty.mineDensity
    var mines: Int = 0
    var first: Boolean = true
    var totalClean: Int = 0
    var opened: Int = 0
    var flags: Int = 0
    var mode: AndroidMode = NULL
    var timer: Timer by Delegates.notNull<Timer>()
    var fCounter: FlagCounter by Delegates.notNull<FlagCounter>()
    private var gameS: GameS = Game.game.scrManager["Game"] as GameS

    enum class AndroidMode {
        NULL, OPEN, FLAG
    }

    fun isOpenMode(): Boolean = mode == OPEN

    fun isFlagMode(): Boolean = mode == FLAG

    fun init() {
        cells = Array(wh) { x ->
            Array(wh) { y ->
                val mined = MathUtils.randomBoolean(mineDensity)
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
        timer.start(true)
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

    fun check(cell: Cell): Boolean {
        if (cell.mined) {
            openMined()
            gameS.gameOver()
            return false
        } else {
            opened++
            if (opened == totalClean) {
                gameS.win()
                return false
            }
        }
        return true
    }
}
