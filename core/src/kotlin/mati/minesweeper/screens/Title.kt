package mati.minesweeper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton
import mati.advancedgdx.utils.createLabel
import mati.advancedgdx.utils.createNPD
import mati.minesweeper.Game
import kotlin.properties.Delegates

class Title(game: Game) : Screen(game) {
    private var stage: Stage by Delegates.notNull<Stage>()
    private var table: Table by Delegates.notNull<Table>()
    private var title: Label by Delegates.notNull<Label>()

    override fun load() {
        stage = Stage(ScreenViewport())
    }

    override fun show() {
        stage.clear()
        table = Table()
        stage.addActor(table)
        table.setFillParent(true)
        table.pad(20f)
        table.debug = false
        title = createLabel("Minesweeper", game.astManager["Title", BitmapFont::class])
        table.add(title).colspan(2).pad(10f)
        table.row()
        table.add(createButton("Play", game.astManager["GeneralB", BitmapFont::class],
                createNPD(game.astManager["ButtonLocked", Texture::class], 8),
                createNPD(game.astManager["ButtonLocked", Texture::class], 8))).pad(5f).fill()
        val exit: TextButton = createButton("Exit", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        exit.color = Color.RED
        table.add(exit).pad(5f).fill()
        exit.addListener1 { e, a ->
            Gdx.app.exit()
        }
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
    }
}
