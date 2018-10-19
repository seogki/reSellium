package com.dev.skh.resellium.Board

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ItemBoardMainBinding

/**
 * Created by Seogki on 2018. 10. 11..
 */
class BoardMainAdapter(context: Context, arraylist: MutableList<BoardMainModel>) : BaseRecyclerViewAdapter<BoardMainModel, BoardMainAdapter.BoardMainHolder>(context, arraylist) {

    private var arr = arraylist
    override fun onBindView(holder: BoardMainHolder, position: Int) {
        holder.setIsRecyclable(true)
        holder.bind(getItem(holder.adapterPosition))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemBoardMainBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_board_main, parent, false)
        return BoardMainHolder(binding)
    }

    inner class BoardMainHolder(val binding: ItemBoardMainBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(model: BoardMainModel?) {
            binding.onClickListener = this
            binding.model = model
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.img_other -> {
                    popupMenu(v)
                }
            }
        }

        private fun popupMenu(v: View) {
            val popupMenu = PopupMenu(context!!, v)
            popupMenu.inflate(R.menu.game_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.menu_report -> {
                        setReport()
                        true
                    }
                    else -> {

                        false
                    }
                }
            }
            popupMenu.show()
        }

        private fun setReport() {
            Toast.makeText(context!!, "신고버튼", Toast.LENGTH_SHORT).show()
        }


    }
}