package mati.minesweeper.board

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import mati.advancedgdx.graphics.Animation
import mati.minesweeper.Game
import kotlin.properties.Delegates
import kotlin.reflect.KClass

class Cell(var x: Int, var y: Int, var size: Int, var mined: Boolean,
           var aroundMines: Int = 0, var opened: Boolean = false, var flagged: Boolean = false,
           var selected: Boolean = false) : Actor() {
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
                batch.draw(open, getX(), getY(), width, height)
                if (mined)
                    batch.draw(mine.get(), getX(), getY(), width, height)
                else if (aroundMines != 0) batch.draw(nums[aroundMines - 1], getX(), getY(), width, height)
            }
        }
    }

    fun addListener() {
        addListener(input.java.getConstructor(Cell::class.java).newInstance(this))
    }
}
