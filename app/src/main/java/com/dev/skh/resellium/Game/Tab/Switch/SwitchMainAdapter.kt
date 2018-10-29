package com.dev.skh.resellium.Game.Tab.Switch

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.Network.ApiCilentRx
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ItemSwitchBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 8. 20..
 */
class SwitchMainAdapter(context: Context, arraylist: MutableList<GameMainModel>) : BaseRecyclerViewAdapter<GameMainModel, SwitchMainAdapter.EstimateRegisterViewHolder>(context, arraylist) {

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


    inner class EstimateRegisterViewHolder(val binding: ItemSwitchBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {


        fun bind(model: GameMainModel?) {
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

        var disposable: Disposable? = null
        val client by lazy { ApiCilentRx.create() }

        private fun setReport() {
            val item = getItem(adapterPosition)

            disposable = client.setReport(item?.platform!!, item.id!!, item.title!!,item.date!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        Toast.makeText(context!!, "신고처리가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }) {
                        Toast.makeText(context!!, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                    }
        }


    }
}