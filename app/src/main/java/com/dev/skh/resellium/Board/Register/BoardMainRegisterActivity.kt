package com.dev.skh.resellium.Board.Register

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.Toast
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityBoardMainRegisterBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class BoardMainRegisterActivity : AppCompatActivity(), BoardMainRegisterPresenter.View, SeekBar.OnSeekBarChangeListener, View.OnClickListener {


    override fun errorData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        binding.layoutAppbar?.layoutAdd?.isEnabled = true
        DLog.e("error : $message")
    }

    companion object {
        fun weakRef(view: BoardMainRegisterPresenter.View): WeakReference<BoardMainRegisterPresenter> {
            return WeakReference(BoardMainRegisterPresenter(view))
        }
    }

    private lateinit var binding: ActivityBoardMainRegisterBinding
    private val weakReference by lazy { weakRef(this) }
    private var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_main_register)
        binding.onClickListener = this
        binding.layoutAppbar?.title = "등록"
        binding.layoutAppbar?.onClickListener = this
        binding.seekbar.setOnSeekBarChangeListener(this)
        setView()
    }

    private fun setView() {
        binding.layoutAppbar?.constPlus?.setBackgroundColor(ContextCompat.getColor(this, R.color.OrangeYellow))
        binding.layoutAppbar?.layoutAdd?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.layoutAppbar?.layoutUndo?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val decimalProgress = progress.toFloat() / 10

        binding.txtNum.text = decimalProgress.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> {
                closeKeyboard()
                finish()
            }
            R.id.layout_add -> {
                setData()
            }
        }
    }

    private fun setData() {
        binding.layoutAppbar?.layoutAdd?.isEnabled = false
        val title = binding.editTitle.text.toString()
        val review = binding.editReview.text.toString()
        var grade = binding.txtNum.text.toString()
        if (title.isNotEmpty() && review.isNotEmpty() && grade.isNotEmpty()) {
            if (grade == "10.0")
                grade = "10"
            weakReference.get()?.setData(title, review, grade)
        } else {
            Toast.makeText(this, "모두 입력해주세요", Toast.LENGTH_SHORT).show()
            binding.layoutAppbar?.layoutAdd?.isEnabled = true
        }
    }

    override fun successData(disposable: Disposable?) {
        finish()
    }


    private fun closeKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
