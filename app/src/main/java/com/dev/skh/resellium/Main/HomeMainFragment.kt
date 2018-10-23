package com.dev.skh.resellium.Main


import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.TextView
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Board.BoardMainActivity
import com.dev.skh.resellium.Game.GameMainActivity
import com.dev.skh.resellium.Main.Model.HoriModel
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main, container, false)
        binding.layoutAppbar?.title = "메인"
        binding.layoutAppbar?.onClickListener = this
        invisibleView()

        binding.onClickListener = this
        setTabLayout()
        setRv()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Handler().postDelayed({

            getData()
        }, 10)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_next_board -> {
                beginActivity(Intent(context!!, BoardMainActivity::class.java))
            }
            R.id.img_refresh -> {
                invisibleView()
                onRefresh()
            }
            R.id.img_recent_xbox -> {
                beginActivity(Intent(context!!, GameMainActivity::class.java))
            }
            R.id.img_setting -> startActivity(Intent(context!!, UserMainActivity::class.java))
        }
    }

    private fun onRefresh() {
        rfBtn = true
        startAnimation()

        Handler().postDelayed({
            getData()
        }, 400)

    }

    private fun startAnimation() {
        val rotate = RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.interpolator = LinearInterpolator()
        rotate.duration = 500
        rotate.repeatCount = Animation.INFINITE
        binding.imgRefresh.startAnimation(rotate)
    }

    private fun stopAnimation() {
        binding.imgRefresh.clearAnimation()
    }

    private fun setTabLayout() {
        viewPager = binding.viewpager
        adapter = TabPagerAdapter(childFragmentManager)
        viewPager.offscreenPageLimit = 3
        adapter.addFragment(HomeMainNewFragment(), "최신")
        adapter.addFragment(HomeMainBestFragment(), "최고")
        adapter.addFragment(HomeMainWorstFragment(), "최저")
        tabLayout = binding.tablayout

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }


    private fun getData() {
        weakPresenter.get()?.getHoriData("PS")
        weakPresenter.get()?.getHoriData("XBOX")
        weakPresenter.get()?.getHoriData("SWITCH")
    }

    private fun setRv() {


        binding.txtPs4.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.txtSwitch.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.txtXbox.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)

    }

    override fun updateData(result: MutableList<HoriModel>?, disposable: Disposable?, currentPos: String) {
        if (result != null) {
            when (currentPos) {
                "PS" -> setPs4Data(result)
                "XBOX" -> setXboxData(result)
                "SWITCH" -> setSwitchData(result)
            }
        }
        defaultSetting()
        this.disposable = disposable
    }

    private fun setPs4Data(horiModel: MutableList<HoriModel>?) {
        if (horiModel != null) {
            setRecentData(binding.layoutPs, horiModel)
        }
    }

    private fun setXboxData(horiModel: MutableList<HoriModel>?) {
        if (horiModel != null) {
            setRecentData(binding.layoutXbox, horiModel)
        }
    }

    private fun setSwitchData(horiModel: MutableList<HoriModel>?) {
        if (horiModel != null) {
            setRecentData(binding.layoutSwitch, horiModel)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun invisibleView() {
        binding.constAll.visibility = View.INVISIBLE
        binding.viewEnd.visibility = View.INVISIBLE
    }

    private fun defaultSetting() {
        if (rfBtn) {
            stopAnimation()
            rfBtn = false
        }
        binding.constAll.visibility = View.VISIBLE
        binding.viewEnd.visibility = View.VISIBLE
    }

    private fun setRecentData(layout: LayoutRecentBinding?, horiModel: MutableList<HoriModel>) {
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
        if (result!!.contains("매입")) {
            textView.text = result
//            textView.background = ContextCompat.getDrawable(context!!, R.drawable.text_green)
        } else if (result.contains("매각")) {
            textView.text = result
//            textView.background = ContextCompat.getDrawable(context!!, R.drawable.text_red)
        }
    }

}
