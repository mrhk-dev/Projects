package com.rahmath888hussain.tapcounter

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
    private lateinit var addBt:Button
    private lateinit var resetBtn:Button
    private lateinit var counterTextView:TextView

//    activity level variables
    var counter_value:Int = 0

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
            counterTextView.text = counter_value.toString()
        }

        resetBtn.setOnClickListener {
            counter_value = 0
            prefs.saveInt(COUNTER_VALUE_NAME, counter_value)
            counterTextView.text = counter_value.toString()
        }
    }

    fun init(){
        prefs = SharedPrefs.getInstance(this)

        addBt = findViewById(R.id.main_btn_increment)
        resetBtn = findViewById(R.id.main_btn_reset)
        counterTextView = findViewById(R.id.tv_counterValue)
    }
}