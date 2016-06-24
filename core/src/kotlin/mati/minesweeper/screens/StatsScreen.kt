package mati.minesweeper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align.left
import com.badlogic.gdx.utils.Align.right
import com.badlogic.gdx.utils.viewport.ScreenViewport
import mati.advancedgdx.AdvancedGame
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton
import mati.advancedgdx.utils.createLabel
import mati.advancedgdx.utils.createNPD
import mati.minesweeper.Game
import mati.minesweeper.gui.Timer
import mati.minesweeper.screens.NewGame.Difficulty
import mati.minesweeper.screens.NewGame.Size
import mati.minesweeper.statistics.Statistics.WinLose
import kotlin.properties.Delegates

class StatsScreen(game: Game) : Screen<Game>(game) {
    private val COLSPAN: Int = 2
    private var font: BitmapFont by Delegates.notNull<BitmapFont>()
    private var stage: Stage by Delegates.notNull<Stage>()

    override fun load() {
        stage = Stage(ScreenViewport())
        font = game.astManager["GeneralW", BitmapFont::class]
    }

    override fun show() {
        val table: Table = Table()
        val scroll: ScrollPane = ScrollPane(table)
        stage.addActor(scroll)
        scroll.setFillParent(true)
        table.pad(10f)

        table.add(createLabel("${if (game.stats.cheated) "Cheated Stats" else "Stats"}",
                game.astManager["Title", BitmapFont::class], if (game.stats.cheated) Color.RED else Color.WHITE))
                .colspan(COLSPAN).pad(5f)
        table.row()

        generalStats(table)
        specificStats(table)

        val back: TextButton = createButton("Back", font, createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        back.color = Color.RED
        table.add(back).colspan(COLSPAN).pad(5f).fill()
        back.addListener1 { e, a ->
            game.scrManager.change("Title")
        }

        Gdx.input.inputProcessor = stage
    }

    private fun generalStats(table: Table) {
        table.add(createLabel("General Stats", font, Color.BLUE)).colspan(COLSPAN).pad(5f)
        table.row()

        table.add(createLabel("Total games played:", font)).pad(5f).align(left)
        table.add(createLabel("${game.stats.wins.size}", font)).pad(5f).align(right)
        table.row()

        table.add(createLabel("Total wins:", font)).pad(5f).align(left)
        table.add(createLabel("${game.stats.totalWins()}", font)).pad(5f).align(right)
        table.row()

        table.add(createLabel("Win percentage:", font)).pad(5f).align(left)
        table.add(createLabel("${game.stats.winPercentage() * 100}%", font)).pad(5f).align(right)
        table.row()

        table.add(createLabel("Win streak:", font)).pad(5f).align(left)
        table.add(createLabel("${game.stats.winStreak()}", font)).pad(5f).align(right)
        table.row()

        table.add(createLabel("Total time played:", font)).pad(5f).align(left)
        val time: Array<Int> = Timer.formatTime(game.stats.totalTime())
        table.add(createLabel("${time[0]}h : ${time[1]}' : ${time[2]}'' : ${time[3]}", font)).pad(5f).align(right)
        table.row()
    }

    private fun specificStats(table: Table) {
        table.add(createLabel("Specific Stats", font, Color.BLUE)).colspan(COLSPAN).pad(5f)
        table.row()
        for (d in Difficulty.values()) {
            if (d != Difficulty.NONE)
                for (s in Size.values()) if (s != Size.NONE) specificStatsGUI(table, d, s)
        }
    }

    private fun specificStatsGUI(table: Table, d: Difficulty, s: Size) {
        val difficulty: String = "${d.name.substring(0, 1)}${d.name.substring(1).toLowerCase()}"
        val size: String = "${s.name.substring(0, 1)}${s.name.substring(1).toLowerCase()}"
        AdvancedGame.log.d("${this.javaClass.simpleName}", "$difficulty - $size")
        table.add(createLabel("$difficulty - $size", font, Color.GOLD)).colspan(COLSPAN).pad(5f)
        table.row()

        val wins: List<WinLose> = game.stats.filterWins(d, s)

        table.add(createLabel("Total games played:", font)).pad(5f).align(left)
        table.add(createLabel("${wins.size}", font)).pad(5f).align(right)
        table.row()

        table.add(createLabel("Total wins:", font)).pad(5f).align(left)
        table.add(createLabel("${game.stats.totalWins(wins)}", font)).pad(5f).align(right)
        table.row()

        table.add(createLabel("Win percentage:", font)).pad(5f).align(left)
        table.add(createLabel("${game.stats.winPercentage(wins) * 100}%", font)).pad(5f).align(right)
        table.row()

        table.add(createLabel("Win streak:", font)).pad(5f).align(left)
        table.add(createLabel("${game.stats.winStreak(wins)}", font)).pad(5f).align(right)
        table.row()

        table.add(createLabel("Total time played:", font)).pad(5f).align(left)
        val time: Array<Int> = Timer.formatTime(game.stats.totalTime(wins))
        table.add(createLabel("${time[0]}h : ${time[1]}' : ${time[2]}'' : ${time[3]}", font)).pad(5f).align(right)
        table.row()
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
}
