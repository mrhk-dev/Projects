package com.rahmath888hussain.tapcounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CountersListActivity : AppCompatActivity() {
    private lateinit var dbHelper: MyAppDatabase
    private lateinit var counterRecView:RecyclerView
    private lateinit var counterAdapter: CounterRecViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_counters_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = MyAppDatabase(this)
        counterRecView = findViewById(R.id.recView_Counters)
        counterRecView.layoutManager = LinearLayoutManager(this)
        counterAdapter = CounterRecViewAdapter()

        val counters = dbHelper.getAllCounters()

        counterRecView.adapter = counterAdapter
        counterAdapter.setCounterList(counters)
    }

    class CounterRecViewAdapter() : RecyclerView.Adapter<CounterRecViewAdapter.ViewAdapter>() {
        private val counterList = mutableListOf<CountModelClass>()


        fun setCounterList(list: List<CountModelClass>) {
            counterList.clear()
            counterList.addAll(list)
            notifyDataSetChanged()
        }

        class ViewAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val counterNameTextView = itemView.findViewById<TextView>(R.id.CounterName)
            private val counterValueTextView = itemView.findViewById<TextView>(R.id.CounterValue)

            fun bind(counterModel: CountModelClass) {
                counterNameTextView.text = counterModel.counterName
                counterValueTextView.text = counterModel.counterValue.toString()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdapter {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.counter_list_row,
            parent, false)
            return ViewAdapter(view)
        }

        override fun onBindViewHolder(holder: ViewAdapter, position: Int) {
            holder.bind(counterList[position])
        }

        override fun getItemCount(): Int {
            return counterList.size
        }
    }

    class CountModelClass(
        var counterId: Int = 0,
        var counterName: String = "TapCounter",
        var counterValue: Int = 0,
        var setDefault:Boolean = false
    )
}