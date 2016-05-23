package mati.minesweeper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ScreenViewport
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.createNPD
import mati.minesweeper.Game
import mati.minesweeper.board.Board
import mati.minesweeper.board.Cell
import mati.minesweeper.gui.Timer
import kotlin.properties.Delegates

class GameS(game: Game) : Screen(game) {
    var stage: Stage by Delegates.notNull<Stage>()
    var gui: Stage by Delegates.notNull<Stage>()
    var timer: Timer by Delegates.notNull<Timer>()

    override fun load() {
        stage = Stage(ScreenViewport())
        gui = Stage(ScreenViewport())
        timer = Timer(game as Game)
    }

    override fun show() {
        timer.reset()
        stage.addActor(Board())
        val guiTop: Image = Image(createNPD(game.astManager["GUIr", Texture::class], 0, 0, 24, 24))
        guiTop.setBounds(0f, gui.height - 64, gui.width, 64f)
        guiTop.color = Color.BLUE
        gui.addActor(guiTop)
        val guiBottom: Image = Image(createNPD(game.astManager["GUI", Texture::class], 0, 0, 24, 24))
        guiBottom.setBounds(0f, 0f, gui.width, 32f)
        guiBottom.color = Color(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 0.5f)
        gui.addActor(guiBottom)
        gui.addActor(timer.label)
        timer.label.setPosition(26f, 4f)
        Gdx.input.inputProcessor = InputMultiplexer(gui, stage)
        timer.start()
    }

    override fun render(delta: Float) {
        timer.update(delta)
        Cell.Static.update(delta)
        stage.act(delta)
        stage.draw()

        gui.act(delta)
        gui.draw()
    }

    override fun hide() {
        timer.stop()
        stage.clear()
        gui.clear()
    }

    override fun dispose() {
        stage.dispose()
        gui.dispose()
    }
}
