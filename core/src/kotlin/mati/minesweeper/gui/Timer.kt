package mati.minesweeper.gui

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import mati.advancedgdx.utils.createLabel
import mati.minesweeper.Game

class Timer(private val game: Game) {
    val label: Label = createLabel("Time: 00:00:00:00", game.astManager["TimerF", BitmapFont::class])
    private var time: Float = 0f
    private var counting: Boolean = false
    private var colon: Boolean = true
    private var colonTime: Float = 0f

    fun start(reset: Boolean = false) {
        if (reset) reset()
        counting = true
        colon = true
        colonTime = 0f
    }

    fun reset() {
        time = 0f
        label.setText("Time: 00:00:00:00")
    }

    fun stop() {
        counting = false
        colon = true
        colonTime = 0f
    }

    fun update(delta: Float) {
        time += delta
        val hf: Float = (time / 3600f)
        val h: Int = hf.toInt()
        val mf: Float = (hf - h) * 60f
        val m: Int = mf.toInt()
        val sf: Float = (mf - m) * 60f
        val s: Int = sf.toInt()
        val msf: Float = (sf - s) * 60f
        val ms: Int = 100 * msf.toInt() / 60

        colonTime += delta
        if (colonTime >= 0.25f) {
            colon = !colon
            colonTime = 0f
        }

        label.setText("Time: ")
        if (h < 10) label.setText("${label.text}0$h${if (colon) ":" else "·"}")
        else label.setText("${label.text}$h${if (colon) ":" else "·"}")
        if (m < 10) label.setText("${label.text}0$m${if (colon) ":" else "·"}")
        else label.setText("${label.text}$m${if (colon) ":" else "·"}")
        if (s < 10) label.setText("${label.text}0$s${if (colon) ":" else "·"}")
        else label.setText("${label.text}$s${if (colon) ":" else "·"}")
        if (ms < 10) label.setText("${label.text}0$ms")
        else label.setText("${label.text}$ms")
    }
}
