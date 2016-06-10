package mati.minesweeper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.*
import mati.minesweeper.Game
import kotlin.properties.Delegates

class NewGame(game: Game) : Screen(game) {
    private var stage: Stage by Delegates.notNull<Stage>()
    private var table: Table by Delegates.notNull<Table>()

    override fun load() {
        stage = Stage(ScreenViewport())
    }

    override fun show() {
        Properties.setDefault()

        table = Table()
        stage.addActor(table)
        table.setFillParent(true)
        table.pad(10f)

        table.add(createLabel("New Game", game.astManager["Title", BitmapFont::class])).colspan(5).pad(5f)
        table.row()

        difficulty()
        table.row()
        size()
        table.row()
        buttons()

        Gdx.input.inputProcessor = stage
    }

    private fun difficulty() {
        table.add(createLabel("Difficulty", game.astManager["GeneralB", BitmapFont::class])).pad(5f)

        val easy: TextButton = createButton("Easy", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        easy.color = Color.GREEN
        table.add(easy).pad(5f).fill()
        easy.addListener2 { event, actor ->
            Properties.difficulty = Difficulty.EASY
        }

        val medium: TextButton = createButton("Medium", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        medium.color = Color.YELLOW
        table.add(medium).pad(5f).fill()
        medium.addListener2 { event, actor ->
            Properties.difficulty = Difficulty.MEDIUM
        }

        val hard: TextButton = createButton("Hard", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        hard.color = Color.RED
        table.add(hard).pad(5f).fill()
        hard.addListener2 { event, actor ->
            Properties.difficulty = Difficulty.HARD
        }

        val insane: TextButton = createButton("Insane", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        insane.color = Color(0.5f, 0f, 0f, 1f)
        table.add(insane).pad(5f).fill()
        insane.addListener2 { event, actor ->
            Properties.difficulty = Difficulty.INSANE
        }

        val group: ButtonGroup<TextButton> = ButtonGroup(easy, medium, hard, insane)
        group.setMinCheckCount(1)
        group.setMaxCheckCount(1)
    }

    private fun size() {
        table.add(createLabel("Difficulty", game.astManager["GeneralB", BitmapFont::class])).pad(5f)

        val short: TextButton = createButton("Short", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        short.color = Color.GREEN
        table.add(short).pad(5f).fill()
        short.addListener2 { event, actor ->
            Properties.size = Size.SHORT
        }

        val medium: TextButton = createButton("Medium", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        medium.color = Color.YELLOW
        table.add(medium).pad(5f).fill()
        medium.addListener2 { event, actor ->
            Properties.size = Size.MEDIUM
        }

        val large: TextButton = createButton("Large", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        large.color = Color.RED
        table.add(large).pad(5f).fill()
        large.addListener2 { event, actor ->
            Properties.size = Size.LARGE
        }

        val insane: TextButton = createButton("Insane", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        insane.color = Color(0.5f, 0f, 0f, 1f)
        table.add(insane).pad(5f).fill()
        insane.addListener2 { event, actor ->
            Properties.size = Size.INSANE
        }

        val group: ButtonGroup<TextButton> = ButtonGroup(short, medium, large, insane)
        group.setMinCheckCount(1)
        group.setMaxCheckCount(1)
    }

    private fun buttons() {
        val back: TextButton = createButton("Back", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        back.color = Color.RED
        table.add(back).pad(5f).colspan(2).fill()
        back.addListener1 { event, actor ->
            game.scrManager.change("Title")
        }

        table.add()

        val start: TextButton = createButton("New Game", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        start.color = Color.BLUE
        table.add(start).pad(5f).colspan(2).fill()
        start.addListener1 { event, actor ->
            if (Properties.difficulty != Difficulty.NONE && Properties.size != Size.NONE)
                game.scrManager.change("Game")
            else {
                start.color = Color.RED
                val runnable: Runnable = Runnable {
                    val time: Long = System.nanoTime()
                    var timeSec: Double = time / 1000000000.0
                    var currentTime: Double = 0.0
                    while (currentTime < 0.5) {
                        val temp: Double = System.nanoTime() / 1000000000.0
                        currentTime += temp - timeSec
                        timeSec = temp
                    }
                    start.color = Color.BLUE
                }
                Thread(runnable).start()
            }
        }
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun hide() {
        stage.clear()
    }

    override fun dispose() {
        stage.dispose()
    }

    object Properties {
        var difficulty: Difficulty = Difficulty.NONE
        var size: Size = Size.NONE

        fun setDefault() {
            difficulty = Difficulty.NONE
            size = Size.NONE
        }
    }

    enum class Difficulty(val mineDensity: Float) {
        NONE(0f), EASY(0.15f), MEDIUM(0.25f), HARD(0.35f), INSANE(0.55f)
    }

    enum class Size(val wh: Int) {
        NONE(0), SHORT(15), MEDIUM(30), LARGE(50), INSANE(100)
    }
}
