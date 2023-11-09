package uz.gita.animation.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.animation.R
import uz.gita.animation.data.CardData
import uz.gita.animation.data.LevelEnum
import uz.gita.animation.databinding.ScreenGameBinding
import uz.gita.animation.repastory.AppRepastory
import uz.gita.animation.ui.dialog.WinDialog

class GameScreen : Fragment(R.layout.screen_game) {
    private val binding by viewBinding(ScreenGameBinding::bind)
    private val args by navArgs<GameScreenArgs>()
    private val repastory = AppRepastory()
    private var _height = 0
    private var defLevel = LevelEnum.EASY
    private var _width = 0
    private var attempCount = 0
    private lateinit var correct: MediaPlayer
    private lateinit var wrong: MediaPlayer
    private lateinit var win: MediaPlayer
    private lateinit var winGame: MediaPlayer
    private lateinit var gameOver: MediaPlayer
    private var isDialogShow = false
    private var isWin = 0
    private var progressMax = 0
    private var millsFuture: Long = 0
    private var winCount = 0
    private var timer: CountDownTimer? = null
    private var firstImage: ImageView? = null
    private var secondImage: ImageView? = null

    private val images = ArrayList<ImageView>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        defLevel = args.level

        isWin = (defLevel.horCount * defLevel.verCount) / 2

        when (defLevel.horCount * defLevel.verCount) {
            6 -> {
                millsFuture = 60000
                progressMax = 60000
            }
            12 -> {
                millsFuture = 191000
                progressMax = 191000
            }
            20 -> {
                millsFuture = 271000
                progressMax = 271000
            }
        }

        correct = MediaPlayer.create(requireContext(), R.raw.win)
        wrong = MediaPlayer.create(requireContext(), R.raw.lose)
        win = MediaPlayer.create(requireContext(), R.raw.yutdi)
        if (defLevel.verCount == 3) {
            binding.levelNumber.text = "easy"
        } else if (defLevel.verCount == 4) {
            binding.levelNumber.text = "medium"
        } else if (defLevel.verCount == 5) {
            binding.levelNumber.text = "hard"
        }

        binding.space.post {
            _height = binding.container.height / (defLevel.verCount)
            _width = binding.container.width / (defLevel.horCount)

            val count = defLevel.horCount * defLevel.verCount
            val ls = repastory.getData(count)
            describedata(ls)
            clickListener()
            binding.imgBackgroun.setImageResource(repastory.getImageForBg())
        }

        binding.menu.setOnClickListener {
            timer?.cancel()
            findNavController().navigateUp()
        }

        binding.reload.setOnClickListener {
            attempCount = 0
            binding.txtAttemp.text = attempCount.toString()
            binding.container.clearAnimation()
            binding.container.removeAllViews()
            val count = defLevel.horCount * defLevel.verCount
            describedata(repastory.getData(count))
            clickListener()
            binding.imgBackgroun.setImageResource(repastory.getImageForBg())

            firstImage = null
            secondImage = null

            timer?.cancel()
            timer?.start()
        }


    }

    private fun describedata(ls: List<CardData>) {


        for (i in 0 until defLevel.horCount) {
            for (j in 0 until defLevel.verCount) {
                val image = ImageView(requireContext())
                binding.container.addView(image)
                val lp = image.layoutParams as ConstraintLayout.LayoutParams
                lp.apply {
                    width = _width
                    height = _height
                }

                //lp.setMargins(4, 4, 4, 4)

                image.layoutParams = lp
                image.tag = ls[i * defLevel.verCount + j]
                image.setImageResource(R.drawable.i_back)

                image.animate()
                    .x(i * _width * 1f)
                    .y(j * _height * 1f)
                    .setDuration(1000)
                    .start()
                images.add(image)
            }
        }
    }


    private fun clickListener() {
        images.forEach { imageVieww ->
            imageVieww.setOnClickListener {
                Log.d("YYY", "oldin: ${imageVieww.getTag()}")
                if (imageVieww.rotationY == 0f) {
                    Log.d("YYY", "keyin: ${imageVieww.getTag()}")

                    if (firstImage == null) {
                        firstImage = imageVieww
                        open(firstImage!!)
                    } else if (secondImage == null) {
                        secondImage = imageVieww
                        open(secondImage!!)
                        binding.reload.isEnabled = false
                    }

                    if (firstImage != null && secondImage != null) {
                        lifecycleScope.launch {
                            for (i in 0 until binding.container.childCount) {
                                val img = binding.container.getChildAt(i)
                                img.isEnabled = false
                            }


                            delay(1300)
                            if ((firstImage!!.tag as CardData).id == (secondImage!!.tag as CardData).id) {
                                // to'g'ri topilganda
                                winCount++

                                correct.start()

                                firstImage!!.animate()
                                    .setDuration(500)
                                    .rotationY(270f)
                                    .start()

                                secondImage!!.animate()
                                    .setDuration(500)
                                    .rotationY(270f)
                                    .start()
                                delay(700)
                                attempCount++
                                if (winCount == isWin) {
                                    showDialog(R.drawable.wonnn)
                                    winGame = MediaPlayer.create(requireContext(), R.raw.yutdi)
                                    winGame.start()

                                }

                                binding.txtAttemp.text = attempCount.toString()

                                binding.reload.isEnabled = true

                            } else {
                                wrong.start()
                                close(firstImage!!)
                                close(secondImage!!)
                                delay(700)
                                binding.reload.isEnabled = true
                                attempCount++
                                binding.txtAttemp.text = attempCount.toString()
                            }
                            for (i in 0 until binding.container.childCount) {
                                val img = binding.container.getChildAt(i)
                                img.isEnabled = true
                            }
                            firstImage = null
                            secondImage = null
                        }
                    }

                }
            }
        }
    }

    private fun showDialog(img: Int) {
        timer?.cancel()
        isDialogShow = true
        lifecycleScope.launch {
            val dialog = WinDialog.getInstance(attempCount.toString(), img)
            dialog.setRestartListener {
                timer?.start()
                attempCount = 0
                winCount = 0
                binding.txtAttemp.text = "0"
                dialog.dismiss()
                binding.container.clearAnimation()
                binding.container.removeAllViews()
                val count = defLevel.horCount * defLevel.verCount
                describedata(repastory.getData(count))
                clickListener()
                binding.imgBackgroun.setImageResource(repastory.getImageForBg())

                firstImage = null
                secondImage = null
            }
            dialog.setMenuDialog {
                dialog.dismiss()
                findNavController().navigateUp()
            }

            dialog.show(childFragmentManager, "")
        }

    }

    private fun open(imageVieww: ImageView) {

        //   Log.d("RRR",count.toString())

        imageVieww.animate()
            .setDuration(350)
            .rotationY(89f)
            .withEndAction {
                val data = imageVieww.tag as CardData
                imageVieww.setImageResource(data.imgRes)
                imageVieww.rotationY = 271f
                imageVieww.animate()
                    .setDuration(350)
                    .rotationY(360f)
                    .start()
            }
            .start()
    }

    private fun close(imageVieww: ImageView) {
        imageVieww.rotationY = 0f
        imageVieww.animate()
            .setDuration(350)
            .rotationY(-89f)
            .withEndAction {
                val data = imageVieww.tag as CardData
                imageVieww.setImageResource(R.drawable.i_back)
                imageVieww.rotationY = 91f
                imageVieww.animate()
                    .setDuration(350)
                    .rotationY(0f)
                    .start()
            }
            .start()

    }

    override fun onStart() {
        super.onStart()
        timer = object : CountDownTimer(millsFuture, 1) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBar.setProgress(millisUntilFinished.toInt())
                binding.progressBar.max = progressMax
            }

            override fun onFinish() {
                Log.d("JJJ", "onTick: finish()")
                cancel()
                gameOver = MediaPlayer.create(requireContext(), R.raw.yutkazdi)
                gameOver.start()
                showDialog(R.drawable.gameoverr)
            }

        }

        timer?.start()

    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

   fun narxniTop(narx:Double, kg:Int){
       val natija = narx * kg
       println("jami narx = $natija so'm")
   }

}