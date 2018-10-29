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
import com.dev.skh.resellium.Game.Search.SearchMainActivity
import com.dev.skh.resellium.Game.Tab.Ps4.Ps4MainFragment
import com.dev.skh.resellium.Game.Tab.Switch.SwitchMainFragment
import com.dev.skh.resellium.Game.Tab.Xbox.XboxMainFragment
import com.dev.skh.resellium.R
import com.dev.skh.resellium.User.UserMainActivity
import com.dev.skh.resellium.Util.TabPagerAdapter
import com.dev.skh.resellium.databinding.FragmentGameMainBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

class GameMainFragment : BaseFragment(), View.OnClickListener {


    private lateinit var binding: FragmentGameMainBinding
    private lateinit var adapter: TabPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var noAds = false
    private var adView: InterstitialAd? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_main, container, false)
        initAds()
        binding.layoutAppbar?.title = "게임"
        binding.layoutAppbar?.onClickListener = this
        setView()
        return binding.root
    }

    private fun initAds() {
        adView = InterstitialAd(context!!)
//        val test = "ca-app-pub-3940256099942544/1033173712"
        val mine = "ca-app-pub-2973552036061869/9167567607"
        adView?.adUnitId = mine
        reloadAd()
        adView?.adListener = object : AdListener() {

            override fun onAdLoaded() {
                noAds = false
            }

            override fun onAdClosed() {
                reloadAd()
                onSearchMainIntent()
            }

            override fun onAdFailedToLoad(p0: Int) {
                noAds = true
            }
        }
    }

    private fun reloadAd() {
        val request = AdRequest
                .Builder()
                .addTestDevice("A86E700BF47A5B43A7D1B1882060F2AA")
                .addTestDevice("945C9CAA6FF2EC9D7AE09BE4244D1081")
                .build()
        this.adView?.loadAd(request)
    }

    override fun onResume() {
        super.onResume()
        reloadAd()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_search -> letsSeeAds()
            R.id.img_setting -> startActivity(Intent(context!!, UserMainActivity::class.java))

        }
    }

    private fun onSearchMainIntent() {
        startActivity(Intent(context, SearchMainActivity::class.java))
    }

    private fun letsSeeAds() {
        if (noAds) {
            onSearchMainIntent()
        } else {
            adView?.show()
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
