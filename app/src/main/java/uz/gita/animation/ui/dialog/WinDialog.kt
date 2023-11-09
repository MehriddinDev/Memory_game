package uz.gita.animation.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.animation.R
import uz.gita.animation.databinding.WinDialogBinding

class WinDialog : DialogFragment(R.layout.win_dialog) {
    private val binding by viewBinding(WinDialogBinding::bind)
    private var restartListener: (() -> Unit)? = null
    private var homeListener: (() -> Unit)? = null

    companion object {
        fun getInstance(attemp: String, img: Int): WinDialog {
            val dialog = WinDialog()
            val args = Bundle()
            args.putString("attemp", attemp)
            args.putInt("img", img)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                Log.d("TTT", "handleOnBackPressed: ")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attemp = arguments?.getString("attemp")
        val img = arguments?.getInt("img")!!
        binding.txtScoreAttemp.text = attemp
        binding.img.setImageResource(img)
        binding.home.setOnClickListener {
            homeListener?.invoke()
        }

        binding.restart.setOnClickListener {
            restartListener?.invoke()
        }

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog?.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    fun setRestartListener(k: (() -> Unit)) {
        restartListener = k
    }

    fun setMenuDialog(k: (() -> Unit)) {
        homeListener = k
    }

    fun setAttemp(n: Int) {
        binding.txtScoreAttemp.text = n.toString()
    }

    fun setImage(img: Int) {
        binding.img.setImageResource(img)
    }


}