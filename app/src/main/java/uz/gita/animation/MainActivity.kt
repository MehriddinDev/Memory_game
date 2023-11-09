package uz.gita.animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    lateinit var btnImg : ImageView
    private var n = 0
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       /*
        btnImg = findViewById(R.id.img)

        btnImg.setOnClickListener {
            if(n == 0) open()
            else close()
        }
    }

    private fun open() {
        btnImg.animate().setDuration(1000)
            .rotationY(89f)
            .withEndAction {
                *//*btnImg.rotationY = -89f
                btnImg.setImageResource(R.drawable.baseline_4k_24)*//*
                btnImg.animate().setDuration(1000)
                    .scaleX(250f)
                    *//*.rotationY(0f)*//*
                    .start()
            }
            .start()
        n = 1
    }
    fun close(){
        btnImg.animate().setDuration(1000)
            .rotationY(-91f)
            .withEndAction {
                btnImg.rotationY = 89f
                btnImg.setImageResource(R.drawable.baseline_3k_24)
                btnImg.animate().setDuration(1000)
                    .rotationY(0f)
                    .start()
            }
            .start()
        n = 0*/
    }
}