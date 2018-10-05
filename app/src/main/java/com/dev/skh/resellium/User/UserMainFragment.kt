package com.dev.skh.resellium.User


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.FragmentUserMainBinding


class UserMainFragment : Fragment() {

    private lateinit var binding: FragmentUserMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_main, container, false)
        binding.layoutAppbar?.title = "유저"
        return binding.root
    }


}
