package mati.minesweeper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
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
    private var warningStage: Stage? = null

    override fun load() {
        stage = Stage(ScreenViewport())
    }

    override fun show() {
        val isSavedGame: Boolean = game.ioManager.exists("board") && game.ioManager.exists("timer") &&
                game.ioManager.exists("camera")
        table = Table()
        stage.addActor(table)
        table.setFillParent(true)
        table.pad(20f)

        title = createLabel("Minesweeper", game.astManager["Title", BitmapFont::class])
        table.add(title).colspan(3).pad(10f)
        table.row()

        createPlayButton(isSavedGame)
        createContinueButton(isSavedGame)
        createExitButton()

        if (Game.superDebug) {
            warningStage = Stage(ScreenViewport())
            val warning = createLabel("WARNING: Super Debug Mode On", game.astManager["WarningF", BitmapFont::class])
            warningStage?.addActor(warning)
            warning.setPosition((Gdx.graphics.width - warning.width) / 2, (Gdx.graphics.height - warning.height) / 2)
            warning.addAction(Actions.repeat(RepeatAction.FOREVER,
                    Actions.sequence(
                            Actions.color(Color.RED, 0.25f),
                            Actions.color(Color.YELLOW, 0.25f)
                    )
            ))
        }

        Gdx.input.inputProcessor = stage
    }

    private fun createPlayButton(isSavedGame: Boolean) {
        val play: TextButton = createButton("New Game", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        table.add(play).pad(5f).fill()

        if (!isSavedGame) {
            play.color = Color.BLUE
            play.addListener1 { event, actor ->
                (game.scrManager["Game"] as GameS).newGame = true
                game.scrManager.change("New")
            }
        } else {
            play.color = Color(0f, 0f, 0.25f, 1f)

            var secure: Boolean = false
            play.addListener1 { event, actor ->
                if (!secure) {
                    secure = true
                    play.color = Color.BLUE

                    val runnable: Runnable = Runnable {
                        val time: Long = System.nanoTime()
                        var timeSec: Double = time / 1000000000.0
                        var currentTime: Double = 0.0
                        while (currentTime < 5.0) {
                            val temp: Double = System.nanoTime() / 1000000000.0
                            currentTime += temp - timeSec
                            timeSec = temp
                        }
                        secure = false
                        play.color = Color(0f, 0f, 0.25f, 1f)
                    }
                    Thread(runnable).start()
                } else {
                    (game.scrManager["Game"] as GameS).newGame = true
                    game.scrManager.change("New")
                }
            }
        }
    }

    private fun createContinueButton(isSavedGame: Boolean) {
        val cont: TextButton = if (!isSavedGame)
            createButton("Load Game", game.astManager["GeneralB", BitmapFont::class],
                    createNPD(game.astManager["ButtonLocked", Texture::class], 8),
                    createNPD(game.astManager["ButtonLocked", Texture::class], 8))
        else
            createButton("Load Game", game.astManager["GeneralW", BitmapFont::class],
                    createNPD(game.astManager["ButtonUp", Texture::class], 8),
                    createNPD(game.astManager["ButtonDown", Texture::class], 8))

        if (!isSavedGame)
            cont.color = Color.DARK_GRAY
        else {
            cont.color = Color.ORANGE
            cont.addListener1 { event, actor ->
                (game.scrManager["Game"] as GameS).newGame = false
                game.scrManager.change("Game")
            }
        }
        table.add(cont).pad(5f).fill()
    }

    private fun createExitButton() {
        val exit: TextButton = createButton("Exit", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        exit.color = Color.RED
        table.add(exit).pad(5f).fill()
        exit.addListener1 { e, a ->
            Gdx.app.exit()
        }
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()

        warningStage?.act(delta)
        warningStage?.draw()
    }

    override fun hide() {
        stage.clear()
        warningStage?.clear()
    }

    override fun dispose() {
        stage.dispose()
        warningStage?.dispose()
    }
}
