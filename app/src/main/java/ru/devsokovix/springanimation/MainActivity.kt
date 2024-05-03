package ru.devsokovix.springanimation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import ru.devsokovix.springanimation.databinding.ActivityMainBinding

class MainActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var diffX = 0f
    private var diffY = 0f


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val springForce = SpringForce(0f).apply {
            stiffness = SpringForce.STIFFNESS_VERY_LOW
            dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        }

        val springAnimationX =
            SpringAnimation(binding.bools, DynamicAnimation.TRANSLATION_X).setSpring(springForce)
        val springAnimationY =
            SpringAnimation(binding.bools, DynamicAnimation.TRANSLATION_Y).setSpring(springForce)

        binding.bools.setOnTouchListener { v, event ->
            v.performClick()
            //Проверяем какое действие у нас произошло
            when (event.action) {
                //MotionEvent.ACTION_DOWN - вызывается, когда ваш палец коснулся экрана, то есть как бы опустился
                //вниз, поэтому и DOWN
                MotionEvent.ACTION_DOWN -> {
                    //Устанавливаем координаты для корректного перемещения
                    diffX = event.rawX - v.x
                    diffY = event.rawY - v.y

                    //Отменяем анимацию, если к примера нашу view еще "пружинит" с предыдущего раза
                    springAnimationX.cancel()
                    springAnimationY.cancel()
                }
                //MotionEvent.ACTION_MOVE - вызывается, когда мы перемещаем view, то есть меняются координаты
                //view
                MotionEvent.ACTION_MOVE -> {
                    //rawX, rawY текущие координаты view
                    binding.bools.x = event.rawX - diffX
                    binding.bools.y = event.rawY - diffY
                }
                //MotionEvent.ACTION_UP - вызывается, когда палец перестал касаться экрана
                MotionEvent.ACTION_UP -> {
                    //Стартуем анимацию возвращения в прежнее положение
                    springAnimationX.start()
                    springAnimationY.start()
                }
            }
            true
        }
    }
}

