package com.dev.skh.resellium.Game.Register

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.CustomTextWatcher
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityGameRegisterBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class GameRegisterActivity : InnerBaseActivity()
        , View.OnClickListener
        , GameRegisterPresenter.View {


    companion object {
        fun weakRef(view: GameRegisterPresenter.View): WeakReference<GameRegisterPresenter> {
            return WeakReference(GameRegisterPresenter(view))
        }
    }

    private lateinit var binding: ActivityGameRegisterBinding
    private val weakReference by lazy { weakRef(this) }

    private var disposable: Disposable? = null
    private var platform = "PS"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_register)
        binding.layoutAppbar?.title = "등록"
        binding.activity = this
        binding.layoutAppbar?.onClickListener = this
        setView()
    }

    private fun setView() {
        binding.editMoney.addTextChangedListener(CustomTextWatcher(binding.editMoney))
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_add -> setRegisterDialog()
            R.id.layout_undo -> {
                closeKeyboard()
                finish()
            }
        }
    }

    fun setRadio(view: View?) {
        when (view?.id) {
            R.id.radio_buy -> {
                setBold(binding.radioBuy)
                setDefault(binding.radioSold)
            }
            R.id.radio_sold -> {
                setBold(binding.radioSold)
                setDefault(binding.radioBuy)
            }
            R.id.radio_new -> {
                setBold(binding.radioNew)
                setDefault(binding.radioOld)
            }
            R.id.radio_old -> {
                setBold(binding.radioOld)
                setDefault(binding.radioNew)
            }
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


    private fun setRegisterFalse() {
        binding.layoutAppbar?.layoutAdd?.isEnabled = false
        binding.imgMark.isEnabled = false
    }

    private fun setRegisterTrue() {
        binding.layoutAppbar?.layoutAdd?.isEnabled = true
        binding.imgMark.isEnabled = true
    }

    fun setRegisterDialog() {
        AlertDialog.Builder(this@GameRegisterActivity, R.style.MyDialogTheme)
                .setTitle("게임")
                .setMessage("정말로 등록하시겠습니까?")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    setData()
                }.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                    setRegisterTrue()
                }
                .show()
    }

    private fun setData() {
        closeKeyboard()
        setRegisterFalse()
        val title = binding.editTitle.text.toString()
        val place = binding.editPlace.text.toString()
        val money = binding.editMoney.text.toString().replace(",".toRegex(), "").replace("원", "")
        val which = binding.radiogroupNewOld.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }.trim() + binding.radiogroupResell.checkedRadioButtonId.let { findViewById<RadioButton>(it).text.toString() }.trim()
        if (place.isNotEmpty() && money.isNotEmpty()) {
            weakReference.get()?.sendData(platform, title, place, money, which)
        } else {
            setRegisterTrue()
            Toast.makeText(this, "모두 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    override fun registerData(disposable: Disposable?) {
        this.disposable = disposable

        AlertDialog.Builder(this@GameRegisterActivity, R.style.MyDialogTheme)
                .setTitle("게임")
                .setMessage("등록되었습니다")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }.setNegativeButton(null, null)
                .show()

        setRegisterTrue()
    }

    override fun errorData(disposable: Disposable?, error: String?) {
        this.disposable = disposable
        DLog.e("error : $error")
        showErrorToast()
        setRegisterTrue()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
