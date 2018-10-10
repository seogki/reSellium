package com.dev.skh.resellium.Main


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Game.Tab.Ps4.HomeMainHoriAdapter
import com.dev.skh.resellium.Main.Model.HoriModel
import com.dev.skh.resellium.Main.Model.PopularModel
import com.dev.skh.resellium.Main.Popular.HomeMainPopularGameAdapter
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentHomeMainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class HomeMainFragment : BaseFragment(), HomeMainPresenter.View {


    override fun errorUpdateData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
    }


    private lateinit var binding: FragmentHomeMainBinding
    private lateinit var ps4LayoutManager: LinearLayoutManager
    private lateinit var xboxLayoutManager: LinearLayoutManager
    private lateinit var switchLayoutManager: LinearLayoutManager
    private lateinit var popularLayoutManager: LinearLayoutManager
    private var homeMainPopularGameAdapter: HomeMainPopularGameAdapter? = null
    private var weakPresenter: WeakReference<HomeMainPresenter>? = null
    private var homeMainPs4HoriAdapter: HomeMainHoriAdapter? = null
    private var homeMainXboxHoriAdapter: HomeMainHoriAdapter? = null
    private var homeMainSwitchHoriAdapter: HomeMainHoriAdapter? = null
    private var popRv: RecyclerView? = null
    private var ps4Rv: RecyclerView? = null
    private var xboxRv: RecyclerView? = null
    private var switchRv: RecyclerView? = null
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main, container, false)
        binding.layoutAppbar?.title = "메인"
        setMVP()
        setRv()
        return binding.root
    }

    private fun setMVP() {
        weakPresenter = WeakReference(HomeMainPresenter(this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Handler().postDelayed({
            popRv?.adapter = homeMainPopularGameAdapter
            ps4Rv?.adapter = homeMainPs4HoriAdapter
            xboxRv?.adapter = homeMainXboxHoriAdapter
            switchRv?.adapter = homeMainSwitchHoriAdapter
            weakPresenter?.get()?.getPopularData()
            weakPresenter?.get()?.getHoriData("PS")
            weakPresenter?.get()?.getHoriData("XBOX")
            weakPresenter?.get()?.getHoriData("SWITCH")
        }, 10)
    }

    private fun setRv() {
        val decor = DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL)
        decor.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.survey_hori_divder)!!)

        homeMainPopularGameAdapter = HomeMainPopularGameAdapter(context!!, mutableListOf())
        popularLayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        popRv = setHorizontalRv(binding.rvRecentPs4, popularLayoutManager)
        popRv?.addItemDecoration(decor)


        homeMainPs4HoriAdapter = HomeMainHoriAdapter(context!!, mutableListOf())
        ps4LayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        ps4Rv = setHorizontalRv(binding.rvPs4, ps4LayoutManager)

        homeMainXboxHoriAdapter = HomeMainHoriAdapter(context!!, mutableListOf())
        xboxLayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        xboxRv = setHorizontalRv(binding.rvXbox, xboxLayoutManager)

        homeMainSwitchHoriAdapter = HomeMainHoriAdapter(context!!, mutableListOf())
        switchLayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        switchRv = setHorizontalRv(binding.rvSwitch, switchLayoutManager)

    }

    override fun getPopularData(it: MutableList<PopularModel>?, disposable: Disposable?) {
        if (it != null)
            homeMainPopularGameAdapter?.addItems(it)
    }

    override fun updateData(result: MutableList<HoriModel>?, disposable: Disposable?, currentPos: String) {
        if (result != null) {
            when (currentPos) {
                "PS" -> setPs4Data(result)
                "XBOX" -> setXboxData(result)
                "SWITCH" -> setSwitchData(result)
            }
        }
        this.disposable = disposable
    }

    private fun setPs4Data(horiModel: MutableList<HoriModel>?) {
        homeMainPs4HoriAdapter?.addItems(horiModel!!)
    }

    private fun setXboxData(horiModel: MutableList<HoriModel>?) {
        homeMainXboxHoriAdapter?.addItems(horiModel!!)
    }

    private fun setSwitchData(horiModel: MutableList<HoriModel>?) {
        homeMainSwitchHoriAdapter?.addItems(horiModel!!)
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
