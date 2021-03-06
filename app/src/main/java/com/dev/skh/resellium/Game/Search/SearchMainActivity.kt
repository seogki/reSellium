package com.dev.skh.resellium.Game.Search

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.Game.GameMainAdapter
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivitySearchMainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class SearchMainActivity : InnerBaseActivity()
        , View.OnClickListener
        , SearchMainPresenter.View
        , TextView.OnEditorActionListener
        , BaseRecyclerViewAdapter.OnItemClickListener {

    override fun onItemClick(view: View, position: Int) {
        when (currentPos) {
            "PS" -> setInnerIntent(ps4MainAdapter?.getItem(position), view)
            "XBOX" -> setInnerIntent(xboxMainAdapter?.getItem(position), view)
            "SWITCH" -> setInnerIntent(switchMainAdapter?.getItem(position), view)
        }
    }


    override fun setError(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
        setProgressbarGone()
        showErrorToast()
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }


    companion object {
        fun weakRef(view: SearchMainPresenter.View): WeakReference<SearchMainPresenter> {
            return WeakReference(SearchMainPresenter(view))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> {
                closeKeyboard()
                finish()
            }
            R.id.btn_ps -> setToolbarColor("PS")
            R.id.btn_xbox -> setToolbarColor("XBOX")
            R.id.btn_switch -> setToolbarColor("SWITCH")
            R.id.img_search -> startGetData(binding.layoutAppbar?.editSearch?.text.toString(), currentPos)
        }
    }

    private var currentPos = "PS"
    private var disposable: Disposable? = null
    private var searchString = ""
    private lateinit var binding: ActivitySearchMainBinding
    private val presenter by lazy { weakRef(this) }
    private var ps4MainAdapter: GameMainAdapter? = null
    private var xboxMainAdapter: GameMainAdapter? = null
    private var switchMainAdapter: GameMainAdapter? = null
    private lateinit var ps4LayoutManager: LinearLayoutManager
    private lateinit var xboxLayoutManager: LinearLayoutManager
    private lateinit var switchLayoutManager: LinearLayoutManager
    private var isLoading: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_main)
        binding.layoutAppbar?.onClickListener = this
        binding.onClickListener = this
        setAdapter()
        setBaseProgressBar(binding.progressBar)
    }

    private fun setAdapter() {

        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decor.setDrawable(ContextCompat.getDrawable(this, R.drawable.survey_divider)!!)

        binding.layoutAppbar?.editSearch?.setOnEditorActionListener(this)

        ps4LayoutManager = LinearLayoutManager(this)
        binding.rvPs4.layoutManager = ps4LayoutManager
        binding.rvPs4.isNestedScrollingEnabled = false

        xboxLayoutManager = LinearLayoutManager(this)
        binding.rvXbox.layoutManager = xboxLayoutManager
        binding.rvXbox.isNestedScrollingEnabled = false

        switchLayoutManager = LinearLayoutManager(this)
        binding.rvSwitch.layoutManager = switchLayoutManager
        binding.rvSwitch.isNestedScrollingEnabled = false

        binding.rvPs4.addItemDecoration(decor)

        binding.rvXbox.addItemDecoration(decor)

        binding.rvSwitch.addItemDecoration(decor)

    }


    @SuppressLint("SetTextI18n")
    private fun startGetData(search: String?, currentPos: String?) {
        clearAndClose(binding.layoutAppbar!!.editSearch)
        onRefresh()
        binding.txtSearch.text = "검색어: $search"
        if (search != null && search != "") {
            when (currentPos) {
                "PS" -> {
                    binding.rvPs4.visibility = View.VISIBLE
                    binding.rvSwitch.visibility = View.GONE
                    binding.rvXbox.visibility = View.GONE

                }
                "XBOX" -> {
                    binding.rvPs4.visibility = View.GONE
                    binding.rvSwitch.visibility = View.GONE
                    binding.rvXbox.visibility = View.VISIBLE
                }
                "SWITCH" -> {
                    binding.rvPs4.visibility = View.GONE
                    binding.rvSwitch.visibility = View.VISIBLE
                    binding.rvXbox.visibility = View.GONE
                }

            }
            searchString = search
            setProgressbarVisible()
            presenter.get()?.getData(this.currentPos, searchString)
        } else {
            Toast.makeText(this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        var handle = false
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            startGetData(binding.layoutAppbar?.editSearch?.text.toString(), currentPos)
            handle = true
        }
        return handle
    }


    private fun setRecyclerViewScrollbar() {
        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {

                    var visibleItemCount = 0
                    var totalItemCount = 0
                    var pastVisiblesItems = 0
                    when (currentPos) {
                        "PS" -> {
                            visibleItemCount = ps4LayoutManager.childCount
                            totalItemCount = ps4LayoutManager.itemCount
                            pastVisiblesItems = ps4LayoutManager.findFirstVisibleItemPosition()
                        }
                        "XBOX" -> {
                            visibleItemCount = xboxLayoutManager.childCount
                            totalItemCount = xboxLayoutManager.itemCount
                            pastVisiblesItems = xboxLayoutManager.findFirstVisibleItemPosition()
                        }
                        "SWITCH" -> {
                            visibleItemCount = switchLayoutManager.childCount
                            totalItemCount = switchLayoutManager.itemCount
                            pastVisiblesItems = switchLayoutManager.findFirstVisibleItemPosition()
                        }
                    }
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (!isLoading) {
                            isLoading = true
                            setProgressbarVisible()
                            Handler().postDelayed({
                                binding.nestedScroll.fling(0)

                                presenter.get()?.getScrollData(getId(), currentPos, searchString)

                            }, 500)

                        }
                    }
                }
            }
        })


    }

    private fun getId(): String? =
            when (currentPos) {
                "PS" -> ps4MainAdapter?.getItem(ps4MainAdapter!!.itemCount - 1)?.id
                "XBOX" -> xboxMainAdapter?.getItem(xboxMainAdapter!!.itemCount - 1)?.id
                "SWITCH" -> switchMainAdapter?.getItem(switchMainAdapter!!.itemCount - 1)?.id
                else -> ""
            }


    override fun setPs4Data(result: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        result?.let {
            when (ps4MainAdapter) {
                null -> {
                    ps4MainAdapter = GameMainAdapter(this, it).apply { setOnItemClickListener(this@SearchMainActivity) }
                    binding.rvPs4.adapter = ps4MainAdapter
                }
                else -> ps4MainAdapter?.addItems(it)
            }
        }

        Handler().postDelayed({
            ps4MainAdapter?.itemCount?.let { setviewGone(it) }
        }, 100)

        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll) setRecyclerViewScrollbar()
    }

    override fun setXboxData(result: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        result?.let {
            when (xboxMainAdapter) {
                null -> {
                    xboxMainAdapter = GameMainAdapter(this, it).apply { setOnItemClickListener(this@SearchMainActivity) }
                    binding.rvXbox.adapter = xboxMainAdapter
                }
                else -> xboxMainAdapter?.addItems(it)
            }
        }

        Handler().postDelayed({
            xboxMainAdapter?.itemCount?.let { setviewGone(it) }
        }, 100)

        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }


    override fun setSwitchData(result: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        result?.let {
            when (switchMainAdapter) {
                null -> {
                    switchMainAdapter = GameMainAdapter(this, it).apply { setOnItemClickListener(this@SearchMainActivity) }
                    binding.rvSwitch.adapter = switchMainAdapter
                }
                else -> switchMainAdapter?.addItems(it)
            }
        }

        Handler().postDelayed({
            switchMainAdapter?.itemCount?.let { setviewGone(it) }
        }, 100)

        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }

    private fun setviewGone(item_size: Int) {
        when (item_size) {
            0 -> binding.txtNoComment.visibility = View.VISIBLE
            else -> binding.txtNoComment.visibility = View.GONE
        }
    }


    private fun setToolbarColor(text: String) {
        binding.txtNoComment.visibility = View.GONE
        currentPos = text
        when (currentPos) {
            "PS" -> {
                setBtnAccent(binding.btnPs)
                setBtnDefault(binding.btnXbox)
                setBtnDefault(binding.btnSwitch)
            }
            "XBOX" -> {
                setBtnAccent(binding.btnXbox)
                setBtnDefault(binding.btnPs)
                setBtnDefault(binding.btnSwitch)
            }
            "SWITCH" -> {
                setBtnAccent(binding.btnSwitch)
                setBtnDefault(binding.btnXbox)
                setBtnDefault(binding.btnPs)
            }
        }
        setSnackBar(binding.coordinator, "$currentPos 설정되었습니다")
        setProgressbarGone()
    }

    private fun onRefresh() {
        binding.rvSwitch.removeOnScrollListener(null)
        binding.rvPs4.removeOnScrollListener(null)
        binding.rvXbox.removeOnScrollListener(null)
        xboxMainAdapter?.clearItems()
        ps4MainAdapter?.clearItems()
        switchMainAdapter?.clearItems()
        isLoading = false

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
