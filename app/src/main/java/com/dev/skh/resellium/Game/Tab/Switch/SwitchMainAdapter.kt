package com.dev.skh.resellium.Game.Tab.Switch

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Game.Model.SwitchMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ItemSwitchBinding

/**
 * Created by Seogki on 2018. 8. 20..
 */
class SwitchMainAdapter(context: Context, arraylist: MutableList<SwitchMainModel>) : BaseRecyclerViewAdapter<SwitchMainModel, SwitchMainAdapter.EstimateRegisterViewHolder>(context, arraylist) {

    private var arr = arraylist
    override fun onBindView(holder: EstimateRegisterViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        holder.bind(getItem(holder.adapterPosition))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSwitchBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_switch, parent, false)
        return EstimateRegisterViewHolder(binding)
    }

//    override fun getItemId(position: Int): Long {
//        val id = arr[position]
//        return id.date!!.hashCode().toLong()
//    }


    inner class EstimateRegisterViewHolder(val binding: ItemSwitchBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {



        fun bind(model: SwitchMainModel?) {
            binding.model = model
        }

        override fun onClick(v: View?) {

        }


    }
}