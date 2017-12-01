package com.example.kmucs.dugeun;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

/** coverflow : 중간(선택된 사진) 을 제외하고는 가운데를 기준으로 옆에 있는 사진들은 기울어져 보이는 효과
 *  (앨범에서 사진 불러오기 선택시 보여짐) */


public class CoverFlow extends Gallery {

	private Camera camera = new Camera();

	// 미리보기로 나오는 사진과 사진 사이의 각도
	// 숫자가 작을수록 사진을 넘겨볼 때 벌어진 부분이 좁아서 평면처럼 이어져 보임.
	public static int maxRotationAngle = 50;

	// 미리보기 사진 화면의 크기 설정
	// 숫자가 작을수록 화면이 커보임
	public static int maxZoom = -350;

	private int centerPoint;

	/**
	 * Constructor
	 *
	 * @param context
	 */
	public CoverFlow(Context context) {
		super(context);

		init();
	}

	/**
	 * Constructor
	 *
	 * @param context
	 * @param attrs
	 */
	public CoverFlow(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	/**
	 * Constructor
	 *
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	/**
	 * Initialization
	 */
	private void init() {
		this.setStaticTransformationsEnabled(true);
	}


	public int getMaxRotationAngle() {
		return maxRotationAngle;
	}

	public void setMaxRotationAngle(int rotationAngle) {
		maxRotationAngle = rotationAngle;
	}

	public int getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(int zoom) {
		maxZoom = zoom;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}


	// 갤러리 뷰 안에서 스크롤 할 때, child가 어디에 위치 했는지 알고 싶음.
	protected boolean getChildStaticTransformation(View child, Transformation t) {

		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth() ;
		int rotationAngle = 0;
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);

		if (childCenter == centerPoint) {
			transformImageBitmap((ImageView) child, t, 0);
		}
		else {
			rotationAngle = (int) (((float) (centerPoint - childCenter)/ childWidth) *  maxRotationAngle);
			if (Math.abs(rotationAngle) > maxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -maxRotationAngle : maxRotationAngle;
			}
			transformImageBitmap((ImageView) child, t, rotationAngle);
		}

		return true;

	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		centerPoint = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void transformImageBitmap(ImageView child, Transformation t, int rotationAngle) {
		camera.save();

		final Matrix imageMatrix = t.getMatrix();;
		final int imageHeight = child.getLayoutParams().height;;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);

		camera.translate(0.0f, 0.0f, 100.0f);

		if ( rotation < maxRotationAngle ) {
			float zoomAmount = (float) (maxZoom +  (rotation * 1.5));
			camera.translate(0.0f, 0.0f, zoomAmount);
		}

		camera.rotateY(rotationAngle);
		camera.getMatrix(imageMatrix);

		imageMatrix.preTranslate(-(imageWidth/2), -(imageHeight/2));
		imageMatrix.postTranslate((imageWidth/2), (imageHeight/2));

		camera.restore();

	}

}
