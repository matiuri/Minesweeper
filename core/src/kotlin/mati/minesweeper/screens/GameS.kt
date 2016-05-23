package mati.minesweeper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import mati.advancedgdx.screens.Screen
import mati.minesweeper.Game
import mati.minesweeper.board.Board
import mati.minesweeper.board.Cell
import kotlin.properties.Delegates

class GameS(game: Game) : Screen(game) {
    var stage: Stage by Delegates.notNull<Stage>()

    override fun load() {
        stage = Stage(ScreenViewport())
    }

    override fun show() {
        stage.addActor(Board())
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Cell.Static.update(delta)
        stage.act(delta)
        stage.draw()
    }

    override fun hide() {
        stage.clear()
    }

    override fun dispose() {
        stage.dispose()
    }
}
