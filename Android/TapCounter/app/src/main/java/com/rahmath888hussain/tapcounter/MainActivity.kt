package com.rahmath888hussain.tapcounter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility

class MainActivity : AppCompatActivity() {
    //    constants
    private val counterValueName = "counterValue"
    private val counterTitle = "Tap Counter"
    private val selectedTheme = "SELECTED_THEME"

    //    widgets declaration
    private lateinit var addBt: Button
    private lateinit var resetBtn: Button
    private lateinit var counterTextView: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var titleTextView: TextView
    private lateinit var editTextTitle: EditText
    private lateinit var setTitle: ImageView
    private lateinit var menu: Menu

    //    activity level variables
    private var counterValue: Int = 0
    private lateinit var prefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        init()
        prefs.getString(selectedTheme, "System Default")?.let { setAppTheme(it) }

        titleTextView.text = prefs.getString(counterTitle, defaultValue = "Tap Counter")
        counterValue = prefs.getInt(counterValueName)
        counterTextView.text = "$counterValue"

        addBt.setOnClickListener {
            counterValue = ++counterValue
            prefs.saveInt(counterValueName, counterValue)
//            counterTextView.text = counterValue.toString()

            flipAnimation {
                counterTextView.text = "$counterValue"
            }

        }

        resetBtn.setOnClickListener {
//            animateCounter(counterValue, 0)
//            counterValue = 0
//            prefs.saveInt(COUNTER_VALUE_NAME, counterValue)

            if (counterValue != 0) {
                flipAnimation {
                    counterValue = 0
                    prefs.saveInt(counterValueName, counterValue)
                    counterTextView.text = "$counterValue"
                }
            }
        }

        setTitle.setOnClickListener {
            if (editTextTitle.text.toString().isNotEmpty() || editTextTitle.text.toString()
                    .isNotBlank()
            ) {
                prefs.saveString(counterTitle, editTextTitle.text.toString())
                titleTextView.text = editTextTitle.text.toString()
                editTextTitle.setText("")
            }
            editTextTitle.visibility = View.GONE
            titleTextView.visibility = View.VISIBLE
            setTitle.visibility = View.GONE
            menu.findItem(R.id.editTitle).setVisible(true)
        }
    }

    private fun init() {
        prefs = SharedPrefs.getInstance(this)

        addBt = findViewById(R.id.main_btn_increment)
        resetBtn = findViewById(R.id.main_btn_reset)
        counterTextView = findViewById(R.id.tv_counterValue)
        titleTextView = findViewById(R.id.tv_taskbarTitle)
        editTextTitle = findViewById(R.id.et_taskbarTitle)
        setTitle = findViewById(R.id.img_setTitle)
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
                counterTextView.text = "$counterValue"
                flipX.removeUpdateListener {
//                    it == animation
                }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        if (menu != null) {
            this.menu = menu
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (prefs.getString(selectedTheme) != "Dark") {
            item.icon?.setTint(Color.WHITE)
        } else {
            item.icon?.setTint(Color.BLACK)
        }

        return when (item.itemId) {
            R.id.settings -> {
                val settingsBottomSheet = SettingsFragment()
                settingsBottomSheet.show(supportFragmentManager, settingsBottomSheet.tag)
                true
            }

            R.id.editTitle -> {
                titleTextView.visibility = View.GONE
                editTextTitle.visibility = View.VISIBLE
                item.isVisible = false
                setTitle.visibility = View.VISIBLE
                true
            }

            R.id.menu_showCounters -> {
                val intent = Intent(this, CountersListActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setAppTheme(item: String) {
        val nightMode = when (item) {
            "System Default" -> {
                val isNightModeOn = AppCompatDelegate.getDefaultNightMode()
                Toast.makeText(this, "System Default Theme Int: $isNightModeOn", Toast.LENGTH_SHORT)
                    .show()
                if (isNightModeOn == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            }

            "Light" -> AppCompatDelegate.MODE_NIGHT_NO
            "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> return
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}