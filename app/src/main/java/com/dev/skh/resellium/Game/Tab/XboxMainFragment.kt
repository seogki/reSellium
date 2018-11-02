package com.dev.skh.resellium.Game.Tab


import android.content.Intent
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
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Game.GameMainAdapter
import com.dev.skh.resellium.Game.GameMainPresenter
import com.dev.skh.resellium.Game.Inner.GameMainCommentActivity
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentXboxMainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class XboxMainFragment : BaseFragment()
        , GameMainPresenter.View
        , SwipeRefreshLayout.OnRefreshListener
        , AdapterView.OnItemSelectedListener
        , View.OnClickListener
        , BaseRecyclerViewAdapter.OnItemClickListener {


    companion object {
        fun weakRef(view: GameMainPresenter.View): WeakReference<GameMainPresenter> {
            return WeakReference(GameMainPresenter(view))
        }
    }

    private val weakPresenter by lazy { weakRef(this) }
    private lateinit var binding: FragmentXboxMainBinding
    private var xboxMainAdapter: GameMainAdapter? = null
    //    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var layoutManager: GridLayoutManager
    private var recyclerView: RecyclerView? = null
    private var first: String = ""
    private var second: String = ""
    private var isLoading: Boolean = false
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_xbox_main, container, false)
        binding.onClickListener = this
        setView()
        setBaseProgressBar(binding.progressBar)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        addItemOnSpinner()
        weakPresenter.get()?.addData("1")

    }

    private fun addItemOnSpinner() {
        weakPresenter.get()?.spinnerTwo()
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

    override fun onItemClick(view: View, position: Int) {
        val data = xboxMainAdapter?.getItem(position)
        val intent = Intent(view.context, GameMainCommentActivity::class.java)
        intent.putExtra("data", data)
        startActivity(intent)
    }

    override fun updateData(arr: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (arr != null) {
            if (xboxMainAdapter == null) {
                xboxMainAdapter = GameMainAdapter(context!!, arr)
                recyclerView?.adapter = xboxMainAdapter
                xboxMainAdapter?.setOnItemClickListener(this)
            } else {
                xboxMainAdapter?.addItems(arr)
            }
        }

        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(false)
    }


    override fun errorUpdateData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
        setProgressbarGone()
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
                                    weakPresenter.get()?.checkSpinnerScrollData("1",first, second, id)
                                else
                                    weakPresenter.get()?.scrollData("1",id)
                            }, 500)

                        }
                    }
                }
            }
        })
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
            xboxMainAdapter?.addItems(result)
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
        weakPresenter.get()?.checkSpinnerData("1",first, second)
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
        setViewDefault()
        recyclerView?.removeOnScrollListener(null)
        weakPresenter.get()?.addData("1")
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
        xboxMainAdapter?.clearItems()
        setProgressbarVisible()
        recyclerView?.removeOnScrollListener(null)
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }

}