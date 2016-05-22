package mati.minesweeper.board

import com.badlogic.gdx.scenes.scene2d.Group
import kotlin.properties.Delegates

class Board() : Group() {
    var cells: Array<Array<Cell>> by Delegates.notNull<Array<Array<Cell>>>()
    var size: Int = 64
    var wh: Int = 10

    init {
        cells = Array(wh) { x ->
            Array(wh) { y ->
                val cell: Cell = Cell(x, y, size, false)
                addActor(cell)
                cell
            }
        }
    }
}
