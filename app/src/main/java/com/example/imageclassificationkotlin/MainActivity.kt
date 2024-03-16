package com.example.imageclassificationkotlin

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.*

class MainActivity : AppCompatActivity(), GetTouch {
    var resultTv: TextView? = null
    var recognize: Button? = null
    var clear:android.widget.Button? = null
    var getTouch: GetTouch? = null
    var drawingView: DrawingView?= null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recognize = findViewById(R.id.button)
        clear = findViewById(R.id.button2)
        resultTv = findViewById(R.id.textView2)

        drawingView = findViewById(R.id.draw)
        getTouch = this
        drawingView?.getTouch = getTouch


        //TODO create ink builder



        // Specify the recognition model for a language


        //Download the model



        recognize?.setOnClickListener(View.OnClickListener {


        })

        clear?.setOnClickListener(View.OnClickListener {
            drawingView?.clear()

        })


    }

    override fun getTouchMethod(motionEvent: MotionEvent?) {
        addNewTouchEvent(motionEvent!!)
    }

    // Call this each time there is a new event.
    fun addNewTouchEvent(event: MotionEvent) {

    }


}