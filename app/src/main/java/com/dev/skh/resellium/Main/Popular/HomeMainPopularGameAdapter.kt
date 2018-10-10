package com.dev.skh.resellium.Main.Popular

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Main.Model.PopularModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ItemPopularBinding

/**
 * Created by Seogki on 2018. 10. 10..
 */
class HomeMainPopularGameAdapter(context: Context, arraylist: MutableList<PopularModel>) : BaseRecyclerViewAdapter<PopularModel, HomeMainPopularGameAdapter.PlusFreeViewHolder>(context, arraylist) {

    override fun onBindView(holder: PlusFreeViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        holder.bind(getItem(holder.adapterPosition))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPopularBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_popular, parent, false)
        return PlusFreeViewHolder(binding)
    }


    inner class PlusFreeViewHolder(val binding: ItemPopularBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {


        fun bind(model: PopularModel?) {
            binding.model = model
        }

        override fun onClick(v: View?) {

        }


    }
}