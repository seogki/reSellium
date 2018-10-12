package com.dev.skh.resellium.Main.tab.Worst


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.FragmentHomeMainWorstBinding

class HomeMainWorstFragment : Fragment() {

    private lateinit var binding: FragmentHomeMainWorstBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main_worst, container, false)


        return binding.root
    }


}
