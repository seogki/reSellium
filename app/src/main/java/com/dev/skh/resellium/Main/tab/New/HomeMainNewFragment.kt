package com.dev.skh.resellium.Main.tab.New


import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentHomeMainNewBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class HomeMainNewFragment : BaseFragment(), HomeMainNewPresenter.View,View.OnClickListener {


    override fun errorData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
    }

    companion object {
        fun weakRef(view: HomeMainNewPresenter.View): WeakReference<HomeMainNewPresenter> {
            return WeakReference(HomeMainNewPresenter(view))
        }
    }


    private lateinit var binding: FragmentHomeMainNewBinding
    private var disposable: Disposable? = null
    private var model: MutableList<BoardMainModel>? = null
    private val weakReference by lazy { weakRef(this) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main_new, container, false)
        binding.onClickListener = this
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
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.const_first -> setCheckType("1")
            R.id.const_second -> setCheckType("2")
            R.id.const_third -> setCheckType("3")
        }
    }

    private fun setCheckType(type: String) {
        var data: BoardMainModel? = null
        var view: TextView? = null
        when (type) {
            "1" -> {
                data = model?.get(0)
                view = binding.txtTitle1
            }
            "2" -> {
                data = model?.get(1)
                view = binding.txtTitle2
            }
            "3" -> {
                data = model?.get(2)
                view = binding.txtTitle3
            }
        }
        if (data != null) {

            setTabInnerIntent(data, view)
        } else {
            Toast.makeText(context!!, "오류가 발생하였습니다. 나중에 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getNewData(arr: MutableList<BoardMainModel>?, disposable: Disposable?) {
        if (arr != null) {
            model = arr
            binding.txtTitle1.text = arr[0].title

            binding.txtTitle2.text = arr[1].title
            binding.txtTitle3.text = arr[2].title
            binding.layoutFirst.text = arr[0].grade
            binding.layoutSecond.text = arr[1].grade
            binding.layoutThird.text = arr[2].grade

            binding.txtReview1.text = arr[0].review
            binding.txtReview2.text = arr[1].review
            binding.txtReview3.text = arr[2].review

        }
        this.disposable = disposable
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
