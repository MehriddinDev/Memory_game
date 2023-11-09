package uz.gita.animation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.animation.R
import uz.gita.animation.data.LevelEnum
import uz.gita.animation.databinding.ScreenLevelBinding

class LevelScreen:Fragment(R.layout.screen_level) {
    private val binding by viewBinding(ScreenLevelBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.easy.setOnClickListener {
        openGameScreen(LevelEnum.EASY)
        }

        binding.medium.setOnClickListener {
            openGameScreen(LevelEnum.MEDIUM)
        }

        binding.hard.setOnClickListener {
        openGameScreen(LevelEnum.HARD)
        }

        binding.btnInfo.setOnClickListener {
            showDialogInfo()
        }
    }

    private fun openGameScreen(level : LevelEnum){
        findNavController().navigate(LevelScreenDirections.actionLevelScreenToGameScreen(level))
    }

    private fun showDialogInfo(){
      AlertDialog.Builder(requireContext())
            .setTitle("Memory Game")
            .setMessage("Play and develop your mind\n\nDeveloper: Mehriddin Sobirov\nGITA Academy of programmers")
            .create()
            .show()
    }
}