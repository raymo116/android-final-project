package com.example.ivories

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import layout.PianoKey

// Main activity for the app
class MainActivity : AppCompatActivity() {
    // lists that hold all of the keys
    private var whiteKeys = ArrayList<PianoKey>()
    private var blackKeys = ArrayList<PianoKey>()

    // Audio for the keys
    private var whitePianoNotes = ArrayList<Int>()
    private var whiteOrganNotes = ArrayList<Int>()
    private var blackPianoNotes = ArrayList<Int>()
    private var blackOrganNotes = ArrayList<Int>()

    // Constants used throughout
    private val screenHeight = 1080F
    private val keyShiftRight = 127F
    private val initialBlackOffset = 81F

    private var pianoSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadWhiteNotes()
        loadBlackNotes()

        setToFullScreen()
        createWhiteKeys()
        createBlackKeys()

        loadBackground()
        createMenu()
    }


    // Create all of the white keys
    private fun createWhiteKeys() {
        // creates 14 white keys
        for (i in 0..14) {
            //whiteKeys.add(createWhiteKey(0F+(keyShiftRight*i), screenHeight, i))
            whiteKeys.add(PianoKey(this.applicationContext, myLayout, 0F+(keyShiftRight*i), screenHeight, whitePianoNotes[i], R.drawable.whitekey))
        }
    }

    // Creates all of the black keys
    private fun createBlackKeys() {
        // Create black keys
        var i = 0
        var sound = 0

        // Two octaves
        for(p in 0..1) {
            // C# and D#
            for (k in 0..1) {
                blackKeys.add(PianoKey(this.applicationContext, myLayout, 0F+(keyShiftRight*i++)+initialBlackOffset, screenHeight, blackPianoNotes[sound++], R.drawable.blackkey))
            }

            // break between || and |||
            i++

            // F#, G#, and A#
            for (k in 0..2) {
                blackKeys.add(PianoKey(this.applicationContext, myLayout, 0F+(keyShiftRight*i++)+initialBlackOffset, screenHeight, blackPianoNotes[sound++], R.drawable.blackkey))
            }

            // Break between ||| and ||
            i++
        }
    }

    // Sets the screen to full so that you don't have to worry about the navigation bar
    private fun setToFullScreen() {
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    // Loads the white notes
    private fun loadWhiteNotes() {
        whitePianoNotes = intArrayOf(
            R.raw.piano_c3, R.raw.piano_d3, R.raw.piano_e3, R.raw.piano_f3, R.raw.piano_g3, R.raw.piano_a3, R.raw.piano_b3,
            R.raw.piano_c4, R.raw.piano_d4, R.raw.piano_e4, R.raw.piano_f4, R.raw.piano_g4, R.raw.piano_a4, R.raw.piano_b4,
            R.raw.piano_c5
        ).toCollection(ArrayList())

        whiteOrganNotes = intArrayOf(
            R.raw.organ_c3, R.raw.organ_d3, R.raw.organ_e3, R.raw.organ_f3, R.raw.organ_g3, R.raw.organ_a3, R.raw.organ_b3,
            R.raw.organ_c4, R.raw.organ_d4, R.raw.organ_e4, R.raw.organ_f4, R.raw.organ_g4, R.raw.organ_a4, R.raw.organ_b4,
            R.raw.organ_c5
        ).toCollection(ArrayList())
    }

    // Loads the black notes
    private fun loadBlackNotes() {
        blackPianoNotes = intArrayOf(
            R.raw.piano_cs3, R.raw.piano_ds3, R.raw.piano_fs3, R.raw.piano_gs3, R.raw.piano_as3,
            R.raw.piano_cs4, R.raw.piano_ds4, R.raw.piano_fs4, R.raw.piano_gs4, R.raw.piano_as4
        ).toCollection(ArrayList())

        blackOrganNotes = intArrayOf(
            R.raw.organ_cs3, R.raw.organ_ds3, R.raw.organ_fs3, R.raw.organ_gs3, R.raw.organ_as3,
            R.raw.organ_cs4, R.raw.organ_ds4, R.raw.organ_fs4, R.raw.organ_gs4, R.raw.organ_as4
        ).toCollection(ArrayList())
    }

    //Displays background of app
    private fun loadBackground() {
        val background = ImageView(this.applicationContext)
        background.setImageResource(R.drawable.speakerbar)
        background.x = 0f
        background.y = 0f
        myLayout.addView(background)
    }

    //Opens Menu Bar for Alternate Modes
    private fun openMenu(c: ImageView, m: ImageView, e: ImageView, o: ImageView, p: ImageView) {
        c.visibility = View.GONE
        m.visibility = View.VISIBLE
        e.visibility = View.VISIBLE
        o.visibility = View.VISIBLE
        p.visibility = View.VISIBLE
    }

    //Closes Menu Bar
    private fun closeMenu(c: ImageView, m: ImageView, e: ImageView, o: ImageView, p: ImageView) {
        m.visibility = View.GONE
        e.visibility = View.GONE
        o.visibility = View.GONE
        p.visibility = View.GONE
        c.visibility = View.VISIBLE
    }

    // Don't mess with this code. It took way too long to make work
    // I'll go back and clean it up later
    // - Matt
    @SuppressLint("ClickableViewAccessibility")
    private fun createMenu() {
        // Create Options icon
        // Creates a new image
        var cog = ImageView(this)

        // Add to the layout
        myLayout.addView(cog)

        // Set the skin
        cog.setImageResource(R.drawable.gearbutton)

        // Set the x and y coordinates
        cog.x = 100f
        cog.y = 100f

        // Set the background color
        cog.setBackgroundColor(Color.TRANSPARENT)

        var cogSound: MediaPlayer = MediaPlayer.create(this, R.raw.click_down) // Add audio here

        // Load Menu
        val menu = ImageView(this.applicationContext)
        menu.setImageResource(R.drawable.fullmenu)
        menu.x = 0f
        menu.y = 0f
        menu.scaleX = 1.025f
        menu.scaleY = 1.025f
        myLayout.addView(menu)
        menu.visibility = View.GONE

        // Creates Exit Button
        val exitButton = ImageView(this.applicationContext)
        exitButton.setImageResource(R.drawable.exitbutton)
        exitButton.x = 415f
        exitButton.y = 22f
        exitButton.scaleY = 0.95f
        myLayout.addView(exitButton)
        exitButton.visibility = View.GONE

        var exitSound: MediaPlayer = MediaPlayer.create(this, R.raw.click_down) // Add audio here

        // Create piano button
        val pianoButton = ImageView(this.applicationContext)
        pianoButton.setImageResource(R.drawable.pianobutton)
        pianoButton.x = 70f
        pianoButton.y = 150f
        pianoButton.scaleY = 0.95f
        myLayout.addView(pianoButton)
        pianoButton.visibility = View.GONE

        var pianoSound: MediaPlayer = MediaPlayer.create(this, R.raw.click_down) // Add audio here


        // Create organ button
        val organButton = ImageView(this.applicationContext)
        organButton.setImageResource(R.drawable.organbutton)
        organButton.x = 80f
        organButton.y = 550f
        organButton.scaleY = 0.95f
        myLayout.addView(organButton)
        organButton.visibility = View.GONE

        var organSound: MediaPlayer = MediaPlayer.create(this, R.raw.click_down) // Add audio here

        // Crates listener for the Cog button
        cog.setOnTouchListener { _, event ->
            // If it's being pressed
            if (event.action == MotionEvent.ACTION_DOWN) {

                // sound from here http://soundbible.com/1970-Pen-Clicks.html
                // make the key darker
                cog.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)

                cogSound.reset()
                cogSound = MediaPlayer.create(getBaseContext(), R.raw.click_down)
                cogSound.start()

                // If it's released
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Clear the color
                cog.clearColorFilter()

                cogSound.reset()
                cogSound = MediaPlayer.create(getBaseContext(), R.raw.click_up)
                cogSound.start()
                openMenu(cog, menu, exitButton, organButton, pianoButton)

            }

            true
        }

        exitButton.setOnTouchListener { _, event ->

            // If it's being pressed
            if (event.action == MotionEvent.ACTION_DOWN) {

                // sound from here http://soundbible.com/1970-Pen-Clicks.html
                // make the key darker
                exitButton.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
                exitSound.reset()
                exitSound = MediaPlayer.create(getBaseContext(), R.raw.click_down)
                exitSound.start()

                // If it's released
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Clear the color
                exitButton.clearColorFilter()

                exitSound.reset()
                exitSound = MediaPlayer.create(getBaseContext(), R.raw.click_up)
                exitSound.start()
                closeMenu(cog, menu, exitButton, organButton, pianoButton)

            }

            true
        }

        pianoButton.setOnTouchListener { _, event ->

            // If it's being pressed
            if (event.action == MotionEvent.ACTION_DOWN) {

                // make the key darker
                pianoButton.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
                pianoSound.reset()
                pianoSound = MediaPlayer.create(getBaseContext(), R.raw.click_down)
                pianoSound.start()

                // If it's released
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Clear the color
                pianoButton.clearColorFilter()

                pianoSound.reset()
                pianoSound = MediaPlayer.create(getBaseContext(), R.raw.click_up)
                pianoSound.start()
                closeMenu(cog, menu, exitButton, organButton, pianoButton)
                convertToPianoSounds()
                if(!pianoSelected) {
                    pianoSelected = true
                }
            }

            true
        }

        organButton.setOnTouchListener { _, event ->

            // If it's being pressed
            if (event.action == MotionEvent.ACTION_DOWN) {

                // make the key darker
                organButton.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
                organSound.reset()
                organSound = MediaPlayer.create(getBaseContext(), R.raw.click_down)
                organSound.start()

                // If it's released
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Clear the color
                organButton.clearColorFilter()

                organSound.reset()
                organSound = MediaPlayer.create(getBaseContext(), R.raw.click_up)
                organSound.start()
                closeMenu(cog, menu, exitButton, organButton, pianoButton)
                convertToOrganSounds()
            }

            true
        }
    }

    //switches sounds to piano
    private fun convertToPianoSounds() {
        pianoSelected = true
        for(i in 0 until(whiteKeys.count())) {
            whiteKeys[i].changeSound(whitePianoNotes[i], pianoSelected)
        }

        for(i in 0 until (blackKeys.count())) {
            blackKeys[i].changeSound(blackPianoNotes[i], pianoSelected)
        }
    }

    //switches sounds to organ
    private fun convertToOrganSounds() {
        pianoSelected = false
        for(i in 0 until whiteKeys.count()) {
            whiteKeys[i].changeSound(whiteOrganNotes[i], pianoSelected)
        }

        for(i in 0 until (blackKeys.count())) {
            blackKeys[i].changeSound(blackOrganNotes[i], pianoSelected)
        }
    }

}
