package com.example.hyeok.myapplication

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val listener = object: View.OnClickListener, View.OnLongClickListener {
        override fun onClick(v: View?) {
            if (v is DecoratableImageView) {
                println(v.getDecoratable().toString())
            }
        }

        override fun onLongClick(v: View?): Boolean {
            return if (v is DecoratableImageView) {
                println(v.getDecoratable().toString())
                true
            } else {
                false
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listOf(image_view1, image_view2, image_view3, image_view4, image_view5, image_view6)
                .forEach {
                    //객체 식, 여러 메소드 오버라이드 이용해보기
                    it.setOnClickListener(listener)
                    it.setOnLongClickListener(listener)
                }

        //companion object 연습
        val rect = Decorate.getDefault(Decorate.Shape.RECT)
        val circle = Decorate.getDefault(Decorate.Shape.CIRCLE)
        val triangle = Decorate.getDefault(Decorate.Shape.TRIANGLE)
        val text1 = Decorate.getDefaultText("#1")
        val text2 = Decorate.getDefaultText("#2")
        val text3 = Decorate.getDefaultText("#3")

        image_view1.setDecoratable(AlphaDecorate(ColorDecorate(rect, Color.RED), 100))
        //순서가 바뀌어도 꾸밀 수 있음
        image_view2.setDecoratable(ColorDecorate(AlphaDecorate(circle, 100), Color.GREEN))
        image_view3.setDecoratable(AlphaDecorate(ColorDecorate(triangle, Color.BLUE), 255))

        image_view4.setDecoratable(AlphaDecorate(ColorDecorate(text1, Color.YELLOW), 150))
        //텍스트와 모양도 같이 그릴 수 있음
        image_view5.setDecoratable(AlphaDecorate(ColorDecorate(CircleDecorate(text2), Color.WHITE), 150))
        image_view6.setDecoratable(AlphaDecorate(ColorDecorate(TriangleDecorate(RectDecorate(text3)), Color.RED), 100))
    }
}
