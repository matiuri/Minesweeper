package mati.minesweeper.io

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import mati.advancedgdx.io.Serializable

class CamSerializer() {
    var position: Vector3 = Vector3()
    var zoom: Float = 0f

    fun init(cam: OrthographicCamera) {
        position = cam.position
        zoom = cam.zoom
    }

    class Serializer() : Serializable<CamSerializer> {
        private var camser: CamSerializer = CamSerializer()

        override fun preserialize(t: CamSerializer) {
            camser = t
        }

        override fun recover(): CamSerializer {
            return camser
        }

    }
}
