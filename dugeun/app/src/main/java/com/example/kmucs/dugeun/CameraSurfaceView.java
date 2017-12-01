package com.example.kmucs.dugeun;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

// 카메라 미리보기 surface view


public class CameraSurfaceView extends SurfaceView implements Callback {

	public static final String TAG = "CameraSurfaceView";

	private SurfaceHolder mHolder;			//실질적으로 surfaceview를 관리하는 객체 = surfaceholder
    private Camera mCamera = null;

	public CameraSurfaceView(Context context) {
		super(context);

		mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	//
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		mCamera.startPreview();
	}

	// 카메라 열기
	public void surfaceCreated(SurfaceHolder holder) {
		openCamera();
	}

	// 카메라 끄기
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopPreview();
	}

	public Surface getSurface() {
		return mHolder.getSurface();
	}

	// 사진 찍기! (.takePicture)
	public boolean capture(Camera.PictureCallback jpegHandler) {
        if (mCamera != null) {
        	mCamera.takePicture(null, null, jpegHandler);
            return true;
        } else {
            return false;
        }
    }

	// 미리보기 화면을 멈추고, release() : 다른 앱에서 카메라를 사용할 수 있도록 해주는 것.
	public void stopPreview() {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	// 카메라 열기
	public void startPreview() {
		openCamera();
		//최종적으로 프리뷰 화면 보여줌
		mCamera.startPreview();
	}

	// 카메라 오픈 (카메라 실행하고 미리보기 화면 띄우기)
	public void openCamera() {
		mCamera = Camera.open();
        try {
			// mHolder에 프리뷰를 나오게 해라.
        	mCamera.setPreviewDisplay(mHolder);
        }
        catch (Exception ex) {
            Log.e(TAG, "Failed to set camera preview display", ex);
        }
	}

}
