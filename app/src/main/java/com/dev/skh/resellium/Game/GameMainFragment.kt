package com.dev.skh.resellium.Game


import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Game.Tab.Ps4.Ps4MainFragment
import com.dev.skh.resellium.Game.Tab.Search.SearchMainActivity
import com.dev.skh.resellium.Game.Tab.Switch.SwitchMainFragment
import com.dev.skh.resellium.Game.Tab.Xbox.XboxMainFragment
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.TabPagerAdapter
import com.dev.skh.resellium.databinding.FragmentGameMainBinding








class GameMainFragment : BaseFragment(), View.OnClickListener {


    private lateinit var binding: FragmentGameMainBinding
    private lateinit var adapter: TabPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_main, container, false)
        binding.layoutAppbar?.title = "게임"
        binding.layoutAppbar?.onClickListener = this
        setView()
        return binding.root
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.img_search -> {
                startActivity(Intent(context, SearchMainActivity::class.java))
            }
        }
    }


    private fun setView() {
        viewPager = binding.registerFragViewpager
        adapter = TabPagerAdapter(childFragmentManager)
        viewPager.offscreenPageLimit = 3
        adapter.addFragment(Ps4MainFragment(), "PS")
        adapter.addFragment(XboxMainFragment(), "XBOX")
        adapter.addFragment(SwitchMainFragment(), "SWITCH")
        tabLayout = binding.layoutAppbar!!.registerFragTablayout

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

}
