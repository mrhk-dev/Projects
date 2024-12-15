package com.rahmath888hussain.zomatoexpensetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rahmath888hussain.zomatoexpensetracker.databinding.OrdersListBinding


class DatesListRecViewAdapter(
    private var dataModelList: List<DatesModelClass>,
    private val listener: CustomOnItemClickListener
) : RecyclerView.Adapter<DatesListRecViewAdapter.ViewAdapter>() {

    inner class ViewAdapter(private val binding: OrdersListBinding) :
        ViewHolder(binding.root), View.OnClickListener {
        fun bind(dataModel: DatesModelClass) {
            binding.model = dataModel
            binding.executePendingBindings()
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener {
                listener.onLongItemClick(it, adapterPosition)
                true
            }
        }

        override fun onClick(p0: View?) {
            if (p0 != null) {
                listener.onItemClick(p0, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewAdapter {
        val binding: OrdersListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.orders_list,
            parent,
            false
        )
        return ViewAdapter(binding)
    }

    override fun onBindViewHolder(holder: ViewAdapter, position: Int) {
        val dataModel = dataModelList[position]
        holder.bind(dataModel)
    }

    override fun getItemCount(): Int {
        return dataModelList.size
    }

    interface CustomOnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onLongItemClick(view: View, position: Int)
    }

    fun updateList(dataList:List<DatesModelClass>){
        dataModelList = dataList
        notifyDataSetChanged()
    }
}