package mati.minesweeper.board

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import mati.advancedgdx.utils.isDesktop
import kotlin.properties.Delegates

class Board() : Group() {
    var cells: Array<Array<Cell>> by Delegates.notNull<Array<Array<Cell>>>()
    var size: Int
    var wh: Int = 10

    init {
        if (isDesktop()) size = 64
        else size = 32
    }

    init {
        cells = Array(wh) { x ->
            Array(wh) { y ->
                val cell: Cell = Cell(x, y, size, MathUtils.randomBoolean(.25f))
                addActor(cell)
                cell.addListener()
                cell
            }
        }
    }
}
