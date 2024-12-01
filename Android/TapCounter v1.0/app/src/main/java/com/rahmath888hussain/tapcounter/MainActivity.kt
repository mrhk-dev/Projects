package com.rahmath888hussain.tapcounter

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var dbRef: TapCounterDataBase
    private lateinit var modelClass:ModelClassCount

    private lateinit var tvCounterValue:TextView
    private lateinit var btnCounterValueIncrease:Button
    private lateinit var btnCounterValueDecrease:Button
    private lateinit var btnCounterValueReset:Button


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

    }

    override fun onResume() {
        super.onResume()

        checkAvailableCounter()
        updateActivities()

        btnCounterValueIncrease.setOnClickListener {
            modelClass.value = (modelClass.value ?: 0) + 1
            Log.d("CounterValue", "onResume: +   ${modelClass.value}    $modelClass")
            updateCounterDisplay()
        }

        btnCounterValueDecrease.setOnClickListener{
            if ((modelClass.value ?: 0) > 0) {
                modelClass.value = (modelClass.value ?: 0) - 1
            }

            Log.d("CounterValue", "onResume: -   ${modelClass.value}   $modelClass")
            updateCounterDisplay()
        }

        btnCounterValueReset.setOnClickListener{
            if ((modelClass.value ?: 0) > 0) {
                modelClass.value = 0
            }
            updateCounterDisplay()
        }
    }

    private fun updateCounterDisplay() {
        tvCounterValue.text = "${modelClass.value}"
        dbRef.updateCounter(modelClass.id , modelClass.value)
    }

    private fun updateActivities() {
        tvCounterValue.text = "${modelClass.value}"
    }

    private fun init(){
        tvCounterValue = findViewById(R.id.tv_CountValue)
        btnCounterValueIncrease = findViewById(R.id.btn_main_increase)
        btnCounterValueDecrease = findViewById(R.id.btn_main_decrease)
        btnCounterValueReset = findViewById(R.id.btn_main_reset)

        dbRef = TapCounterDataBase(this)
    }

    private fun checkAvailableCounter() {
        val countersAvailableCount = dbRef.getACountersCount()

        if (countersAvailableCount > 0)
            modelClass = dbRef.getDefaultModelClass(countersAvailableCount)
        else {
            modelClass = ModelClassCount()
            dbRef.insertCounter(modelClass)
        }
    }
}