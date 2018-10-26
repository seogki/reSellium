package com.dev.skh.resellium.Game.Tab.Ps4


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Game.Model.Ps4MainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentPs4MainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class Ps4MainFragment : BaseFragment()
        , Ps4MainPresenter.View
        , SwipeRefreshLayout.OnRefreshListener
        , AdapterView.OnItemSelectedListener
        , View.OnClickListener {


    companion object {
        fun weakRef(view: Ps4MainPresenter.View): WeakReference<Ps4MainPresenter> {
            return WeakReference(Ps4MainPresenter(view))
        }
    }

    private lateinit var binding: FragmentPs4MainBinding
    private var ps4MainAdapter: Ps4MainAdapter? = null
    private val weakPresenter by lazy { weakRef(this) }
    //    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var layoutManager: GridLayoutManager
    private var recyclerView: RecyclerView? = null
    private var first: String = ""
    private var second: String = ""
    private var isLoading: Boolean = false
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ps4_main, container, false)
        binding.onClickListener = this
        setView()
        setBaseProgressBar(binding.progressBar)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addItemOnSpinner()
        weakPresenter.get()?.addData()

    }

    private fun setView() {

//        layoutManager = LinearLayoutManager(context!!)
        layoutManager = GridLayoutManager(context, 2)
        recyclerView = setGridGameRv(binding.rvGame, layoutManager)
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

    override fun updateData(arr: MutableList<Ps4MainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (arr != null) {
            if (ps4MainAdapter == null) {
                ps4MainAdapter = Ps4MainAdapter(context!!, arr)
                recyclerView?.adapter = ps4MainAdapter
            } else {
                ps4MainAdapter?.addItems(arr)
            }
        }

        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(false)
    }


    private fun setRecyclerViewScrollbar(isSpinner: Boolean) {

        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {

                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            isLoading = true
                            setProgressbarVisible()
                            Handler().postDelayed({
                                val id = ps4MainAdapter?.getItem(ps4MainAdapter!!.itemCount - 1)?.id
                                if (isSpinner)
                                    weakPresenter.get()?.checkSpinnerScrollData(first, second, id)
                                else
                                    weakPresenter.get()?.scrollData(id)

                            }, 500)

                        }
                    }
                }
            }
        })
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
        weakPresenter.get()?.checkSpinnerData(first, second)
    }

    override fun updateSpinnerData(result: MutableList<Ps4MainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (result != null)
            ps4MainAdapter?.addItems(result)
        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(true)
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
        setProgessbarGone()
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
        recyclerView?.removeOnScrollListener(null)

        binding.spinner.setSelection(0, false)
        weakPresenter.get()?.addData()
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
        recyclerView?.removeOnScrollListener(null)
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }
}
