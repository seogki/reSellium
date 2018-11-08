package com.dev.skh.resellium.Board.Register

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.RadioButton
import android.widget.SeekBar
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityBoardMainRegisterBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class BoardMainRegisterActivity : InnerBaseActivity(), BoardMainRegisterPresenter.View, SeekBar.OnSeekBarChangeListener, View.OnClickListener {


    override fun errorData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        binding.layoutAppbar?.layoutAdd?.isEnabled = true
        DLog.e("error : $message")
        showErrorToast()
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
        binding.layoutAppbar?.layoutAdd?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
    }


    @SuppressLint("SetTextI18n")
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val decimalProgress = progress.toFloat() / 10

        binding.txtNum.text = decimalProgress.toString() + "점"
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
                setRegisterDialog()
            }
        }
    }

    private fun setData() {
        disableAdd()
        val title = binding.editTitle.text.toString()
        val review = binding.editReview.text.toString().trim()
        val grade = checkGrade(binding.txtNum.text.toString())
        if (grade == "") {
            shortToast("평점을 제대로 설정해주세요")
            enableAdd()
            return
        }
        val platform = binding.radiogroupPlatform.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }.trim()
        if (title.isNotEmpty() && review.isNotEmpty() && grade.isNotEmpty()) {
            weakReference.get()?.setData(title, review, grade, platform)
        } else {
            shortToast("모두 입력해주세요")
            enableAdd()
        }
    }

    private fun enableAdd(){
        binding.layoutAppbar?.layoutAdd?.isEnabled = true
    }

    private fun disableAdd(){
        binding.layoutAppbar?.layoutAdd?.isEnabled = false
    }


    private fun checkGrade(grade: String?): String {
        return if (grade != null) {
            if (grade.contains("0.0")) {
                ""
            } else {
                grade.replace("점", "")
            }
        } else
            ""
    }

    private fun setRegisterDialog() {
        AlertDialog.Builder(this@BoardMainRegisterActivity, R.style.MyDialogTheme)
                .setTitle("리뷰")
                .setMessage("정말로 등록하시겠습니까?")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    setData()
                }.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun successData(disposable: Disposable?) {
        this.disposable = disposable
        AlertDialog.Builder(this@BoardMainRegisterActivity, R.style.MyDialogTheme)
                .setTitle("리뷰")
                .setMessage("등록되었습니다.")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }.setNegativeButton(null, null)
                .show()

        enableAdd()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
