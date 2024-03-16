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
    var modelIdentifier: DigitalInkRecognitionModelIdentifier? = null
    var recognizer: DigitalInkRecognizer? = null
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
        // Specify the recognition model for a language

        try {
            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        } catch (e: MlKitException) {
            // language tag failed to parse, handle error.
        }
        if (modelIdentifier == null) {
            // no model was found, handle error.
        }
        var model: DigitalInkRecognitionModel =
            DigitalInkRecognitionModel.builder(modelIdentifier!!).build()


// Get a recognizer for the language



        //Download the model
        val remoteModelManager = RemoteModelManager.getInstance()

        remoteModelManager.download(model, DownloadConditions.Builder().build())
            .addOnSuccessListener {
               // Log.i(TAG, "Model downloaded")
             recognizer =
                    DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(model).build());
            }
            .addOnFailureListener { e: Exception ->
              //  Log.e(TAG, "Error while downloading a model: $e")
            }


        var inkBuilder = Ink.builder()
        lateinit var strokeBuilder: Ink.Stroke.Builder

        // Call this each time there is a new event.
        fun addNewTouchEvent(event: MotionEvent) {
            val action = event.actionMasked
            val x = event.x
            val y = event.y
            var t = System.currentTimeMillis()

            // If your setup does not provide timing information, you can omit the
            // third paramater (t) in the calls to Ink.Point.create
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    strokeBuilder = Ink.Stroke.builder()
                    strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                }
                MotionEvent.ACTION_MOVE -> strokeBuilder!!.addPoint(Ink.Point.create(x, y, t))
                MotionEvent.ACTION_UP -> {
                    strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                    inkBuilder.addStroke(strokeBuilder.build())
                }
                else -> {
                    // Action not relevant for ink construction
                }
            }
        }





        recognize?.setOnClickListener(View.OnClickListener {


// This is what to send to the recognizer.
            val ink = inkBuilder.build()

            recognizer?.recognize(ink)
                ?.addOnSuccessListener { result: RecognitionResult ->
                    // `result` contains the recognizer's answers as a RecognitionResult.
                    // Logs the text from the top candidate.
                   // Log.i(TAG, result.candidates[0].text)
                    resultTv?.text = result.candidates[0].text
                }
                ?.addOnFailureListener { e: Exception ->
                 //   Log.e(TAG, "Error during recognition: $e")
                }

        })

        clear?.setOnClickListener(View.OnClickListener {
            drawingView?.clear()
            inkBuilder = Ink.builder()

        })


    }

    override fun getTouchMethod(motionEvent: MotionEvent?) {
        addNewTouchEvent(motionEvent!!)
    }

    // Call this each time there is a new event.
    fun addNewTouchEvent(event: MotionEvent) {

    }


}