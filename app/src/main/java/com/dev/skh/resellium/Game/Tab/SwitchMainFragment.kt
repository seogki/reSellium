package com.dev.skh.resellium.Game.Tab


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Game.GameMainAdapter
import com.dev.skh.resellium.Game.GameMainPresenter
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.CustomNestedScrollListener
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentSwitchMainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class SwitchMainFragment : BaseFragment()
        , GameMainPresenter.View
        , SwipeRefreshLayout.OnRefreshListener
        , AdapterView.OnItemSelectedListener
        , View.OnClickListener
        , BaseRecyclerViewAdapter.OnItemClickListener
        , CustomNestedScrollListener.OnScrollListener {


    companion object {
        fun weakRef(view: GameMainPresenter.View): WeakReference<GameMainPresenter> {
            return WeakReference(GameMainPresenter(view))
        }
    }

    private val weakPresenter by lazy { weakRef(this) }
    private lateinit var binding: FragmentSwitchMainBinding
    private var switchMainAdapter: GameMainAdapter? = null
    private lateinit var layoutManager: LinearLayoutManager
    //    private lateinit var layoutManager: GridLayoutManager
    private var recyclerView: RecyclerView? = null
    private var first: String = ""
    private var second: String = ""
    private var isSpinner = false
    private var isLoading: Boolean = false
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_switch_main, container, false)
        binding.onClickListener = this
        setView()
        setBaseProgressBar(binding.progressBar)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addItemOnSpinner()
        weakPresenter.get()?.addData("2")

    }

    private fun addItemOnSpinner() {
        weakPresenter.get()?.spinnerTwo()
    }

    private fun setView() {
        layoutManager = LinearLayoutManager(context!!)
//        layoutManager = GridLayoutManager(context, 2)
        recyclerView = setGameRv(binding.rvGame, layoutManager)

        binding.swipeLayout.setDistanceToTriggerSync(350)
        binding.swipeLayout.setOnRefreshListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_all -> callBtnData("전체")
            R.id.btn_new -> callBtnData("신품")
            R.id.btn_old -> callBtnData("중고")
        }
    }

    private fun callBtnData(data: String) {
        when (data) {
            "전체" -> {
                setBtnAccent(binding.btnAll)
                setBtnDefault(binding.btnNew)
                setBtnDefault(binding.btnOld)
            }
            "신품" -> {
                setBtnAccent(binding.btnNew)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnOld)
            }
            "중고" -> {
                setBtnAccent(binding.btnOld)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnNew)
            }
        }
        first = if (data == "전체")
            ""
        else
            data

        callSpinnerData()
    }

    override fun updateData(arr: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (arr != null) {
            if (switchMainAdapter == null) {
                switchMainAdapter = GameMainAdapter(context!!, arr)
                recyclerView?.adapter = switchMainAdapter
                switchMainAdapter?.setOnItemClickListener(this)
            } else {
                switchMainAdapter?.addItems(arr)
            }
        }

        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(false)
    }

    override fun onItemClick(view: View, position: Int) {
        val data = switchMainAdapter?.getItem(position)
        setInnerIntent(data, view)
    }

    private fun setRecyclerViewScrollbar(isSpinner: Boolean) {
        this.isSpinner = isSpinner
        binding.nestedScroll.setOnScrollChangeListener(CustomNestedScrollListener(layoutManager, this))
    }

    override fun onScrollEnd() {
        if (!isLoading) {

            isLoading = true
            setProgressbarVisible()
            val id = switchMainAdapter?.getItem(switchMainAdapter!!.itemCount - 1)?.id
            setData(id)


        }
    }

    private fun setData(id: String?) {
        Handler().postDelayed({
            binding.nestedScroll.fling(0)
            if (isSpinner)
                weakPresenter.get()?.checkSpinnerScrollData("2", first, second, id)
            else
                weakPresenter.get()?.scrollData("2", id)
        }, 500)
    }

    override fun spinner2(arr2: MutableList<String>) {
        activity!!.runOnUiThread {
            val spinnerAdapter = ArrayAdapter<String>(context, R.layout.item_spinner, arr2)
            spinnerAdapter.setDropDownViewResource(R.layout.item_spinner)
            binding.spinner.adapter = spinnerAdapter
            binding.spinner.onItemSelectedListener = this
        }
    }

    override fun updateSpinnerData(result: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (result != null)
            switchMainAdapter?.addItems(result)
        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(true)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner -> {
                second = binding.spinner.selectedItem.toString()
                if (second != "") {
                    callSpinnerData()
                }
            }
        }
    }

    private fun callSpinnerData() {
        refreshWithoutData()
        weakPresenter.get()?.checkSpinnerData("2", first, second)
    }


    override fun errorUpdateData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
        setProgressbarGone()
        showErrorToast()

    }

    override fun onRefresh() {
        refresh()
    }


    private fun refresh() {
        switchMainAdapter?.clearItems()
        setViewDefault()
        recyclerView?.removeOnScrollListener(null)
        weakPresenter.get()?.addData("2")
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }

    private fun setViewDefault() {
        setProgressbarVisible()
        setBtnAccent(binding.btnAll)
        setBtnDefault(binding.btnNew)
        setBtnDefault(binding.btnOld)
        first = ""
        second = ""
        binding.spinner.setSelection(0, false)
    }

    private fun refreshWithoutData() {
        switchMainAdapter?.clearItems()
        setProgressbarVisible()
        recyclerView?.removeOnScrollListener(null)
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }

}
