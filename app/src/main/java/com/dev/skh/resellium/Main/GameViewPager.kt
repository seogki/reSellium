package com.dev.skh.resellium.Main

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.LayoutMainSliderBinding



/**
 * Created by Seogki on 2018. 10. 1..
 */


class GameViewPager : Fragment() {


    private lateinit var binding: LayoutMainSliderBinding
    private var data : Int? = null
    fun getDataFromPresenter(i: Int) {
        this.data = i
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.layout_main_slider, container, false)
        binding.imgTitle.setImageResource(data!!)
        binding.imgTitle.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
        return binding.root
    }
}