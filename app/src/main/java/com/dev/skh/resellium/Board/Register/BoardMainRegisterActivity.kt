package com.dev.skh.resellium.Board.Register

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
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
        enableAdd()
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
    private var platform = "PS"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_main_register)
        binding.layoutAppbar?.title = "등록"
        binding.activity = this
        binding.layoutAppbar?.onClickListener = this
        binding.seekbar.setOnSeekBarChangeListener(this)

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
            R.id.layout_add -> setRegisterDialog()
        }
    }

    fun setPlatform(view: View?) {
        when (view?.id) {
            R.id.btn_ps -> {
                this.platform = "PS"
                setBtnAccent(binding.btnPs)
                setBtnDefault(binding.btnXbox)
                setBtnDefault(binding.btnSwitch)
                binding.imgPlatform.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons8_playstation_24))
            }
            R.id.btn_xbox -> {
                this.platform = "XBOX"
                setBtnAccent(binding.btnXbox)
                setBtnDefault(binding.btnPs)
                setBtnDefault(binding.btnSwitch)
                binding.imgPlatform.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons8_xbox_24))
            }
            R.id.btn_switch -> {
                this.platform = "SWITCH"
                setBtnAccent(binding.btnSwitch)
                setBtnDefault(binding.btnXbox)
                setBtnDefault(binding.btnPs)
                binding.imgPlatform.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nintendo_switch_logo))
            }

        }

    }

    private fun setData() {
        disableAdd()
        val title = binding.editTitle.text.toString()
        val review = binding.editReview.text.toString().trim()
        val grade = checkGrade(binding.txtNum.text.toString())

        when {
            (grade == "") -> {
                shortToast("평점을 제대로 설정해주세요")
                enableAdd()
                return
            }
            (title.isNotEmpty() && review.isNotEmpty() && grade.isNotEmpty()) -> {
                weakReference.get()?.setData(title, review, grade, platform)
            }
            else -> {
                shortToast("모두 입력해주세요")
                enableAdd()
            }
        }

    }

    private fun enableAdd() {
        binding.layoutAppbar?.layoutAdd?.isEnabled = true
        binding.imgMark.isEnabled = true
    }

    private fun disableAdd() {
        binding.layoutAppbar?.layoutAdd?.isEnabled = false
        binding.imgMark.isEnabled = false
    }


    private fun checkGrade(grade: String?): String =
            when {
                grade != null -> {
                    if (grade.contains("0.0")) {
                        ""
                    } else {
                        grade.replace("점", "")
                    }
                }
                else -> ""
            }


    fun setRegisterDialog() {
        AlertDialog.Builder(this@BoardMainRegisterActivity, R.style.MyDialogTheme)
                .setTitle("리뷰")
                .setMessage("정말로 등록하시겠습니까?")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    setData()
                }.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                    enableAdd()
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
