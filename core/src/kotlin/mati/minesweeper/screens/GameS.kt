package mati.minesweeper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import mati.advancedgdx.AdvancedGame
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.isDesktop
import mati.minesweeper.Game
import mati.minesweeper.board.Board
import mati.minesweeper.board.Cell
import mati.minesweeper.gui.*
import mati.minesweeper.gui.GUIHelper.GUIBase
import mati.minesweeper.gui.GUIHelper.guiButtons
import mati.minesweeper.gui.GUIHelper.winDialog
import mati.minesweeper.gui.GUIHelper.gameOverDialog
import mati.minesweeper.gui.Timer.TimerSerializer
import mati.minesweeper.input.CamButtonListener
import mati.minesweeper.io.BoardSerializer
import mati.minesweeper.io.CamSerializer
import mati.minesweeper.io.CamSerializer.Serializer
import mati.minesweeper.screens.NewGame.Properties
import kotlin.properties.Delegates

class GameS(game: Game) : Screen<Game>(game) {
    var stage: Stage by Delegates.notNull<Stage>()
    var gui: Stage by Delegates.notNull<Stage>()
    var timer: Timer by Delegates.notNull<Timer>()
    var cam: OrthographicCamera by Delegates.notNull<OrthographicCamera>()
    var CBLL: CamButtonListener by Delegates.notNull<CamButtonListener>()
    var CBLU: CamButtonListener by Delegates.notNull<CamButtonListener>()
    var CBLD: CamButtonListener by Delegates.notNull<CamButtonListener>()
    var CBLR: CamButtonListener by Delegates.notNull<CamButtonListener>()
    var newGame: Boolean = false
    private var camZ: Float = 0f

    override fun load() {
        cam = OrthographicCamera()
        stage = Stage(ScreenViewport(cam))
        camZ = cam.zoom
        gui = Stage(ScreenViewport())
        timer = Timer(game)
    }

    override fun show() {
        if (isDesktop())
            Gdx.graphics.setCursor(game.cursors[1])

        if (newGame)
            timer.reset()
        else
            timer = game.ioManager.load("timer", TimerSerializer::class)
        val board: Board = if (newGame) Board()
        else game.ioManager.load("board", BoardSerializer::class)
        if (newGame)
            board.init()
        else if (!board.first)
            timer.start()
        board.timer = timer
        stage.addActor(board)

        val guiBottom: Image = GUIBase(gui, board)

        gui.addActor(timer.label)
        timer.label.setPosition(26f, 4f)

        val fCounter: FlagCounter = FlagCounter(game, board, guiBottom.width - 24f)
        if (!newGame) fCounter.init(board.flags)
        board.fCounter = fCounter
        fCounter.label.setPosition(guiBottom.width - 24f - fCounter.label.width, 4f)
        fCounter.label.setAlignment(Align.right)
        gui.addActor(fCounter.label)

        val CBL: Array<CamButtonListener> = guiButtons(board, gui, timer, cam)
        CBLL = CBL[0]
        CBLU = CBL[1]
        CBLD = CBL[2]
        CBLR = CBL[3]

        if (newGame || !game.ioManager.exists("camera")) {
            cam.position.x = board.wh.toFloat() * board.size / 2f
            cam.position.y = board.wh.toFloat() * board.size / 2f
            cam.zoom = camZ
            cam.update()
        } else {
            val camData: CamSerializer = game.ioManager.load("camera", Serializer::class)
            cam.position.set(camData.position)
            cam.zoom = camData.zoom
        }

        Gdx.input.inputProcessor = InputMultiplexer(gui, stage)
    }

    override fun render(delta: Float) {
        timer.update(delta)
        Cell.Static.update(delta)

        if (CBLD.pressed || CBLL.pressed || CBLR.pressed || CBLU.pressed) {
            val amount: Float = 2.5f * cam.zoom * delta * 100f
            if (CBLD.pressed) cam.position.y -= amount
            else if (CBLL.pressed) cam.position.x -= amount
            else if (CBLR.pressed) cam.position.x += amount
            else if (CBLU.pressed) cam.position.y += amount
            cam.update()
        }

        stage.act(delta)
        stage.draw()

        gui.act(delta)
        gui.draw()
    }

    fun gameOver() {
        timer.stop()
        AdvancedGame.log.d("${this.javaClass.simpleName}", "Game Over. ${timer.label.text}")
        Gdx.input.inputProcessor = gui
        if (isDesktop())
            Gdx.graphics.setCursor(game.cursors[0])
        gameOverDialog(gui)
        game.ioManager.delete("board").delete("timer").delete("camera")
        game.stats.addEntry(false, Properties.difficulty, Properties.size, timer.time)
    }

    fun win() {
        timer.stop()
        AdvancedGame.log.d("${this.javaClass.simpleName}", "WIN! ${timer.label.text}")
        Gdx.input.inputProcessor = gui
        if (isDesktop())
            Gdx.graphics.setCursor(game.cursors[0])
        winDialog(gui)
        game.ioManager.delete("board").delete("timer").delete("camera")
        game.stats.addEntry(true, Properties.difficulty, Properties.size, timer.time)
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
