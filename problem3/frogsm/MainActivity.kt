package frogsm.kotlin.problem3

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val listener = object : View.OnClickListener, View.OnLongClickListener {
        override fun onClick(v: View?) {
            Log.d("MainActivity", "onClick: ${(v as DecoratableImageView).getDecoratable().toString()}")
        }

        override fun onLongClick(v: View?): Boolean {
            Log.d("MainActivity", "onLongClick: ${(v as DecoratableImageView).getDecoratable().toString()}")
            return true
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

        val rect = Decorate.getDefault(Decorate.Shape.RECT)
        val circle = Decorate.getDefault(Decorate.Shape.CIRCLE)
        val triangle = Decorate.getDefault(Decorate.Shape.TRIANGLE)
        val text1 = Decorate.getDefaultText("#1")
        val text2 = Decorate.getDefaultText("#2")
        val text3 = Decorate.getDefaultText("#3")

        image_view1.setDecoratable(AlphaDecorate(ColorDecorate(rect, Color.RED), 100))
        image_view2.setDecoratable(ColorDecorate(AlphaDecorate(circle, 100), Color.GREEN))
        image_view3.setDecoratable(AlphaDecorate(ColorDecorate(triangle, Color.BLUE), 255))
        image_view4.setDecoratable(AlphaDecorate(ColorDecorate(text1, Color.YELLOW), 150))
        image_view5.setDecoratable(AlphaDecorate(ColorDecorate(CircleDecorate(text2), Color.WHITE), 150))
        image_view6.setDecoratable(AlphaDecorate(ColorDecorate(TriangleDecorate(RectDecorate(text3)), Color.RED), 100))
    }
}
