package com.rahmath888hussain.zomatoexpensetracker

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmath888hussain.zomatoexpensetracker.databinding.ActivityMainBinding
import com.rahmath888hussain.zomatoexpensetracker.databinding.NewdataBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity(), DatesListRecViewAdapter.CustomOnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHandler: DataBaseHelper
    private lateinit var newDataBinding: NewdataBinding
    lateinit var model: DatesModelClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dbHandler = DataBaseHelper(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val dataModels: MutableList<DatesModelClass> = mutableListOf() // Populate your list here

        // Set up RecyclerView with adapter
        val adapter = DatesListRecViewAdapter(dataModels, this)
        binding.DatesListRecView.layoutManager = LinearLayoutManager(this)
        binding.DatesListRecView.adapter = adapter

        adapter.updateList(dbHandler.getAllData().toMutableList())
        binding.notifyChange()

        binding.btnAddData.setOnClickListener {
            showDialog()
        }
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "${position} clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onLongItemClick(view: View, position: Int) {

    }

    fun showDialog() {
        val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        model = DatesModelClass()
        model.date = formattedDate

        model = dbHandler.getData(model.date)!!

        val dialogView = LayoutInflater.from(this).inflate(R.layout.newdata, null)

        newDataBinding = DataBindingUtil.bind(dialogView)!!
        newDataBinding.model = model

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        alertDialog.setOnShowListener {
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        newDataBinding.dialogBtnAdd.setOnClickListener {
            if (model.tripCollected == "" || model.tripCollected.isBlank())
                model.tripCollected = "0"

            if (model.cashCollected == "" || model.cashCollected.isBlank())
                model.cashCollected = "0"

            if (model.expenses == "" || model.expenses.isBlank())
                model.expenses = "0"

            if (model.petrol == "" || model.petrol.isBlank())
                model.petrol = "0"

            if (model.distanceTraveled == "" || model.distanceTraveled.isBlank())
                model.distanceTraveled = "0"

            model.profitLoss = (model.tripCollected.toInt()
                    + model.cashCollected.toInt() - model.petrol.toInt()
                    - model.expenses.toInt()).toString()
            dbHandler.addData(model)
            alertDialog.dismiss()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                updateValue()
            }
        }

        newDataBinding.dialogEtPetrol.addTextChangedListener(textWatcher)

        newDataBinding.dialogEtExpenses.addTextChangedListener(textWatcher)

        newDataBinding.dialogEtCashCollection.addTextChangedListener(textWatcher)

        newDataBinding.dialogEtTripCollection.addTextChangedListener(textWatcher)


        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var selectedDate = ""

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth-${month + 1}-$year"
                Toast.makeText(this, "Selected date: $selectedDate", Toast.LENGTH_SHORT).show()

                if (selectedDate != model.date) {
                    val newModel = DatesModelClass().apply {
                        date = selectedDate
                    }
                    newDataBinding.model = newModel
                    newDataBinding.notifyChange()

                    model = newModel
                }

            },
            year,
            month,
            day
        )

        newDataBinding.dialogEditDateLayout.setOnClickListener {
            datePickerDialog.show()
        }

        alertDialog.show()
    }

    fun updateValue() {
        val tripCollected = model.tripCollected.toIntOrNull() ?: 0
        val cashCollected = model.cashCollected.toIntOrNull() ?: 0
        val petrol = model.petrol.toIntOrNull() ?: 0
        val expenses = model.expenses.toIntOrNull() ?: 0
        model.profitLoss = (tripCollected + cashCollected - petrol - expenses).toString()
        newDataBinding.notifyChange()
    }


}