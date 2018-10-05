package com.dev.skh.resellium.Game.Tab.Xbox


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Game.Model.XboxMainModel
import com.dev.skh.resellium.Game.Tab.Ps4.XboxMainPresenter
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentXboxMainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class XboxMainFragment : BaseFragment(), XboxMainPresenter.View, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener {


    private lateinit var binding: FragmentXboxMainBinding
    private var weakPresenter: WeakReference<XboxMainPresenter>? = null
    private var xboxMainAdapter: XboxMainAdapter? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? = null
    private var first: String = ""
    private var second: String = ""
    private var isLoading: Boolean = false
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_xbox_main, container, false)
        setMVP()

        setView()
        setBaseProgressBar(binding.progressBar)
        return binding.root
    }

    private fun setMVP() {
        weakPresenter = WeakReference(XboxMainPresenter(this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        Handler().postDelayed({
            addItemOnSpinner()
            weakPresenter?.get()?.addData()
        }, 200)
    }

    private fun addItemOnSpinner() {
        weakPresenter?.get()?.spinnerOne()
        weakPresenter?.get()?.spinnerTwo()
    }

    private fun setView() {

        layoutManager = LinearLayoutManager(context!!)
        recyclerView = setGameRv(binding.rvGame, layoutManager)
        binding.swipeLayout.setDistanceToTriggerSync(350)
        binding.swipeLayout.setOnRefreshListener(this)


    }

    override fun updateData(arr: MutableList<XboxMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if(arr != null) {
            if (xboxMainAdapter == null) {
                xboxMainAdapter = XboxMainAdapter(context!!, arr)
                recyclerView?.adapter = xboxMainAdapter
            } else {
                xboxMainAdapter?.addItems(arr)
            }
        }

        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(false)
    }


    override fun errorUpdateData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
        setProgessbarGone()
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
                                val id = xboxMainAdapter?.getItem(xboxMainAdapter!!.itemCount - 1)?.id
                                if (isSpinner)
                                    weakPresenter?.get()?.checkSpinnerScrollData(first, second, id)
                                else
                                    weakPresenter?.get()?.scrollData(id)
                            }, 500)

                        }
                    }
                }
            }
        })
    }

    override fun spinner1(arr1: MutableList<String>) {
        activity!!.runOnUiThread {
            val spinnerAdapter = ArrayAdapter<String>(context, R.layout.item_spinner, arr1)
            spinnerAdapter.setDropDownViewResource(R.layout.item_spinner)
            binding.spinners?.compareFragSpinnerSpinner1?.adapter = spinnerAdapter
            binding.spinners?.compareFragSpinnerSpinner1?.onItemSelectedListener = this
        }
    }

    override fun spinner2(arr2: MutableList<String>) {
        activity!!.runOnUiThread {
            val spinnerAdapter = ArrayAdapter<String>(context, R.layout.item_spinner, arr2)
            spinnerAdapter.setDropDownViewResource(R.layout.item_spinner)
            binding.spinners?.compareFragSpinnerSpinner2?.adapter = spinnerAdapter
            binding.spinners?.compareFragSpinnerSpinner2?.onItemSelectedListener = this
        }
    }

    override fun updateSpinnerData(result: MutableList<XboxMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (result != null)
            xboxMainAdapter?.addItems(result)
        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(true)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.compare_frag_spinner_spinner_1 -> {
                first = binding.spinners?.compareFragSpinnerSpinner1?.selectedItem.toString()
                second = binding.spinners?.compareFragSpinnerSpinner2?.selectedItem.toString()
                if (first != "" && second != "") {
                    callSpinnerData()
                }
            }
            R.id.compare_frag_spinner_spinner_2 -> {
                first = binding.spinners?.compareFragSpinnerSpinner1?.selectedItem.toString()
                second = binding.spinners?.compareFragSpinnerSpinner2?.selectedItem.toString()
                if (first != "" && second != "") {
                    callSpinnerData()
                }
            }
        }
    }

    private fun callSpinnerData() {
        refreshWithoutData()
        weakPresenter?.get()?.checkSpinnerData(first, second)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


    override fun onRefresh() {
        refresh()
    }

    private fun refresh() {
        xboxMainAdapter?.clearItems()
        setProgressbarVisible()
        recyclerView?.removeOnScrollListener(null)
        binding.spinners?.compareFragSpinnerSpinner1?.setSelection(0, false)
        binding.spinners?.compareFragSpinnerSpinner2?.setSelection(0, false)
        weakPresenter?.get()?.addData()
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }

    private fun refreshWithoutData() {
        xboxMainAdapter?.clearItems()
        setProgressbarVisible()
        recyclerView?.removeOnScrollListener(null)
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }

}
