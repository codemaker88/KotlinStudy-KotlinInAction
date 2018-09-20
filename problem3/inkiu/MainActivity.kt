package study.kotlin.problem3

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import study.kotlin.problem3.view.*

class MainActivity : AppCompatActivity() {

    private val listener = object : View.OnClickListener, View.OnLongClickListener {
        override fun onClick(v: View?) {
            println("onClick : ${(v as? DecoratableImageView)?.getDecoratable().toString()}")
        }

        override fun onLongClick(v: View?): Boolean {
            println("onLongClick : ${(v as? DecoratableImageView)?.getDecoratable().toString()}")
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()

        listOf(image_view1, image_view2, image_view3, image_view4, image_view5, image_view6)
                .forEach {
                    //객체 식, 여러 메소드 오버라이드 이용해보기
                    it.setOnClickListener(listener)
                    it.setOnLongClickListener(listener)
                }

        //companion object 연습
        val rect = Decorate.createDefault(Decorate.Shape.RECT)
        val circle = Decorate.createDefault(Decorate.Shape.CIRCLE)
        val triangle = Decorate.createDefault(Decorate.Shape.TRIANGLE)
        val text1 = Decorate.createDefault("#1")
        val text2 = Decorate.createDefault("#2")
        val text3 = Decorate.createDefault("#3")

        val d1 = AlphaDecorate(
                ColorDecorate(rect, Color.RED),
                100
        )
        val d2 = ColorDecorate(
                AlphaDecorate(circle, 100),
                Color.GREEN
        )
        val d3 = AlphaDecorate(
                ColorDecorate(triangle, Color.BLUE),
                255
        )
        image_view1.setDecoratable(d1)
        image_view2.setDecoratable(d2)
        image_view3.setDecoratable(d3)

        image_view4.setDecoratable(AlphaDecorate(ColorDecorate(text1, Color.YELLOW), 150))
        image_view5.setDecoratable(AlphaDecorate(ColorDecorate(CircleDecorate(text2), Color.WHITE), 150))
        image_view6.setDecoratable(AlphaDecorate(ColorDecorate(TriangleDecorate(RectDecorate(text3)), Color.RED), 100))
    }
}
