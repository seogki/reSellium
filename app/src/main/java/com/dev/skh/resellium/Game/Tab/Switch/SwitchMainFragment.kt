package com.dev.skh.resellium.Game.Tab.Switch


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
import com.dev.skh.resellium.Game.Model.SwitchMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentSwitchMainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class SwitchMainFragment : BaseFragment(), SwitchMainPresenter.View, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener {


    private lateinit var binding: FragmentSwitchMainBinding
    private var weakPresenter: WeakReference<SwitchMainPresenter>? = null
    private var switchMainAdapter: SwitchMainAdapter? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? = null
    private var first: String = ""
    private var second: String = ""
    private var isLoading: Boolean = false
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_switch_main, container, false)
        setMVP()

        setView()
        setBaseProgressBar(binding.progressBar)
        return binding.root
    }

    private fun setMVP() {
        weakPresenter = WeakReference(SwitchMainPresenter(this))
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

    override fun updateData(arr: MutableList<SwitchMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if(arr != null) {
            if (switchMainAdapter == null) {
                switchMainAdapter = SwitchMainAdapter(context!!, arr)
                recyclerView?.adapter = switchMainAdapter
            } else {
                switchMainAdapter?.addItems(arr)
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
                                val id = switchMainAdapter?.getItem(switchMainAdapter!!.itemCount - 1)?.id
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

    override fun updateSpinnerData(result: MutableList<SwitchMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (result != null)
            switchMainAdapter?.addItems(result)
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


    override fun errorUpdateData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
        setProgessbarGone()

    }

    override fun onRefresh() {
        refresh()
    }


    private fun refresh() {
        switchMainAdapter?.clearItems()
        setProgressbarVisible()
        recyclerView?.removeOnScrollListener(null)
        binding.spinners?.compareFragSpinnerSpinner1?.setSelection(0, false)
        binding.spinners?.compareFragSpinnerSpinner2?.setSelection(0, false)
        weakPresenter?.get()?.addData()
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }

    private fun refreshWithoutData() {
        switchMainAdapter?.clearItems()
        setProgressbarVisible()
        recyclerView?.removeOnScrollListener(null)
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }

}
