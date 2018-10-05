package com.dev.skh.resellium.Board


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.FragmentBoardMainBinding


class BoardMainFragment : Fragment() {
    private lateinit var binding: FragmentBoardMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_board_main, container, false)
        binding.layoutAppbar?.title = "게시판"
        return binding.root
    }


}
