package com.example.ivories

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.VolumeShaper
import android.os.Handler
import android.provider.MediaStore
import android.widget.ImageView


class MainActivity : AppCompatActivity() {
    // lists that hold all of the keys
    var whiteKeys = ArrayList<ImageView>()
    var blackKeys = ArrayList<ImageView>()

    // Audio for the keys
    // ToDo: Still have to remake C3
    var whiteNotes = ArrayList<Int>()
    var blackNotes = ArrayList<Int>()

    // Constants used throughout
    val screenHeight = 1080F
    val screenWidth = 1920F
    val keyShiftUp = 480F
    val keyShiftRight = 127F
    val initialBlackOffset = 81F

    val duration = 100L

    // ToDO: Have the different key sounds
    // ToDO: Fix the note cutoff
    // ToDO: Finish the UI
    // ToDO: Fix the memory error that causes it to crash
    // ToDo: Buzzes when you hit a key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadWhiteNotes()
        loadBlackNotes()

        setToFullScreen()
        createWhiteKeys()
        createBlackKeys()
    }

    // ToDo: Refactor these when you get the change
    fun createWhiteKey( _x: Float, _y: Float, i: Int): ImageView {

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
        addAudioToKey(newView, whiteNotes[i])

        // return the object
        return newView
    }

    fun createBlackKey( _x: Float, _y: Float, sound: Int): ImageView {

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
        addAudioToKey(newView, sound)

        // return the object
        return newView
    }

    // Create all of the white keys
    fun createWhiteKeys() {
        // creates 14 white keys
        for (i in 0..14) {
            whiteKeys.add(createWhiteKey(0F+(keyShiftRight*i), screenHeight, i))
        }
    }

    // Creates all of the black keys
    fun createBlackKeys() {
        // Create black keys
        var i = 0
        var sound = 0

        // Two octaves
        for(p in 0..1) {
            // C# and D#
            for (k in 0..1) {
                blackKeys.add(createBlackKey(0F+(keyShiftRight*i++), screenHeight, blackNotes[sound++]))
            }

            // break between || and |||
            i++

            // F#, G#, and A#
            for (k in 0..2) {
                blackKeys.add(createBlackKey(0F+(keyShiftRight*i++), screenHeight, blackNotes[sound++]))
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
    fun addAudioToKey(key: ImageView, sound: Int) {

        val times = floatArrayOf(0f, 1f) // can add more points, volume points must correspond to time points
        val volumes = floatArrayOf(1f, 0f)

        // Create a media player
        var keySound = MediaPlayer.create(this, sound) // Add audio here

        // Create an on-touch listener
        key.setOnTouchListener(View.OnTouchListener { v, event ->

            // If it's being pressed
            if (event.action == MotionEvent.ACTION_DOWN) {

                // Start the audio
                keySound.start()
                //println("Test1: " + volumeOut.volume)

                // make the key darker
                key.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)

            // If it's released
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Clear the color
                key.clearColorFilter()

                for(i in 1..10) {
                    keySound.setVolume((1/(i*i)).toFloat(),(1/(i*i)).toFloat())
                    Thread.sleep(2)
                }

                keySound.stop()

                // Reset the player
                keySound.prepare()
                keySound.setVolume((1).toFloat(),(1).toFloat())

            }

            true
        })
    }

    // Loads the white notes
    fun loadWhiteNotes() {
        whiteNotes = intArrayOf(

            // ToDo: add back c3
            R.raw.piano_c4, R.raw.piano_d3, R.raw.piano_e3, R.raw.piano_f3, R.raw.piano_g3, R.raw.piano_a3, R.raw.piano_b3,
            R.raw.piano_c4, R.raw.piano_d4, R.raw.piano_e4, R.raw.piano_f4, R.raw.piano_g4, R.raw.piano_a4, R.raw.piano_b4,
            R.raw.piano_c5

        ).toCollection(ArrayList())
    }

    // Loads the black notes
    fun loadBlackNotes() {
        blackNotes = intArrayOf(

            R.raw.piano_cs3, R.raw.piano_ds3, R.raw.piano_fs3, R.raw.piano_gs3, R.raw.piano_as3,
            R.raw.piano_cs4, R.raw.piano_ds4, R.raw.piano_fs4, R.raw.piano_gs4, R.raw.piano_as4

        ).toCollection(ArrayList())
    }
}
