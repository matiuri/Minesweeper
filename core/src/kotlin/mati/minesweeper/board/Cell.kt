package mati.minesweeper.board

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import mati.minesweeper.Game
import kotlin.properties.Delegates

class Cell(var x: Int, var y: Int, var size: Int, var mined: Boolean,
           var arroundMines: Int = 0, var opened: Boolean = false, var flagged: Boolean = false,
           var selected: Boolean = false) : Actor() {
    companion object Static {
        var up: Texture by Delegates.notNull<Texture>()
        var down: Texture by Delegates.notNull<Texture>()
        var open: Texture by Delegates.notNull<Texture>()
        var nums: Array<Texture> by Delegates.notNull<Array<Texture>>()
        var mine: Texture by Delegates.notNull<Texture>()
        var flag: Texture by Delegates.notNull<Texture>()

        fun init(game: Game) {
            up = game.astManager["CellUp", Texture::class]
            down = game.astManager["CellDown", Texture::class]
            open = game.astManager["CellOpen", Texture::class]
            nums = Array(7) { game.astManager["N${it + 1}", Texture::class] }
            mine = game.astManager["Mine", Texture::class]
            flag = game.astManager["Flag", Texture::class]
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch != null) {
            if (!opened) {
                if (!selected)
                    batch.draw(up, (x * size).toFloat(), (y * size).toFloat(), size.toFloat(), size.toFloat())
                else
                    batch.draw(down, (x * size).toFloat(), (y * size).toFloat(), size.toFloat(), size.toFloat())
            } else {
                batch.draw(open, (x * size).toFloat(), (y * size).toFloat(), size.toFloat(), size.toFloat())
            }
        }
    }
}
