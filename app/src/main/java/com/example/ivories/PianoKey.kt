package layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

// The class that represents each individual note
// Takes in the context, constraint layout, x and y offset, sound that should be player, and the image for the key
class PianoKey(context: Context, myLayout: ConstraintLayout, _x: Float, _y: Float, sound: Int, image: Int) {

    // Sound to be played when the key is pressed
    var keySound = MediaPlayer()

    // View that acts as the key
    var newView = ImageView(context)

    // The context of the key
    var parentContext = context

    // The amount that the key should be shifted up from the bottom of the screen
    private val keyShiftUp = 480F

    // On Create
    init {
        // Set the skin
        newView.setImageResource(image)

        // Set the x and y coordinates
        newView.x = _x
        newView.y = _y - keyShiftUp

        // Set the background color
        newView.setBackgroundColor(Color.TRANSPARENT)

        // Assign audio to the key
        addAudioToKey(sound)

        // Add to the layout
        myLayout.addView(newView)
    }

    // Add audio to a single key
    @SuppressLint("ClickableViewAccessibility")
    fun addAudioToKey(sound: Int, pianoSelected: Boolean = true) {

        // Sets the audio for the key
        changeSound(sound, pianoSelected)

        // Create an on-touch listener
        newView.setOnTouchListener { _, event ->

            // If it's being pressed
            if (event.action == MotionEvent.ACTION_DOWN) {

                // Start the audio
                keySound.start()
                //println("Test1: " + volumeOut.volume)

                // make the key darker
                newView.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)

                // Vibrate the phone for tactile feedback
                vibratePhone(50)

                // If it's released
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Clear the color
                newView.clearColorFilter()

                // Gradually reduce volume to reduce clipping
                for(i in 1..10) {
                    keySound.setVolume((1/(i*i)).toFloat(),(1/(i*i)).toFloat())
                    Thread.sleep(2)
                }

                // Stop the audio
                keySound.stop()

                // Reset the player
                keySound.prepare()
                keySound.setVolume(1F,1F)
            }

            true
        }
    }

    // Change the sound for a single key
    fun changeSound(sound: Int, isPiano: Boolean) {
        // Deletes the old media player
        keySound.release()

        // Creates a new one
        keySound = MediaPlayer.create(parentContext, sound)

        // Loops if organ
        keySound.isLooping = !isPiano
    }

    // Makes the phone vibrate
    private fun vibratePhone(time:Long) {
        // Creates vibrator
        val vibrator = parentContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Vibrates the phone based on the API level
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(time)
        }
    }
}
