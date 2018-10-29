package com.dev.skh.resellium.Game.Inner

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Game.Model.GameCommentModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ItemCommentBinding

/**
 * Created by Seogki on 2018. 10. 29..
 */
class GameMainCommentAdapter(context: Context, arraylist: MutableList<GameCommentModel>) : BaseRecyclerViewAdapter<GameCommentModel, GameMainCommentAdapter.EstimateRegisterViewHolder>(context, arraylist) {

    private var arr = arraylist
    override fun onBindView(holder: EstimateRegisterViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        holder.bind(getItem(holder.adapterPosition))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCommentBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_comment, parent, false)
        return EstimateRegisterViewHolder(binding)
    }

    inner class EstimateRegisterViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {

        }


        fun bind(model: GameCommentModel?) {
            binding.onClickListener = this
            binding.model = model
        }


    }
}