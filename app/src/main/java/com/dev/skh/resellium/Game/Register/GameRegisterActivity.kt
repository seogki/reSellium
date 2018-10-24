package com.dev.skh.resellium.Game.Register

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.CustomTextWatcher
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityGameRegisterBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class GameRegisterActivity : AppCompatActivity(), View.OnClickListener, GameRegisterPresenter.View {


    companion object {
        fun weakRef(view: GameRegisterPresenter.View): WeakReference<GameRegisterPresenter> {
            return WeakReference(GameRegisterPresenter(view))
        }
    }

    private lateinit var binding: ActivityGameRegisterBinding
    private val weakReference by lazy { weakRef(this) }

    private var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_register)
        binding.layoutAppbar?.title = "등록"
        binding.onClickListener = this
        binding.layoutAppbar?.onClickListener = this
        setView()
    }

    private fun setView() {
        binding.layoutAppbar?.layoutAdd?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.editMoney.addTextChangedListener(CustomTextWatcher(binding.editMoney))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_add -> {
                setRegisterDialog()
            }
            R.id.layout_undo -> {
                closeKeyboard()
                finish()
            }
        }
    }

    private fun setData() {
        closeKeyboard()
        binding.layoutAppbar?.layoutAdd?.isEnabled = false
        val platform = binding.radiogroupPlatform.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }.trim()
        val title = binding.editTitle.text.toString()
        val place = binding.editPlace.text.toString()
        val money = binding.editMoney.text.toString().replace(",".toRegex(), "").replace("원", "")
        val which = binding.radiogroupNewOld.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }.trim() + binding.radiogroupResell.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }.trim()
        if (platform.isNotEmpty() && place.isNotEmpty() && money.isNotEmpty()) {
            weakReference.get()?.sendData(platform, title, place, money, which)
        } else {
            Toast.makeText(this, "모두 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRegisterDialog() {
        AlertDialog.Builder(this@GameRegisterActivity, R.style.MyDialogTheme)
                .setTitle("등록")
                .setMessage("정말로 등록하시겠습니까?")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    setData()
                }.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun registerData(disposable: Disposable?) {
        this.disposable = disposable

        AlertDialog.Builder(this@GameRegisterActivity, R.style.MyDialogTheme)
                .setTitle("등록")
                .setMessage("등록되었습니다.")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }.setNegativeButton(null, null)
                .show()


        binding.layoutAppbar?.layoutAdd?.isEnabled = true
    }

    override fun errorData(disposable: Disposable?, error: String?) {
        this.disposable = disposable
        binding.layoutAppbar?.layoutAdd?.isEnabled = true
        DLog.e("error : $error")
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
