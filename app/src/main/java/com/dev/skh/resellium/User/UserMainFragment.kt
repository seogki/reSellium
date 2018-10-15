package com.dev.skh.resellium.User


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.FragmentUserMainBinding
import java.lang.ref.WeakReference


class UserMainFragment : BaseFragment(), UserMainPresenter.View {

    companion object {
        fun weakRef(view: UserMainPresenter.View): WeakReference<UserMainPresenter> {
            return WeakReference(UserMainPresenter(view))
        }
    }

    private val weakReference by lazy { weakRef(this) }
    private lateinit var binding: FragmentUserMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_main, container, false)
        binding.layoutAppbar?.title = "설정"
        return binding.root
    }


}
