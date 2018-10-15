package com.dev.skh.resellium.Main.tab.Best


import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Main.Model.RankModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentHomeMainBestBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class HomeMainBestFragment : BaseFragment(), HomeMainBestPresenter.View {

    override fun errorData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
    }

    companion object {
        fun weakRef(view: HomeMainBestPresenter.View): WeakReference<HomeMainBestPresenter> {
            return WeakReference(HomeMainBestPresenter(view))
        }
    }


    private lateinit var binding: FragmentHomeMainBestBinding
    private var disposable: Disposable? = null
    private val weakReference by lazy { weakRef(this) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main_best, container, false)
        setRankColor()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        weakReference.get()?.getData()
    }


    private fun setRankColor() {
        binding.imgFirst.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.gold), PorterDuff.Mode.SRC_ATOP)
        binding.imgSecond.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.silver), PorterDuff.Mode.SRC_ATOP)
        binding.imgThird.drawable?.setColorFilter(ContextCompat.getColor(context!!, R.color.bronze), PorterDuff.Mode.SRC_ATOP)
    }

    override fun getBestData(best: MutableList<RankModel>?, disposable: Disposable?) {
        if(best != null) {
            binding.txtTitle1.text = best[0].title
            binding.txtTitle2.text = best[1].title
            binding.txtTitle3.text = best[2].title
            binding.layoutFirst.text = best[0].grade
            binding.layoutSecond.text = best[1].grade
            binding.layoutThird.text = best[2].grade

            binding.txtReview1.text = best[0].review
            binding.txtReview2.text = best[1].review
            binding.txtReview3.text = best[2].review

        }
        this.disposable = disposable
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
