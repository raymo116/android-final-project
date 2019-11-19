package com.example.ivories

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView


class MainActivity : AppCompatActivity() {
    // lists that hold all of the keys
    var whiteKeys = ArrayList<ImageView>()
    var blackKeys = ArrayList<ImageView>()

    // Constants used throughout
    val screenHeight = 1080F
    val screenWidth = 1920F
    val keyShiftUp = 480F
    val keyShiftRight = 127F
    val initialBlackOffset = 81F

    // ToDO: Have the different key sounds
    // ToDO: Fix the note cutoff
    // ToDO: Finish the UI
    // ToDO: Fix the memory error that causes it to crash
    // ToDo: Buzzes when you hit a key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToFullScreen()
        createWhiteKeys()
        createBlackKeys()
    }

    // ToDo: Refactor these when you get the change
    fun createWhiteKey( _x: Float, _y: Float): ImageView {

        // Creates a new image
        var newView: ImageView

        // Assigns it to the context
        newView = ImageView(this)

        // Add to the layout
        myLayout.addView(newView)

        // Set the skin
        newView.setImageResource(R.drawable.whitekey)

        // Set the x and y coordinates
        newView.x = _x
        newView.y = _y - keyShiftUp

        // Set the background color
        newView.setBackgroundColor(Color.TRANSPARENT)

        // Assign audio to the key
        addAudioToKey(newView)

        // return the object
        return newView
    }

    fun createBlackKey( _x: Float, _y: Float): ImageView {

        // Creates a new image
        var newView: ImageView

        // Assigns it to the context
        newView = ImageView(this)

        // Add to the layout
        myLayout.addView(newView)

        // Set the skin
        newView.setImageResource(R.drawable.blackkey)

        // Set the x and y coordinates
        newView.x = _x + initialBlackOffset
        newView.y = _y - keyShiftUp

        // Set the background color
        newView.setBackgroundColor(Color.TRANSPARENT)

        // Assign audio to the key
        addAudioToKey(newView)

        // return the object
        return newView
    }

    // Create all of the white keys
    fun createWhiteKeys() {
        // creates 14 white keys
        for (i in 0..14) {
            whiteKeys.add(createWhiteKey(0F+(keyShiftRight*i), screenHeight))
        }
    }

    // Creates all of the black keys
    fun createBlackKeys() {
        // Create black keys
        var i = 0

        // Two octaves
        for(p in 0..1) {
            // C# and D#
            for (k in 0..1) {
                blackKeys.add(createBlackKey(0F+(keyShiftRight*i++), screenHeight))
            }

            // break between || and |||
            i++

            // F#, G#, and A#
            for (k in 0..2) {
                blackKeys.add(createBlackKey(0F+(keyShiftRight*i++), screenHeight))
            }

            // Break between ||| and ||
            i++
        }
    }

    // Sets the screen to full so that you don't have to worry about the navigation bar
    fun setToFullScreen() {
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    // Add audio to a single key
    fun addAudioToKey(key: ImageView) {

        // Create a media player
        var keySound = MediaPlayer.create(this, R.raw.piano_c5) // Add audio here

        // Create an on-touch listener
        key.setOnTouchListener(View.OnTouchListener { v, event ->

            // If it's being pressed
            if (event.action == MotionEvent.ACTION_DOWN) {

                // Start the audio
                keySound.start()

                // make the key darker
                key.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)

            // If it's released
            } else if (event.action == MotionEvent.ACTION_UP) {

                // Clear the color
                key.clearColorFilter()

                // Stop the media player
                keySound.stop()

                // Reset the player
                keySound.prepare()

            }

            true
        })
    }
}
