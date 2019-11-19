package com.example.ivories

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.LinearLayout
import android.R.attr.button
import android.graphics.Color
import android.widget.ImageView
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics


class MainActivity : AppCompatActivity() {
//    newView.x = 5F
//    newView.y = 536F

    var whiteKeys = ArrayList<ImageView>()

    var screenHeight = 1080F
    var screenWidth = 1920F
    var keyShiftUp = 480F
    var keyShiftRight = 127F



    fun createWhiteKey( _x: Float, _y: Float): ImageView {
        var newView: ImageView
        newView = ImageView(this)
        myLayout.addView(newView)
        newView.setImageResource(R.drawable.whitekey)
        newView.x = _x
        newView.y = _y - keyShiftUp
        newView.setBackgroundColor(Color.TRANSPARENT)
        return newView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        for (i in 0..14) {
            whiteKeys.add(createWhiteKey(0F+(keyShiftRight*i), screenHeight))
        }

//        var c5 = MediaPlayer.create(this, R.raw.piano_c5) // Add audio here

//        c3.setOnTouchListener(View.OnTouchListener { v, event ->
////
////            // If it's being pressed
////            if (event.action == MotionEvent.ACTION_DOWN) {
////
////                // Start the audio
////                c5.start()
////
////                // If it's released
////            } else if (event.action == MotionEvent.ACTION_UP) {
////
////                c5.stop()
////                c5.prepare()
////
////            }
////
////            true
////        })

    }




}
