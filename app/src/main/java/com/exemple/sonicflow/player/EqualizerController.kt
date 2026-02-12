import android.media.audiofx.Equalizer
import android.media.MediaPlayer

class EqualizerController(private val player: MediaPlayer) {

    private var equalizer: Equalizer? = null

    fun init() {
        equalizer = Equalizer(0, player.audioSessionId).apply {
            enabled = true
        }
    }

    fun release() {
        equalizer?.release()
    }
}
