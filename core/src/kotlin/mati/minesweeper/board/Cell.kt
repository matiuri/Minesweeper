package mati.minesweeper.board

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import mati.advancedgdx.AdvancedGame.Static.log
import mati.advancedgdx.graphics.Animation
import mati.minesweeper.Game
import kotlin.properties.Delegates
import kotlin.reflect.KClass

class Cell(var x: Int, var y: Int, var size: Int, var mined: Boolean) : Actor() {
    var aroundMines: Int = 0
    var aroundFlags: Int = 0
    var opened: Boolean = false
    var flagged: Boolean = false
    var selected: Boolean = false

    companion object Static {
        private var up: Texture by Delegates.notNull<Texture>()
        private var down: Texture by Delegates.notNull<Texture>()
        private var open: Texture by Delegates.notNull<Texture>()
        private var nums: Array<Texture> by Delegates.notNull<Array<Texture>>()
        private var mine: Animation by Delegates.notNull<Animation>()
        private var flag: Texture by Delegates.notNull<Texture>()
        private var input: KClass<out InputListener> by Delegates.notNull<KClass<out InputListener>>()

        fun init(game: Game) {
            up = game.astManager["CellUp", Texture::class]
            down = game.astManager["CellDown", Texture::class]
            open = game.astManager["CellOpen", Texture::class]
            nums = Array(7) { game.astManager["N${it + 1}", Texture::class] }
            mine = Animation(arrayOf(game.astManager["Mine", Texture::class],
                    game.astManager["Mine1", Texture::class]), 0.05f)
            flag = game.astManager["Flag", Texture::class]
            input = game.cellInput
        }

        fun update(delta: Float) {
            mine.update(delta)
        }
    }

    init {
        setBounds((x * size).toFloat(), (y * size).toFloat(), size.toFloat(), size.toFloat())
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch != null) {
            if (!opened) {
                if (!selected)
                    batch.draw(up, getX(), getY(), width, height)
                else
                    batch.draw(down, getX(), getY(), width, height)
                if (flagged) batch.draw(flag, getX(), getY(), width, height)
            } else {
                if (!selected)
                    batch.draw(open, getX(), getY(), width, height)
                else if (aroundFlags == aroundMines) {
                    batch.color = Color.BLUE
                    batch.draw(open, getX(), getY(), width, height)
                    batch.color = Color.WHITE
                } else {
                    batch.color = Color.RED
                    batch.draw(open, getX(), getY(), width, height)
                    batch.color = Color.WHITE
                }
                if (mined)
                    batch.draw(mine.get(), getX(), getY(), width, height)
                else if (aroundMines != 0) batch.draw(nums[aroundMines - 1], getX(), getY(), width, height)
            }
        }
    }

    fun setAroundMines() {
        val cells: Array<Array<Cell>> = (parent as Board).cells
        for (x in (this.x - 1)..(this.x + 1)) {
            if (x < 0 || x >= cells.size) continue
            for (y in(this.y - 1)..(this.y + 1)) {
                if (y < 0 || y >= cells[x].size || (this.x == x && this.y == y)) continue
                if (cells[x][y].mined) aroundMines++
            }
        }
    }

    fun open() {
        opened = true
        if (mined) {
            log.d("${this.javaClass.simpleName}", "Game Over")
            (parent as Board).openMined()
        } else {
            openAround()
        }
    }

    fun flag() {
        val cells: Array<Array<Cell>> = (parent as Board).cells
        if (flagged) {
            flagged = false
            for (x in (this.x - 1)..(this.x + 1)) {
                if (x < 0 || x >= cells.size) continue
                for (y in(this.y - 1)..(this.y + 1)) {
                    if (y < 0 || y >= cells[x].size || (this.x == x && this.y == y)) continue
                    cells[x][y].aroundFlags--
                }
            }
        } else {
            flagged = true
            for (x in (this.x - 1)..(this.x + 1)) {
                if (x < 0 || x >= cells.size) continue
                for (y in(this.y - 1)..(this.y + 1)) {
                    if (y < 0 || y >= cells[x].size || (this.x == x && this.y == y)) continue
                    cells[x][y].aroundFlags++
                }
            }
        }
    }

    fun openAround() {
        if (aroundFlags == aroundMines) {
            val cells: Array<Array<Cell>> = (parent as Board).cells
            for (x in (this.x - 1)..(this.x + 1)) {
                if (x < 0 || x >= cells.size) continue
                for (y in(this.y - 1)..(this.y + 1)) {
                    if (y < 0 || y >= cells[x].size || (this.x == x && this.y == y)) continue
                    if (!cells[x][y].flagged && !cells[x][y].opened) {
                        cells[x][y].open()
                        cells[x][y].openAround()
                    }
                }
            }
        }
    }

    fun addListener() {
        addListener(input.java.getConstructor(Cell::class.java).newInstance(this))
    }
}
