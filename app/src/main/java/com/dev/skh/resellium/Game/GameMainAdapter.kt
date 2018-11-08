package com.dev.skh.resellium.Game

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ItemGameBinding

/**
 * Created by Seogki on 2018. 8. 20..
 */
class GameMainAdapter(context: Context, arraylist: MutableList<GameMainModel>) : BaseRecyclerViewAdapter<GameMainModel, GameMainAdapter.GameMainViewHolder>(context, arraylist) {

    override fun onBindView(holder: GameMainViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        holder.bind(getItem(holder.adapterPosition))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemGameBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_game, parent, false)
        return GameMainViewHolder(binding)
    }

    inner class GameMainViewHolder(val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: GameMainModel?) {
            binding.model = model
        }
    }
}