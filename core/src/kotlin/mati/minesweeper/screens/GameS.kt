package mati.minesweeper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import mati.advancedgdx.screens.Screen
import mati.advancedgdx.utils.addListener1
import mati.advancedgdx.utils.createButton
import mati.advancedgdx.utils.createNPD
import mati.minesweeper.Game
import mati.minesweeper.board.Board
import mati.minesweeper.board.Cell
import mati.minesweeper.gui.FlagCounter
import mati.minesweeper.gui.Timer
import mati.minesweeper.input.CamButtonListener
import kotlin.properties.Delegates

class GameS(game: Game) : Screen(game) {
    var stage: Stage by Delegates.notNull<Stage>()
    var gui: Stage by Delegates.notNull<Stage>()
    var timer: Timer by Delegates.notNull<Timer>()
    var cam: OrthographicCamera by Delegates.notNull<OrthographicCamera>()
    var CBLL: CamButtonListener by Delegates.notNull<CamButtonListener>()
    var CBLU: CamButtonListener by Delegates.notNull<CamButtonListener>()
    var CBLD: CamButtonListener by Delegates.notNull<CamButtonListener>()
    var CBLR: CamButtonListener by Delegates.notNull<CamButtonListener>()
    private var camZ: Float = 0f

    override fun load() {
        cam = OrthographicCamera()
        stage = Stage(ScreenViewport(cam))
        camZ = cam.zoom
        gui = Stage(ScreenViewport())
        timer = Timer(game as Game)
    }

    override fun show() {
        timer.reset()
        val board = Board(timer)
        stage.addActor(board)

        val guiTop: Image = Image(createNPD(game.astManager["GUIt", Texture::class], 0, 0, 24, 24))
        guiTop.setBounds(0f, gui.height - 64, gui.width, 64f)
        guiTop.color = Color.BLUE
        gui.addActor(guiTop)
        guiTop.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })

        val guiBottom: Image = Image(createNPD(game.astManager["GUIb", Texture::class], 0, 0, 24, 24))
        guiBottom.setBounds(0f, 0f, gui.width, 32f)
        guiBottom.color = Color(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 0.5f)
        gui.addActor(guiBottom)
        guiBottom.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })

        gui.addActor(timer.label)
        timer.label.setPosition(26f, 4f)

        val fCounter: FlagCounter = FlagCounter(game as Game, board, guiBottom.width - 24f)
        board.fCounter = fCounter
        fCounter.label.setPosition(guiBottom.width - 24f - fCounter.label.width, 4f)
        fCounter.label.setAlignment(Align.right)
        gui.addActor(fCounter.label)

        val table: Table = Table()
        gui.addActor(table)
        table.setBounds(24f, gui.height - 64, gui.width - 48, 64f)
        //table.debug = true

        val left: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamLU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamLD", Texture::class])))
        CBLL = CamButtonListener()
        left.addListener(CBLL)
        table.add(left).pad(5f)

        val up: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamUU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamUD", Texture::class])))
        CBLU = CamButtonListener()
        up.addListener(CBLU)
        table.add(up).pad(5f)

        val down: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamDU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamDD", Texture::class])))
        CBLD = CamButtonListener()
        down.addListener(CBLD)
        table.add(down).pad(5f)

        val right: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamRU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamRD", Texture::class])))
        CBLR = CamButtonListener()
        right.addListener(CBLR)
        table.add(right).pad(5f)

        val zUp: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamPU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamPD", Texture::class])))
        zUp.addListener1 { e, a ->
            cam.zoom -= 0.25f
            cam.update()
        }
        table.add(zUp).pad(5f)

        val zDn: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamMU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamMD", Texture::class])))
        zDn.addListener1 { e, a ->
            cam.zoom += 0.25f
            cam.update()
        }
        table.add(zDn).pad(5f)

        //gui.setDebugAll(true)

        cam.position.x = board.wh.toFloat() * board.size / 2f
        cam.position.y = board.wh.toFloat() * board.size / 2f
        cam.zoom = camZ
        cam.update()

        Gdx.input.inputProcessor = InputMultiplexer(gui, stage)
    }

    override fun render(delta: Float) {
        timer.update(delta)
        Cell.Static.update(delta)

        if (CBLD.pressed || CBLL.pressed || CBLR.pressed || CBLU.pressed) {
            if (CBLD.pressed) cam.position.y -= 1.5f
            else if (CBLL.pressed) cam.position.x -= 1.5f
            else if (CBLR.pressed) cam.position.x += 1.5f
            else if (CBLU.pressed) cam.position.y += 1.5f
            cam.update()
        }

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
