## **[문제 3] Chapter4 클래스, 객체, 인터페이스** 
이번 과제는 4장의 내용을 이용하여 안드로이드에서 연습해봅시다.
최종적으로 아래의 그림과 같이 완성해 보는것이 목표입니다.

[그림 - 이번엔 이런걸 한번 해봅시다]
![problem3](https://user-images.githubusercontent.com/23000328/45427181-33527300-b6d9-11e8-8777-cbe9cab9b7ed.png)

**이번 과제로 아래의 내용들을 연습해 봅시다.**
* 인터페이스 사용해보기
* 클래스 사용해보기
* 클래스 위임 by 사용해보기
* companion object 사용해보기
* 객체식 - 여러 메소드를 오버라이드 해보기

위의 내용들이 포함 된다면 상세 구현은 마음대로 하셔도 좋습니다. 
또는 문제 이해를 위해 올려진 코드 조각을 활용하셔도 좋습니다. (주요 부분 제거)

**[문제 3-1]** 
![decoratableimageview](https://user-images.githubusercontent.com/23000328/45429231-db6a3b00-b6dd-11e8-9d2c-2228b867a6cf.png)

* DecoratableImageView
기본적으로 위의 그림과 같이 이미지를 그려주고 있는 이미지 뷰가 있습니다.
```
//activity_main.xml
<study.kotlin.problem3.view.DecoratableImageView
        android:id="@+id/image_view1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/ic_launcher_background" />
```

이것을 처음 봤던 그림처럼 꾸밀 수 있도록 만들어 보세요.
예를들면 아래와 같습니다.
```
override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
	//꾸밀 수 있는것이 있다면 추가적으로 그려줌.
        mDecoratable?.onDraw(canvas)
    }
```
문제 이해를 돕기 위해 DecoratableImageView.kt 를 참고하세요.


**[문제 3-2]**
DecoratableImageView를 아래처럼 꾸밀 수 있도록 꾸미기 클래스들을 만들어 보세요.
```
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
```
문제 이해를 돕기 위해 MainActivity.kt, DecorateItems.kt 를 참고하세요.


**[문제 3-3]**
아래 처럼 각 이미지뷰들을 클릭, 롱클릭 하면 꾸며진 정보들이 로그로 출력되도록 해보세요.
```
onClick : ColorDecorate + AlphaDecorate + CircleDecorate + DefaultDecorate
onLongClick : ColorDecorate + AlphaDecorate + CircleDecorate + DefaultDecorate

onClick : AlphaDecorate + ColorDecorate + TriangleDecorate + RectDecorate + TextDecorate + DefaultDecorate
onLongClick : AlphaDecorate + ColorDecorate + TriangleDecorate + RectDecorate + TextDecorate + DefaultDecorate
```
문제 이해를 돕기 위해 MainActivity.kt 를 참고하세요.
