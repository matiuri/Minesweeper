package mati.minesweeper.gui

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import mati.advancedgdx.utils.createLabel
import mati.minesweeper.Game

class Timer(private val game: Game) {
    val label: Label = createLabel("Time: 00:00:00:00", game.astManager["TimerF", BitmapFont::class])
    val timeArr: IntArray = IntArray(4) { 0 }
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
        if (!counting) return

        time += delta
        val hf: Float = (time / 3600f)
        val h: Int = hf.toInt()
        val mf: Float = (hf - h) * 60f
        val m: Int = mf.toInt()
        val sf: Float = (mf - m) * 60f
        val s: Int = sf.toInt()
        val msf: Float = (sf - s) * 60f
        val ms: Int = 100 * msf.toInt() / 60

        timeArr[0] = h
        timeArr[1] = m
        timeArr[2] = s
        timeArr[3] = ms

        colonTime += delta
        if (colonTime >= 0.25f) {
            colon = !colon
            colonTime = 0f
        }

        val colonS: String = if (colon) ":" else " "

        label.setText("Time: ")
        if (h < 10) label.setText("${label.text}0$h$colonS")
        else label.setText("${label.text}$h$colonS")
        if (m < 10) label.setText("${label.text}0$m$colonS")
        else label.setText("${label.text}$m$colonS")
        if (s < 10) label.setText("${label.text}0$s$colonS")
        else label.setText("${label.text}$s$colonS")
        if (ms < 10) label.setText("${label.text}0$ms")
        else label.setText("${label.text}$ms")
    }
}
