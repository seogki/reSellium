package com.dev.skh.resellium.Main


import android.annotation.SuppressLint
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.Main.tab.Best.HomeMainBestFragment
import com.dev.skh.resellium.Main.tab.New.HomeMainNewFragment
import com.dev.skh.resellium.Main.tab.Worst.HomeMainWorstFragment
import com.dev.skh.resellium.R
import com.dev.skh.resellium.User.UserMainActivity
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.Util.TabPagerAdapter
import com.dev.skh.resellium.Util.UtilMethod
import com.dev.skh.resellium.databinding.FragmentHomeMainBinding
import com.dev.skh.resellium.databinding.LayoutRecentBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class HomeMainFragment : BaseFragment(), HomeMainPresenter.View, View.OnClickListener {

    override fun errorUpdateData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
        defaultSetting()
        showErrorToast()
    }

    companion object {
        fun weakRef(view: HomeMainPresenter.View): WeakReference<HomeMainPresenter> {
            return WeakReference(HomeMainPresenter(view))
        }
    }


    private lateinit var binding: FragmentHomeMainBinding
    private val weakPresenter by lazy { weakRef(this) }
    private var disposable: Disposable? = null
    private var rfBtn: Boolean = false
    private lateinit var adapter: TabPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var psModels: MutableList<GameMainModel>? = null
    private var xboxModels: MutableList<GameMainModel>? = null
    private var switchModels: MutableList<GameMainModel>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main, container, false)
        binding.fragment = this
        invisibleView()
        setView()
        setTabLayout()
        setRv()
        return binding.root
    }

    private fun setView() {
        binding.layoutAppbar?.title = "겜창고"
        binding.layoutAppbar?.onClickListener = this
        binding.layoutPs?.fragment = this
        binding.layoutXbox?.fragment = this
        binding.layoutSwitch?.fragment = this
        binding.layoutPs?.text = "PS"
        binding.layoutXbox?.text = "XBOX"
        binding.layoutSwitch?.text = "SWITCH"

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_setting -> startActivity(Intent(context!!, UserMainActivity::class.java))
        }
    }

    private fun setRv() {
        binding.txtPs4.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.txtSwitch.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.txtXbox.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)

    }

    fun onClickLayout(view: View?, text: String) {
        when (text) {
            "PS" -> {
                when (view?.id) {
                    R.id.const_first -> setInnerIntent(psModels?.let { it[0] }, view)
                    R.id.const_second -> setInnerIntent(psModels?.let { it[1] }, view)
                    R.id.const_third -> setInnerIntent(psModels?.let { it[2] }, view)
                }
            }
            "XBOX" -> {
                when (view?.id) {
                    R.id.const_first -> setInnerIntent(xboxModels?.let { it[0] }, view)
                    R.id.const_second -> setInnerIntent(xboxModels?.let { it[1] }, view)
                    R.id.const_third -> setInnerIntent(xboxModels?.let { it[2] }, view)
                }
            }
            "SWITCH" -> {
                when (view?.id) {
                    R.id.const_first -> setInnerIntent(switchModels?.let { it[0] }, view)
                    R.id.const_second -> setInnerIntent(switchModels?.let { it[1] }, view)
                    R.id.const_third -> setInnerIntent(switchModels?.let { it[2] }, view)
                }
            }
        }
    }


//    private fun startAnimation() {
//        val rotate = RotateAnimation(
//                0f, 360f,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f
//        )
//        rotate.interpolator = LinearInterpolator()
//        rotate.duration = 500
//        rotate.repeatCount = Animation.INFINITE
//        binding.imgRefresh.startAnimation(rotate)
//    }
//
//    private fun stopAnimation() {
//        binding.imgRefresh.clearAnimation()
//    }

    private fun setTabLayout() {
        viewPager = binding.viewpager
        tabLayout = binding.tablayout

        adapter = TabPagerAdapter(childFragmentManager)
        viewPager.offscreenPageLimit = 3
        adapter.addFragment(HomeMainNewFragment(), "최신")
        adapter.addFragment(HomeMainBestFragment(), "최고")
        adapter.addFragment(HomeMainWorstFragment(), "최저")


        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }


    private fun getData() {
        weakPresenter.get()?.getHoriData("PS")
        weakPresenter.get()?.getHoriData("XBOX")
        weakPresenter.get()?.getHoriData("SWITCH")
    }


    override fun updateData(result: MutableList<GameMainModel>?, disposable: Disposable?, currentPos: String) {
        if (result != null) {
            when (currentPos) {
                "PS" -> {
                    this.psModels = result
                    setPs4Data(result)
                }
                "XBOX" -> {
                    this.xboxModels = result
                    setXboxData(result)
                }
                "SWITCH" -> {
                    this.switchModels = result
                    setSwitchData(result)
                }
            }
        }
        defaultSetting()
        this.disposable = disposable
    }

    private fun setPs4Data(horiModel: MutableList<GameMainModel>?) {
        horiModel?.let { it -> setRecentData(binding.layoutPs, it) }
    }

    private fun setXboxData(horiModel: MutableList<GameMainModel>?) {
        horiModel?.let { it -> setRecentData(binding.layoutXbox, it) }
    }

    private fun setSwitchData(horiModel: MutableList<GameMainModel>?) {
        horiModel?.let { it -> setRecentData(binding.layoutSwitch, it) }
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun invisibleView() {
        binding.constAll.visibility = View.INVISIBLE
    }

    private fun defaultSetting() {
        if (rfBtn) {
            rfBtn = false
        }
        binding.constAll.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun setRecentData(layout: LayoutRecentBinding?, horiModel: MutableList<GameMainModel>) {
        layout?.txtMoney1?.text = UtilMethod.currencyFormat(horiModel[0].money) + "원"
        layout?.txtMoney2?.text = UtilMethod.currencyFormat(horiModel[1].money) + "원"
        layout?.txtMoney3?.text = UtilMethod.currencyFormat(horiModel[2].money) + "원"

        layout?.txtTitle1?.text = horiModel[0].title
        layout?.txtTitle2?.text = horiModel[1].title
        layout?.txtTitle3?.text = horiModel[2].title

        layout?.txtWhich1?.text = horiModel[0].which
        layout?.txtWhich2?.text = horiModel[1].which
        layout?.txtWhich3?.text = horiModel[2].which

        setBg(horiModel[0].which, layout?.txtWhich1!!)
        setBg(horiModel[1].which, layout.txtWhich2)
        setBg(horiModel[2].which, layout.txtWhich3)
    }

    private fun setBg(result: String?, textView: TextView) {
        if (result!!.contains("매각")) {
            textView.text = result
            textView.setTextColor(ContextCompat.getColor(context!!, R.color.accentColor))
        } else if (result.contains("매입")) {
            textView.text = result
            textView.setTextColor(ContextCompat.getColor(context!!, R.color.fabColor))
        }
    }

}