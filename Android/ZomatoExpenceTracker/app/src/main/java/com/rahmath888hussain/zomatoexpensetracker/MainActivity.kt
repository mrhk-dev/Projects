package com.rahmath888hussain.zomatoexpensetracker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmath888hussain.zomatoexpensetracker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), DatesListRecViewAdapter.CustomOnItemClickListener
{
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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

        dataModels.add(DatesModelClass("22-13-24", "100"))
        dataModels.add(DatesModelClass("22-13-24", "100"))
        dataModels.add(DatesModelClass("22-13-24", "100"))
        dataModels.add(DatesModelClass("22-13-24", "100"))

        binding.notifyChange()

    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "${position} clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onLongItemClick(view: View, position: Int) {
    }


}