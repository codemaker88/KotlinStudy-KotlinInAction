package com.rounz.decorator

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //companion object 연습
        val rect = Decoration.getDefault(Decoration.Shape.RECT)
        val circle = Decoration.getDefault(Decoration.Shape.CIRCLE)
        val triangle = Decoration.getDefault(Decoration.Shape.TRIANGLE)
        val text1 = Decoration.getDefaultText("#1")
        val text2 = Decoration.getDefaultText("#2")
        val text3 = Decoration.getDefaultText("#3")

        imageView1.decorator = AlphaDecorator(ColorDecorator(rect, Color.RED), 100)
//순서가 바뀌어도 꾸밀 수 있음
        imageView2.decorator = ColorDecorator(AlphaDecorator(circle, 100), Color.GREEN)
        imageView3.decorator = AlphaDecorator(ColorDecorator(triangle, Color.BLUE), 255)

        imageView4.decorator = AlphaDecorator(ColorDecorator(text1, Color.YELLOW), 150)
//텍스트와 모양도 같이 그릴 수 있음
        imageView5.decorator = AlphaDecorator(ColorDecorator(CircleDecorator(text2), Color.WHITE), 150)
        imageView6.decorator = AlphaDecorator(ColorDecorator(TriangleDecorator(RectDecorator(text3)), Color.RED), 100)

        listOf(imageView1, imageView2, imageView3, imageView4, imageView5, imageView6)
                .forEach {
                    val listener = object : View.OnClickListener, View.OnLongClickListener {
                        override fun onClick(v: View?) {
                            println("onClick : ${it.decorator.toString()}")
                        }

                        override fun onLongClick(v: View?): Boolean {
                            println("onLongClick : ${it.decorator.toString()}")
                            return true
                        }
                    }
                    //객체 식, 여러 메소드 오버라이드 이용해보기
                    it.setOnClickListener(listener)
                    it.setOnLongClickListener(listener)
                }
    }
}
