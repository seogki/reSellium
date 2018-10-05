package com.dev.skh.resellium.Game.Tab.Search

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.dev.skh.resellium.Game.Model.Ps4MainModel
import com.dev.skh.resellium.Game.Model.SwitchMainModel
import com.dev.skh.resellium.Game.Model.XboxMainModel
import com.dev.skh.resellium.Game.Tab.Ps4.Ps4MainAdapter
import com.dev.skh.resellium.Game.Tab.Switch.SwitchMainAdapter
import com.dev.skh.resellium.Game.Tab.Xbox.XboxMainAdapter
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivitySearchMainBinding
import io.reactivex.disposables.Disposable

class SearchMainActivity : AppCompatActivity(), View.OnClickListener, SearchMainPresenter.View {
    override fun setError(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
        setProgessbarGone()
    }

    private fun closeKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun clearAndClose(edit: EditText) {
        edit.text.clear()
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> {
                closeKeyboard()
                finish()
            }
            R.id.radio_ps4 -> setToolbarColor("PS")
            R.id.radio_xbox -> setToolbarColor("XBOX")
            R.id.radio_switch -> setToolbarColor("SWITCH")
            R.id.img_search -> startGetData(binding.layoutAppbar?.editSearch?.text.toString(), currentPos)
        }
    }


    private var currentPos: String? = "PS"
    private var disposable: Disposable? = null
    private var searchString = ""
    lateinit var binding: ActivitySearchMainBinding
    private lateinit var presenter: SearchMainPresenter
    private var ps4MainAdapter: Ps4MainAdapter? = null
    private var xboxMainAdapter: XboxMainAdapter? = null
    private var switchMainAdapter: SwitchMainAdapter? = null
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
        presenter = SearchMainPresenter(this)
        binding.layoutAppbar?.layoutUndo?.drawable?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
    }

    private fun setAdapter() {

        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decor.setDrawable(ContextCompat.getDrawable(this, R.drawable.survey_divder)!!)


        ps4MainAdapter = Ps4MainAdapter(this, mutableListOf())
        ps4LayoutManager = LinearLayoutManager(this)
        binding.rvPs4.layoutManager = ps4LayoutManager
        binding.rvPs4.isNestedScrollingEnabled = false
        binding.rvPs4.animation = null
        binding.rvPs4.addItemDecoration(decor)

        xboxMainAdapter = XboxMainAdapter(this, mutableListOf())
        xboxLayoutManager = LinearLayoutManager(this)
        binding.rvXbox.layoutManager = xboxLayoutManager
        binding.rvXbox.isNestedScrollingEnabled = false
        binding.rvXbox.animation = null
        binding.rvXbox.addItemDecoration(decor)

        switchMainAdapter = SwitchMainAdapter(this, mutableListOf())
        switchLayoutManager = LinearLayoutManager(this)
        binding.rvSwitch.layoutManager = switchLayoutManager
        binding.rvSwitch.isNestedScrollingEnabled = false
        binding.rvSwitch.animation = null
        binding.rvSwitch.addItemDecoration(decor)

        Handler().postDelayed({
            binding.rvPs4.adapter = ps4MainAdapter
            binding.rvSwitch.adapter = switchMainAdapter
            binding.rvXbox.adapter = xboxMainAdapter

        }, 100)
    }


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
            presenter.getData(this.currentPos, searchString)
        } else {
            Toast.makeText(this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setRecyclerViewScrollbar() {
        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
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

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            isLoading = true
                            setProgressbarVisible()
                            Handler().postDelayed({
                                var id: String? = null
                                when (currentPos) {
                                    "PS" -> id = ps4MainAdapter?.getItem(ps4MainAdapter!!.itemCount - 1)?.id
                                    "XBOX" -> id = xboxMainAdapter?.getItem(xboxMainAdapter!!.itemCount - 1)?.id
                                    "SWITCH" -> id = switchMainAdapter?.getItem(switchMainAdapter!!.itemCount - 1)?.id
                                }

                                presenter.getScrollData(id, currentPos, searchString)

                            }, 500)

                        }
                    }
                }
            }
        })
    }

    override fun setPs4Data(result: MutableList<Ps4MainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (result != null)
            ps4MainAdapter?.addItems(result)
        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }

    override fun setXboxData(result: MutableList<XboxMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (result != null)
            xboxMainAdapter?.addItems(result)
        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }

    override fun setSwitchData(result: MutableList<SwitchMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (result != null)
            switchMainAdapter?.addItems(result)
        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }

    private fun setProgessbarGone() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setProgressbarVisible() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setToolbarColor(text: String) {
        var color: Int? = null
        var drawable: Drawable? = null
        when (text) {
            "PS" -> {
                color = ContextCompat.getColor(this, R.color.ps4Color)
                drawable = ContextCompat.getDrawable(this, R.drawable.text_stroke_ps4)

            }
            "XBOX" -> {
                color = ContextCompat.getColor(this, R.color.xboxColor)
                drawable = ContextCompat.getDrawable(this, R.drawable.text_stroke_xbox)
            }
            "SWITCH" -> {
                color = ContextCompat.getColor(this, R.color.switchColor)
                drawable = ContextCompat.getDrawable(this, R.drawable.text_stroke_switch)

            }

        }
        currentPos = text
        setProgessbarGone()
        binding.layoutAppbar?.editSearch?.background = drawable
        binding.layoutAppbar?.layoutUndo?.setBackgroundColor(color!!)

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
