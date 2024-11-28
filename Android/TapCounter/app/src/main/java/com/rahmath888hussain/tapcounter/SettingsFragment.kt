package com.rahmath888hussain.tapcounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsFragment : BottomSheetDialogFragment() {
    private lateinit var prefs: SharedPrefs
    private val selectedTheme = "SELECTED_THEME"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = SharedPrefs.getInstance(view.context)

        view.findViewById<TextView>(R.id.textViewTitle).setOnClickListener {
            dismiss()
        }

        val spinnerItems = listOf("System Default", "Light", "Dark")
        val spinner = view.findViewById<Spinner>(R.id.spinner_settings_theme)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.themeOptionsArray)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)

        val setThemeName = prefs.getString(selectedTheme, "System Default")
        var themeValue = 0
        if (setThemeName == "System Default"){
            themeValue = 0
        }else if (setThemeName == "Light"){
            themeValue = 1
        }else{
            themeValue = 2
        }

        spinner.setSelection(themeValue)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position)
                setAppTheme(selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    fun setAppTheme(item: String) {
        val theme = when (item) {
            "System Default" -> {
                val systemHasDarkMode = AppCompatDelegate.getDefaultNightMode()
                if (systemHasDarkMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    "Dark"
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    "Light"
                }
            }
            "Light" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "Light"
            }
            "Dark" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "Dark"
            }
            else -> return
        }

        prefs.saveString(selectedTheme, theme)
    }
}