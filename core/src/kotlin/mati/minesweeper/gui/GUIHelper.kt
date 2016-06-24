package mati.minesweeper.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import mati.advancedgdx.utils.*
import mati.minesweeper.Game
import mati.minesweeper.Game.Static.game
import mati.minesweeper.board.Board
import mati.minesweeper.gui.Timer.TimerSerializer
import mati.minesweeper.input.CamButtonListener
import mati.minesweeper.io.BoardSerializer
import mati.minesweeper.io.CamSerializer
import mati.minesweeper.io.CamSerializer.Serializer
import mati.minesweeper.screens.GameS

object GUIHelper {
    fun GUIBase(gui: Stage, board: Board): Image {
        if (isAndroid()) {
            androidButtons(board, gui)
        }

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

        return guiBottom
    }

    private fun androidButtons(board: Board, gui: Stage) {
        val guiLeft: Image = Image(createNPD(game.astManager["GUIl", Texture::class], 24, 24, 0, 0))
        guiLeft.setBounds(0f, 0f, 64f, gui.height)
        guiLeft.color = Color.GOLD
        gui.addActor(guiLeft)
        guiLeft.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })

        val table: Table = Table()
        gui.addActor(table)
        table.setBounds(0f, 32f + 24f, 64f, gui.height - 32f - 64f - 2f * 24f)

        val buttonNull: TextButton = createButton("Safe", game.astManager["AndroidF", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        buttonNull.color = Color.GRAY
        buttonNull.addListener2 { e, a ->
            board.mode = Board.AndroidMode.NULL
        }
        table.add(buttonNull).pad(5f)
        table.row()

        val buttonOpen: TextButton = createButton("Open", game.astManager["AndroidF", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        buttonOpen.color = Color.RED
        buttonOpen.addListener2 { e, a ->
            board.mode = Board.AndroidMode.OPEN
        }
        table.add(buttonOpen).pad(5f)
        table.row()

        val buttonFlag: TextButton = createButton("Flag", game.astManager["AndroidF", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        buttonFlag.color = Color.BLUE
        buttonFlag.addListener2 { e, a ->
            board.mode = Board.AndroidMode.FLAG
        }
        table.add(buttonFlag).pad(5f)

        val group: ButtonGroup<TextButton> = ButtonGroup(buttonNull, buttonOpen, buttonFlag)
        group.setMinCheckCount(1)
        group.setMaxCheckCount(1)
        group.setChecked(buttonNull.text.toString())
    }

    fun guiButtons(board: Board, gui: Stage, timer: Timer, cam: OrthographicCamera): Array<CamButtonListener> {
        val table: Table = Table()
        gui.addActor(table)
        table.setBounds(24f, gui.height - 64, gui.width - 48, 64f)

        val image: Image = Image(createNPD(game.astManager["Dialog", Texture::class], 5))
        image.setBounds(0f, 0f, gui.width, gui.height)
        image.color = Color.RED

        val pause: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["PauseUp", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["PauseDown", Texture::class])))

        val dialog: Dialog = Dialog("", Window.WindowStyle(game.astManager["GeneralW", BitmapFont::class],
                Color.WHITE, createNPD(game.astManager["Dialog", Texture::class], 5)))
        dialog.color = Color.ORANGE

        val resume: TextButton = createButton("Continue", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        resume.color = Color.GREEN
        resume.addListener1 { e, a ->
            if (isDesktop())
                Gdx.graphics.setCursor(game.cursors[1])
            if (!board.first)
                timer.start()
            dialog.hide()
            image.remove()
        }
        dialog.button(resume)

        val save: TextButton = createButton("Save and Exit", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        save.color = Color.YELLOW
        save.addListener1 { e, a ->
            game.ioManager.save("board", board, BoardSerializer())
            game.ioManager.save("timer", timer, TimerSerializer())
            val camSer: CamSerializer = CamSerializer()
            camSer.init(cam)
            game.ioManager.save("camera", camSer, Serializer())

            game.scrManager.change("Title")
        }
        dialog.button(save)

        val exit: TextButton = createButton("Leave Game", game.astManager["GeneralW", BitmapFont::class],
                createNPD(game.astManager["ButtonUp", Texture::class], 8),
                createNPD(game.astManager["ButtonDown", Texture::class], 8))
        exit.color = Color(0.5f, 0f, 0f, 1f)
        var secure: Boolean = false
        exit.addListener1 { e, a ->
            if (!secure) {
                secure = true
                exit.color = Color.RED

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
                    exit.color = Color(0.5f, 0f, 0f, 1f)
                }
                Thread(runnable).start()
            } else {
                game.ioManager.delete("board").delete("timer").delete("camera")
                secure = false
                game.scrManager.change("Title")
                dialog.hide()
            }
        }
        dialog.button(exit)

        pause.addListener1 { e, a ->
            exit.color = Color(0.5f, 0f, 0f, 1f)
            secure = false
            if (isDesktop())
                Gdx.graphics.setCursor(game.cursors[0])
            timer.stop()
            gui.addActor(image)
            dialog.show(gui)
        }
        table.add(pause).pad(5f)

        val left: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamLU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamLD", Texture::class])))
        val CBLL = CamButtonListener()
        left.addListener(CBLL)
        table.add(left).pad(5f)

        val up: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamUU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamUD", Texture::class])))
        val CBLU = CamButtonListener()
        up.addListener(CBLU)
        table.add(up).pad(5f)

        val down: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamDU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamDD", Texture::class])))
        val CBLD = CamButtonListener()
        down.addListener(CBLD)
        table.add(down).pad(5f)

        val right: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamRU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamRD", Texture::class])))
        val CBLR = CamButtonListener()
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

        val zCn: TextButton = createButton("", game.astManager["GeneralW", BitmapFont::class],
                TextureRegionDrawable(TextureRegion(game.astManager["CamCU", Texture::class])),
                TextureRegionDrawable(TextureRegion(game.astManager["CamCD", Texture::class])))
        zCn.addListener1 { e, a ->
            cam.zoom = 1f
            cam.update()
        }
        table.add(zCn).pad(5f)

        return arrayOf(CBLL, CBLU, CBLD, CBLR)
    }

    fun gameOverDialog(gui: Stage) {
        game.astManager["ExplosionS", Sound::class].play(0.5f)
        val dialog: Dialog = Dialog("", Window.WindowStyle(Game.game.astManager["GameOverF", BitmapFont::class],
                Color.WHITE, createNPD(Game.game.astManager["Dialog", Texture::class], 5)))
        dialog.color = Color.RED


        dialog.text(createLabel("Game Over!", Game.game.astManager["GameOverF", BitmapFont::class]))

        val retry: TextButton = createButton("Try Again", Game.game.astManager["GeneralW", BitmapFont::class],
                createNPD(Game.game.astManager["ButtonUp", Texture::class], 8),
                createNPD(Game.game.astManager["ButtonDown", Texture::class], 8))
        retry.color = Color.GREEN
        retry.addListener1 { e, a ->
            (Game.game.scrManager["Game"] as GameS).newGame = true
            Game.game.scrManager.change("New")
            dialog.hide()
        }
        dialog.button(retry)

        val menu: TextButton = createButton("Main Menu", Game.game.astManager["GeneralW", BitmapFont::class],
                createNPD(Game.game.astManager["ButtonUp", Texture::class], 8),
                createNPD(Game.game.astManager["ButtonDown", Texture::class], 8))
        menu.color = Color.RED
        menu.addListener1 { e, a ->
            Game.game.scrManager.change("Title")
            dialog.hide()
        }
        dialog.button(menu)

        dialog.show(gui)
    }

    fun winDialog(gui: Stage) {
        game.astManager["WinS", Sound::class].play(0.5f)
        val dialog: Dialog = Dialog("", Window.WindowStyle(Game.game.astManager["WinF", BitmapFont::class],
                Color.WHITE, createNPD(Game.game.astManager["Dialog", Texture::class], 5)))
        dialog.color = Color.GREEN

        dialog.text(createLabel("Congratulations!", Game.game.astManager["WinF", BitmapFont::class]))

        val replay: TextButton = createButton("Play Again",
                Game.game.astManager["GeneralW", BitmapFont::class],
                createNPD(Game.game.astManager["ButtonUp", Texture::class], 8),
                createNPD(Game.game.astManager["ButtonDown", Texture::class], 8))
        replay.color = Color.GREEN
        replay.addListener1 { e, a ->
            (Game.game.scrManager["Game"] as GameS).newGame = true
            Game.game.scrManager.change("New")
            dialog.hide()
        }
        dialog.button(replay)

        val menu: TextButton = createButton("Main Menu", Game.game.astManager["GeneralW", BitmapFont::class],
                createNPD(Game.game.astManager["ButtonUp", Texture::class], 8),
                createNPD(Game.game.astManager["ButtonDown", Texture::class], 8))
        menu.color = Color.RED
        menu.addListener1 { e, a ->
            Game.game.scrManager.change("Title")
            dialog.hide()
        }
        dialog.button(menu)

        dialog.show(gui)
    }
}
