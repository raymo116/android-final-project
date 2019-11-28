package com.example.ivories

import android.content.ComponentCallbacks2
import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.Image
import android.media.VolumeShaper
import android.os.Handler
import android.provider.MediaStore
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Exception


// ToDo: Fix the clipping at the end of notes
// ToDo: Fix the bottom note being too low
// ToDo: fix the organ audio being too low
// ToDo: Fix the organ clipping when looping

class MainActivity : AppCompatActivity() {
    // lists that hold all of the keys
    var whiteKeys = ArrayList<pianoKey>()
    var blackKeys = ArrayList<pianoKey>()

    // Audio for the keys
    // ToDo: Still have to remake C3
    var whitePianoNotes = ArrayList<Int>()
    var whiteOrganNotes = ArrayList<Int>()
    var blackPianoNotes = ArrayList<Int>()
    var blackOrganNotes = ArrayList<Int>()

    // Constants used throughout
    val screenHeight = 1080F
    val screenWidth = 1920F
    val keyShiftUp = 480F
    val keyShiftRight = 127F
    val initialBlackOffset = 81F

    val duration = 100L

    var pianoSelected = true

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

    // ToDo: Refactor these when you get the change
    fun createWhiteKey( _x: Float, _y: Float, i: Int): ImageView {

        // Creates a new image
        var newView = ImageView(this)

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
//        addAudioToKey(newView, whitePianoNotes[i], whiteOrganNotes[i])

        // return the object
        return newView
    }

    fun createBlackKey( _x: Float, _y: Float, pianoSound: Int, organSound: Int): ImageView {

        // Creates a new image
        var newView = ImageView(this)

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
//        addAudioToKey(newView, pianoSound, organSound)

        // return the object
        return newView
    }

    // Create all of the white keys
    fun createWhiteKeys() {
        // creates 14 white keys
        for (i in 0..14) {
            //whiteKeys.add(createWhiteKey(0F+(keyShiftRight*i), screenHeight, i))
            whiteKeys.add(pianoKey(this.applicationContext, myLayout, 0F+(keyShiftRight*i), screenHeight, whitePianoNotes[i], R.drawable.whitekey))
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
                blackKeys.add(pianoKey(this.applicationContext, myLayout, 0F+(keyShiftRight*i++)+initialBlackOffset, screenHeight, blackPianoNotes[sound++], R.drawable.blackkey))

            }

            // break between || and |||
            i++

            // F#, G#, and A#
            for (k in 0..2) {
                blackKeys.add(pianoKey(this.applicationContext, myLayout, 0F+(keyShiftRight*i++)+initialBlackOffset, screenHeight, blackPianoNotes[sound++], R.drawable.blackkey))

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
    fun addAudioToKey(key: ImageView, ps: Int) {

        val times = floatArrayOf(0f, 1f) // can add more points, volume points must correspond to time points
        val volumes = floatArrayOf(1f, 0f)

        // Create a media player
        var pianoSound = MediaPlayer.create(this, ps) // Add audio here

            // Piano Sound
            key.setOnTouchListener(View.OnTouchListener { v, event ->

                // If it's being pressed
                if (pianoSelected && event.action == MotionEvent.ACTION_DOWN) {

                    // Start the audio
                    pianoSound.start()
                    //println("Test1: " + volumeOut.volume)

                    // make the key darker
                    key.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)

                    // If it's released
                } else if (pianoSelected && event.action == MotionEvent.ACTION_UP) {
                    // Clear the color
                    key.clearColorFilter()

                    for(i in 1..10) {
                        pianoSound.setVolume((1/(i*i)).toFloat(),(1/(i*i)).toFloat())
                        Thread.sleep(2)
                    }

                    pianoSound.stop()

                    // Reset the player
                    pianoSound.prepare()
                    pianoSound.setVolume((1).toFloat(),(1).toFloat())

                }

                true
            })


    }

    // Loads the white notes
    fun loadWhiteNotes() {
        whitePianoNotes = intArrayOf(

            // ToDo: add back c3
            R.raw.piano_c4, R.raw.piano_d3, R.raw.piano_e3, R.raw.piano_f3, R.raw.piano_g3, R.raw.piano_a3, R.raw.piano_b3,
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
    fun loadBlackNotes() {
        blackPianoNotes = intArrayOf(

            R.raw.piano_cs3, R.raw.piano_ds3, R.raw.piano_fs3, R.raw.piano_gs3, R.raw.piano_as3,
            R.raw.piano_cs4, R.raw.piano_ds4, R.raw.piano_fs4, R.raw.piano_gs4, R.raw.piano_as4

        ).toCollection(ArrayList())
        blackOrganNotes = intArrayOf(

            R.raw.organ_cs3, R.raw.organ_ds3, R.raw.organ_fs3, R.raw.organ_gs3, R.raw.organ_as3,
            R.raw.organ_cs4, R.raw.organ_ds4, R.raw.organ_fs4, R.raw.organ_gs4, R.raw.organ_as4

        ).toCollection(ArrayList())
    }

    fun loadBackground() {
        val background = ImageView(this.applicationContext)
        background.setImageResource(R.drawable.speakerbar)
        background.x = 0f
        background.y = 0f
        myLayout.addView(background)
    }

    fun openMenu(c: ImageView, m: ImageView, e: ImageView, o: ImageView, p: ImageView) {
        c.visibility = View.GONE
        m.visibility = View.VISIBLE
        e.visibility = View.VISIBLE
        o.visibility = View.VISIBLE
        p.visibility = View.VISIBLE
    }

    fun closeMenu(c: ImageView, m: ImageView, e: ImageView, o: ImageView, p: ImageView) {
        m.visibility = View.GONE
        e.visibility = View.GONE
        o.visibility = View.GONE
        p.visibility = View.GONE
        c.visibility = View.VISIBLE
    }

    // Don't fuck with this code. It took waaay too long to make work
    // I'll go back and clean it up later
    // - Matt
    fun createMenu() {

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



        cog.setOnTouchListener(View.OnTouchListener { v, event ->

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
        })

        exitButton.setOnTouchListener(View.OnTouchListener { v, event ->

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
        })

        pianoButton.setOnTouchListener(View.OnTouchListener { v, event ->

            // If it's being pressed
            if (event.action == MotionEvent.ACTION_DOWN) {

                // sound from here http://soundbible.com/1970-Pen-Clicks.html
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
        })

        organButton.setOnTouchListener(View.OnTouchListener { v, event ->

            // If it's being pressed
            if (event.action == MotionEvent.ACTION_DOWN) {

                // sound from here http://soundbible.com/1970-Pen-Clicks.html
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
        })
    }

    fun convertToPianoSounds() {
        pianoSelected = true
        for(i in 0..(whiteKeys.count()-1)) {
            whiteKeys[i].changeSound(whitePianoNotes[i], pianoSelected)
        }

        for(i in 0..(blackKeys.count()-1)) {
            blackKeys[i].changeSound(blackPianoNotes[i], pianoSelected)
        }
    }

    fun convertToOrganSounds() {
        pianoSelected = false
        for(i in 0..(whiteKeys.count()-1)) {
            whiteKeys[i].changeSound(whiteOrganNotes[i], pianoSelected)
        }

        for(i in 0..(blackKeys.count()-1)) {
            blackKeys[i].changeSound(blackOrganNotes[i], pianoSelected)
        }
    }

    class pianoKey(context: Context, myLayout: ConstraintLayout, _x: Float, _y: Float, sound: Int, image: Int) {
        var keySound = MediaPlayer()
        var newView = ImageView(context)
        var parentContext = context
        val initialBlackOffset = 81F
        val keyShiftUp = 480F


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
        fun addAudioToKey(sound: Int, pianoSelected: Boolean = true) {

            val times = floatArrayOf(0f, 1f) // can add more points, volume points must correspond to time points
            val volumes = floatArrayOf(1f, 0f)


            if(!pianoSelected) {
                keySound.isLooping = true
            }

            keySound.reset()
            keySound = MediaPlayer.create(parentContext, sound)

            // Create an on-touch listener
            newView.setOnTouchListener(View.OnTouchListener { v, event ->

                // If it's being pressed
                if (event.action == MotionEvent.ACTION_DOWN) {

                    // Start the audio
                    keySound.start()
                    //println("Test1: " + volumeOut.volume)

                    // make the key darker
                    newView.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)

                    // If it's released
                } else if (event.action == MotionEvent.ACTION_UP) {
                    // Clear the color
                    newView.clearColorFilter()

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

        fun changeSound(sound: Int, isPiano: Boolean) {
            keySound.release()
            keySound = MediaPlayer.create(parentContext, sound)
            keySound.isLooping = !isPiano
        }
    }
}
