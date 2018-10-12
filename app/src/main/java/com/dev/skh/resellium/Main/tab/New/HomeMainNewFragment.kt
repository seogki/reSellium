package com.dev.skh.resellium.Main.tab.New


import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Main.Model.RankModels
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentHomeMainNewBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class HomeMainNewFragment : Fragment(), HomeMainNewPresenter.View {
    override fun errorData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
    }


    private lateinit var binding: FragmentHomeMainNewBinding
    private var disposable: Disposable? = null
    private var weakReference: WeakReference<HomeMainNewPresenter>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main_new, container, false)
        setMVP()
        setRankColor()
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        weakReference?.get()?.getData()
    }

    private fun setMVP() {
        weakReference = WeakReference(HomeMainNewPresenter(this))
    }

    private fun setRankColor() {
        binding.imgFirst.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.gold), PorterDuff.Mode.SRC_ATOP)
        binding.imgSecond.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.silver), PorterDuff.Mode.SRC_ATOP)
        binding.imgThird.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.bronze), PorterDuff.Mode.SRC_ATOP)
    }

    override fun getNewData(new: RankModels?, disposable: Disposable?) {
        binding.model = new
        this.disposable = disposable
        binding.executePendingBindings()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
