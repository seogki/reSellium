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
import com.dev.skh.resellium.databinding.FragmentPs4MainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class Ps4MainFragment : BaseFragment()
        , GameMainPresenter.View
        , SwipeRefreshLayout.OnRefreshListener
        , AdapterView.OnItemSelectedListener
        , BaseRecyclerViewAdapter.OnItemClickListener
        , CustomNestedScrollListener.OnScrollListener {


    companion object {
        fun weakRef(view: GameMainPresenter.View): WeakReference<GameMainPresenter> {
            return WeakReference(GameMainPresenter(view))
        }
    }

    private lateinit var binding: FragmentPs4MainBinding
    private var ps4MainAdapter: GameMainAdapter? = null
    private val weakPresenter by lazy { weakRef(this) }
    private lateinit var layoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? = null
    private var first: String = ""
    private var second: String = ""
    private var isSpinner = false
    private var isLoading: Boolean = false
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ps4_main, container, false)
        binding.fragment = this
        setView()
        setBaseProgressBar(binding.progressBar)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addItemOnSpinner()
        weakPresenter.get()?.addData("0")

    }

    private fun setView() {
        layoutManager = LinearLayoutManager(context!!)
        recyclerView = setGameRv(binding.rvGame, layoutManager)
        binding.swipeLayout.setDistanceToTriggerSync(350)
        binding.swipeLayout.setOnRefreshListener(this)

    }

    fun callBtnData(view: View?) {
        var data = ""
        when (view?.id) {
            R.id.btn_all -> {
                data = "전체"
                setBtnAccent(binding.btnAll)
                setBtnDefault(binding.btnNew)
                setBtnDefault(binding.btnOld)
            }
            R.id.btn_new -> {
                data = "신품"
                setBtnAccent(binding.btnNew)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnOld)
            }
            R.id.btn_old -> {
                data = "중고"
                setBtnAccent(binding.btnOld)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnNew)
            }
        }
        first = if (data == "전체")
            ""
        else
            data
        isSpinner = true
        callSpinnerData()
    }

    override fun updateData(arr: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean) {

        arr?.let {
            if (ps4MainAdapter == null) {
                setAdapter(it)
            } else {
                ps4MainAdapter?.addItems(it)
            }

        }

        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }

    private fun setAdapter(it: MutableList<GameMainModel>) {
        ps4MainAdapter = GameMainAdapter(context!!, it)
        ps4MainAdapter?.setOnItemClickListener(this)
        recyclerView?.adapter = ps4MainAdapter
    }

    override fun onItemClick(view: View, position: Int) {
        ps4MainAdapter?.let { setInnerIntent(it.getItem(position), view) }
    }

    private fun setRecyclerViewScrollbar() {
        binding.nestedScroll.setOnScrollChangeListener(CustomNestedScrollListener(layoutManager, this))
    }

    override fun onScrollEnd() {
        if (!isLoading) {
            isLoading = true
            setProgressbarVisible()
            val id = ps4MainAdapter?.getItem(ps4MainAdapter!!.itemCount - 1)?.id
            setData(id)
        }
    }

    private fun setData(id: String?) {
        Handler().postDelayed({
            binding.nestedScroll.fling(0)
            if (isSpinner)
                weakPresenter.get()?.checkSpinnerScrollData("0", first, second, id)
            else
                weakPresenter.get()?.scrollData("0", id)

        }, 500)
    }


    private fun addItemOnSpinner() {
        weakPresenter.get()?.spinnerTwo()
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
        weakPresenter.get()?.checkSpinnerData("0", first, second)
    }

    override fun updateSpinnerData(result: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean) {

        result?.let { ps4MainAdapter?.addItems(it) }

        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }

    override fun spinner2(arr2: MutableList<String>) {
        activity!!.runOnUiThread {
            val spinnerAdapter = ArrayAdapter<String>(context, R.layout.item_spinner, arr2)
            spinnerAdapter.setDropDownViewResource(R.layout.item_spinner)
            binding.spinner.adapter = spinnerAdapter
            binding.spinner.onItemSelectedListener = this
        }
    }


    override fun errorUpdateData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
        setProgressbarGone()
        showErrorToast()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


    override fun onRefresh() {
        refresh()
    }

    private fun refresh() {
        ps4MainAdapter?.clearItems()
        setViewDefault()
        recyclerView?.clearOnScrollListeners()
        isSpinner = false
        binding.spinner.setSelection(0, false)
        weakPresenter.get()?.addData("0")
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

    }

    private fun refreshWithoutData() {
        ps4MainAdapter?.clearItems()
        setProgressbarVisible()
        recyclerView?.clearOnScrollListeners()
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }
}
