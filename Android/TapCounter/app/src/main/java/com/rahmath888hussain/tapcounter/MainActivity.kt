package com.rahmath888hussain.tapcounter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    //    constants
    private val COUNTER_VALUE_NAME = "counterValue"

    //    widgets declaration
    private lateinit var prefs: SharedPrefs
    private lateinit var addBt: Button
    private lateinit var resetBtn: Button
    private lateinit var counterTextView: TextView

    //    activity level variables
    var counter_value: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        counter_value = prefs.getInt(COUNTER_VALUE_NAME)
        counterTextView.text = counter_value.toString()

        addBt.setOnClickListener {
            counter_value = ++counter_value
            prefs.saveInt(COUNTER_VALUE_NAME, counter_value)
//            counterTextView.text = counter_value.toString()

            flipAnimation {
                counterTextView.text = counter_value.toString()
            }

        }

        resetBtn.setOnClickListener {
//            animateCounter(counter_value, 0)
//            counter_value = 0
//            prefs.saveInt(COUNTER_VALUE_NAME, counter_value)

            flipAnimation {
                counter_value = 0
                prefs.saveInt(COUNTER_VALUE_NAME, counter_value)
                counterTextView.text = counter_value.toString()
            }

        }
    }

    fun init() {
        prefs = SharedPrefs.getInstance(this)

        addBt = findViewById(R.id.main_btn_increment)
        resetBtn = findViewById(R.id.main_btn_reset)
        counterTextView = findViewById(R.id.tv_counterValue)
    }

    private fun flipAnimation(onAnimationEnd: () -> Unit) {
        val animatorSet = AnimatorSet()
        val flipX = ObjectAnimator.ofFloat(counterTextView, "rotationX", 0f, 90f)
        flipX.duration = 100

        val flipXBack = ObjectAnimator.ofFloat(counterTextView, "rotationX", 90f, 0f)
        flipXBack.duration = 100

        flipX.addUpdateListener { animation ->
            val rotation = animation.animatedValue as Float
            if (rotation >= 90f) {
                val currentValue = counterTextView.text.toString().toIntOrNull() ?: 0
                counterTextView.text = (currentValue + 1).toString()
                flipX.removeUpdateListener { it == animation }
            }
        }

        animatorSet.playSequentially(flipX, flipXBack)

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onAnimationEnd()
            }
        })

        animatorSet.start()
    }

}