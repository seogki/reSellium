package com.dev.skh.resellium.Game.Register

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
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


    private lateinit var binding: ActivityGameRegisterBinding
    private var weakReference: WeakReference<GameRegisterPresenter>? = null

    private var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_register)
        binding.layoutAppbar?.title = "등록"
        binding.onClickListener = this
        binding.layoutAppbar?.onClickListener = this
        setView()
        setMVP()
    }

    private fun setMVP(){
        weakReference = WeakReference(GameRegisterPresenter(this))
    }


    private fun setView(){
        binding.layoutAppbar?.constPlus?.setBackgroundColor(ContextCompat.getColor(this, R.color.ps4Color))
        binding.layoutAppbar?.layoutAdd?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.layoutAppbar?.layoutUndo?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.editMoney.addTextChangedListener(CustomTextWatcher(binding.editMoney))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_add -> {
                setData()
            }
            R.id.layout_undo -> {
                closeKeyboard()
                finish()
            }
            R.id.radio_ps4 -> setToolbarColor("PS")
            R.id.radio_xbox -> setToolbarColor("XBOX")
            R.id.radio_switch -> setToolbarColor("SWITCH")
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
            weakReference?.get()?.sendData(platform, title, place, money, which)
        } else {
            Toast.makeText(this, "모두 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    override fun registerData(disposable: Disposable?) {
        this.disposable = disposable
        finish()
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

    private fun setToolbarColor(text: String) {
        var color: Int? = null
        when (text) {
            "PS" -> color = ContextCompat.getColor(this, R.color.ps4Color)
            "XBOX" -> color = ContextCompat.getColor(this, R.color.xboxColor)
            "SWITCH" -> color = ContextCompat.getColor(this, R.color.switchColor)
        }
        if (color != null) {
            binding.layoutAppbar?.constPlus?.setBackgroundColor(color)
            binding.txtPlatform.setBackgroundColor(color)
            binding.txtTitle.setBackgroundColor(color)
            binding.txtMoney.setBackgroundColor(color)
            binding.txtNewOld.setBackgroundColor(color)
            binding.txtPlace.setBackgroundColor(color)
            binding.txtResell.setBackgroundColor(color)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
