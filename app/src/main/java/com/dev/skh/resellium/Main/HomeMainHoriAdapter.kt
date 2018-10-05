package com.dev.skh.resellium.Game.Tab.Ps4

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Main.Model.HoriModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ItemRvHorizontalBinding


/**
 * Created by Seogki on 2018. 8. 20..
 */
class HomeMainHoriAdapter(context: Context, arraylist: MutableList<HoriModel>) : BaseRecyclerViewAdapter<HoriModel, HomeMainHoriAdapter.PlusFreeViewHolder>(context, arraylist) {

    private var arr = arraylist
    override fun onBindView(holder: PlusFreeViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        holder.bind(getItem(holder.adapterPosition))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemRvHorizontalBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_rv_horizontal, parent, false)
        return PlusFreeViewHolder(binding)
    }


    inner class PlusFreeViewHolder(val binding: ItemRvHorizontalBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {



        fun bind(model: HoriModel?) {
            binding.model = model
        }

        override fun onClick(v: View?) {

        }


    }
}