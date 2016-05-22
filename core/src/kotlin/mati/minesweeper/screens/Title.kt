package mati.minesweeper.screens

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.ScreenViewport
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.createLabel
import mati.minesweeper.Game
import kotlin.properties.Delegates

class Title(game: Game) : Screen(game) {
    private var stage: Stage by Delegates.notNull<Stage>()
    private var table: Table by Delegates.notNull<Table>()
    private var title: Label by Delegates.notNull<Label>()

    override fun show() {
        stage = Stage(ScreenViewport())
        table = Table()
        stage.addActor(table)
        table.setFillParent(true)
        title = createLabel("Minesweeper", game.astManager["Title", BitmapFont::class])
        table.add(title)
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
    }
}
