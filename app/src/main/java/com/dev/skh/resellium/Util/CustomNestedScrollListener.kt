package com.dev.skh.resellium.Util

import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by Seogki on 2018. 11. 9..
 */
class CustomNestedScrollListener(layoutManager: RecyclerView.LayoutManager?, val listener: OnScrollListener?) : NestedScrollView.OnScrollChangeListener {

    private var linearLayoutManager: LinearLayoutManager? = null
    private var gridLayoutManager: GridLayoutManager? = null

    init {
        when (layoutManager) {
            is LinearLayoutManager -> this.linearLayoutManager = layoutManager
            is GridLayoutManager -> this.gridLayoutManager = layoutManager
        }
    }

    override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        if (v.getChildAt(v.childCount - 1) != null) {
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                val visibleItemCount = linearLayoutManager!!.childCount
                val totalItemCount = linearLayoutManager!!.itemCount
                val pastVisiblesItems = linearLayoutManager!!.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                    listener?.onScrollEnd()
            }
        }
    }


    interface OnScrollListener {
        fun onScrollEnd()
    }
}

