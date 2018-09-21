package study.kotlin.problem3

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import kotlinx.android.synthetic.main.activity_main.image_view1
import kotlinx.android.synthetic.main.activity_main.image_view2
import kotlinx.android.synthetic.main.activity_main.image_view3
import kotlinx.android.synthetic.main.activity_main.image_view4
import kotlinx.android.synthetic.main.activity_main.image_view5
import kotlinx.android.synthetic.main.activity_main.image_view6
import study.kotlin.problem3.view.AlphaDecorate
import study.kotlin.problem3.view.CircleDecorate
import study.kotlin.problem3.view.ColorDecorate
import study.kotlin.problem3.view.Decorate
import study.kotlin.problem3.view.RectDecorate
import study.kotlin.problem3.view.TriangleDecorate

class MainActivity : AppCompatActivity() {

  private val listener = object : OnClickListener, OnLongClickListener {
    override fun onClick(v: View?) {
      println("A")
    }

    override fun onLongClick(v: View?): Boolean {
      println("A")
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

    //companion object 연습
    val rect = Decorate.getDefault(Decorate.Shape.RECT)
    val circle = Decorate.getDefault(Decorate.Shape.CIRCLE)
    val triangle = Decorate.getDefault(Decorate.Shape.TRIANGLE)
    val text1 = Decorate.getDefaultText("#1")
    val text2 = Decorate.getDefaultText("#2")
    val text3 = Decorate.getDefaultText("#3")

    image_view1.setDecoratable(AlphaDecorate(100, ColorDecorate(rect, Color.RED)))
    //순서가 바뀌어도 꾸밀 수 있음
    image_view2.setDecoratable(ColorDecorate(AlphaDecorate(100, circle), Color.GREEN))
    image_view3.setDecoratable(AlphaDecorate(255, ColorDecorate(triangle, Color.BLUE)))

    image_view4.setDecoratable(AlphaDecorate(150, ColorDecorate(text1, Color.YELLOW)))
    //텍스트와 모양도 같이 그릴 수 있음
    image_view5.setDecoratable(
      AlphaDecorate(150, ColorDecorate(CircleDecorate(text2), Color.WHITE)))
    image_view6.setDecoratable(
      AlphaDecorate(100, ColorDecorate(TriangleDecorate(RectDecorate(text3)), Color.RED)))
  }
}
